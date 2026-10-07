#!/usr/bin/env bash
# Back up top-level regular files to a timestamped directory over explicit FTPS.
# Configuration: db/.env (copy db/.env.example); options override its paths.
# Usage: ./backup_files_ftp.sh [-s SOURCE_DIR] [-r REMOTE_DIR] [-h]
set -euo pipefail
umask 077

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ENV_FILE="${SCRIPT_DIR}/.env"
if [[ -f "${ENV_FILE}" ]]; then
    # shellcheck source=/dev/null
    source "${ENV_FILE}"
fi

usage() {
    echo "Usage: $(basename "$0") [-s SOURCE_DIR] [-r REMOTE_DIR] [-h]"
    echo "  Settings come from ${ENV_FILE} (see .env.example)."
    echo "  -s SOURCE_DIR   local directory (FTP_BACKUP_SOURCE_DIR)"
    echo "  -r REMOTE_DIR   remote base directory (FTP_BACKUP_REMOTE_DIR)"
    echo "  -h              show this help"
}

while getopts ":s:r:h" opt; do
    case "${opt}" in
        s) FTP_BACKUP_SOURCE_DIR="${OPTARG}" ;;
        r) FTP_BACKUP_REMOTE_DIR="${OPTARG}" ;;
        h) usage; exit 0 ;;
        \?) echo "ERROR: unknown option -${OPTARG}" >&2; usage >&2; exit 1 ;;
        :)  echo "ERROR: option -${OPTARG} requires an argument" >&2; usage >&2; exit 1 ;;
    esac
done

for variable in FTP_HOST FTP_USER FTP_PASSWORD FTP_BACKUP_SOURCE_DIR FTP_BACKUP_REMOTE_DIR; do
    if [[ -z "${!variable:-}" ]]; then
        echo "ERROR: ${variable} is required (set it in ${ENV_FILE})" >&2
        exit 1
    fi
done

FTP_PORT="${FTP_PORT:-21}"
if [[ ! -d "${FTP_BACKUP_SOURCE_DIR}" || ! -r "${FTP_BACKUP_SOURCE_DIR}" ]]; then
    echo "ERROR: source directory is missing or unreadable: ${FTP_BACKUP_SOURCE_DIR}" >&2
    exit 1
fi
command -v curl >/dev/null 2>&1 || { echo "ERROR: curl not found in PATH" >&2; exit 1; }

# dotglob includes hidden files; directories and symlinks are not uploaded.
shopt -s nullglob dotglob
files=()
for file in "${FTP_BACKUP_SOURCE_DIR}"/*; do
    if [[ -f "${file}" && ! -L "${file}" ]]; then
        files+=("${file}")
    fi
done
if (( ${#files[@]} == 0 )); then
    echo "ERROR: no regular files to back up in ${FTP_BACKUP_SOURCE_DIR}" >&2
    exit 1
fi

credentials="${FTP_USER}:${FTP_PASSWORD}"
if [[ "${credentials}" == *$'\n'* || "${credentials}" == *$'\r'* ]]; then
    echo "ERROR: FTP credentials cannot contain newlines" >&2
    exit 1
fi
credentials="${credentials//\\/\\\\}"
credentials="${credentials//\"/\\\"}"

# Encode spaces and URL metacharacters in file names without changing the
# slash-separated FTP path (same remote path convention as the DB backup).
encode_path() {
    local LC_ALL=C path="$1" char hex encoded='' i
    for ((i = 0; i < ${#path}; i++)); do
        char="${path:i:1}"
        case "${char}" in
            [a-zA-Z0-9._~/-]) encoded+="${char}" ;;
            *) printf -v hex '%%%02X' "'${char}"; encoded+="${hex}" ;;
        esac
    done
    printf '%s' "${encoded}"
}

timestamp="$(date +%y%m%d_%H%M%S)"
remote_dir="${FTP_BACKUP_REMOTE_DIR%/}/${timestamp}"
for file in "${files[@]}"; do
    remote_path="$(encode_path "${remote_dir#/}/$(basename "${file}")")"
    url="ftp://${FTP_HOST}:${FTP_PORT}/${remote_path}"
    printf 'user = "%s"\n' "${credentials}" | curl --config - --ssl-reqd --fail --silent --show-error \
        --ftp-create-dirs --upload-file "${file}" "${url}"
    echo "Uploaded: ${url}"
done
echo "Backed up ${#files[@]} file(s) to ${remote_dir}"
