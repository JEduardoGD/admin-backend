#!/usr/bin/env bash
#
# Backup of the db_register MySQL database.
# Configuration: db/.env (copy db/.env.example). Command-line options override it.
# Usage: ./backup_db_register.sh [-H HOST] [-P PORT] [-u USER] [-p PASSWORD]
#                                [-d DBNAME] [-z] [-o OUTPUT_DIR] [-f] [-h]
#   -z  compress the resulting .sql file into a .zip and remove the plain file
#   -o  directory for the backup (overrides OUTPUT_DIR from db/.env)
#   -f  upload the resulting .sql or .zip via explicit FTPS (requires FTP_HOST,
#       FTP_USER, FTP_PASSWORD; optional FTP_PORT and FTP_REMOTE_DIR)
# Example: ./backup_db_register.sh
#
set -euo pipefail
umask 077

# ---------------------------------------------------------------------------
# Load backup settings from the .env next to this script (not the app's .env).
# ---------------------------------------------------------------------------
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ENV_FILE="${SCRIPT_DIR}/.env"
if [[ -f "${ENV_FILE}" ]]; then
    # shellcheck source=/dev/null
    source "${ENV_FILE}"
fi

# ---------------------------------------------------------------------------
# Optional defaults / option parsing
# ---------------------------------------------------------------------------
DB_PASSWORD="${DB_PASSWORD:-}"
OUTPUT_DIR="${OUTPUT_DIR:-${SCRIPT_DIR}/backups}"
if [[ "${OUTPUT_DIR}" != /* ]]; then
    OUTPUT_DIR="${SCRIPT_DIR}/${OUTPUT_DIR}"
fi
ZIP="${ZIP:-false}"
FTP="${FTP:-false}"

usage() {
    echo "Usage: $(basename "$0") [-H HOST] [-P PORT] [-u USER] [-p PASSWORD] [-d DBNAME] [-z] [-o OUTPUT_DIR] [-f] [-h]"
    echo "  Settings come from ${ENV_FILE} (see .env.example); options override them."
    echo "  -H HOST         database host (DB_HOST)"
    echo "  -P PORT         database port (DB_PORT)"
    echo "  -u USER         database user (DB_USER)"
    echo "  -p PASSWORD     database password (DB_PASSWORD)"
    echo "  -d DBNAME       database name (DB_NAME)"
    echo "  -z              zip the result (ZIP=true removes the plain .sql)"
    echo "  -o OUTPUT_DIR   output directory (overrides OUTPUT_DIR from .env)"
    echo "  -f              upload via explicit FTPS (FTP=true)"
    echo "                  FTP_HOST, FTP_PORT, FTP_USER, FTP_PASSWORD, FTP_REMOTE_DIR"
    echo "  -h              show this help"
    exit 0
}

while getopts ":H:P:u:p:d:zo:fh" opt; do
    case "${opt}" in
        H) DB_HOST="${OPTARG}" ;;
        P) DB_PORT="${OPTARG}" ;;
        u) DB_USER="${OPTARG}" ;;
        p) DB_PASSWORD="${OPTARG}" ;;
        d) DB_NAME="${OPTARG}" ;;
        z) ZIP=true ;;
        o) OUTPUT_DIR="${OPTARG}" ;;
        f) FTP=true ;;
        h) usage ;;
        \?) echo "ERROR: unknown option -${OPTARG}" >&2; usage ;;
        :)  echo "ERROR: option -${OPTARG} requires an argument" >&2; usage ;;
    esac
done

for variable in DB_HOST DB_PORT DB_USER DB_NAME; do
    if [[ -z "${!variable:-}" ]]; then
        echo "ERROR: ${variable} is required (set it in ${ENV_FILE} or use a command-line option)" >&2
        exit 1
    fi
done

if [[ "${FTP}" == true ]]; then
    for variable in FTP_HOST FTP_USER FTP_PASSWORD; do
        if [[ -z "${!variable:-}" ]]; then
            echo "ERROR: ${variable} is required for FTP upload" >&2
            exit 1
        fi
    done
    FTP_CREDENTIALS="${FTP_USER}:${FTP_PASSWORD}"
    if [[ "${FTP_CREDENTIALS}" == *$'\n'* || "${FTP_CREDENTIALS}" == *$'\r'* ]]; then
        echo "ERROR: FTP credentials cannot contain newlines" >&2
        exit 1
    fi
    command -v curl >/dev/null 2>&1 || { echo "ERROR: curl not found in PATH" >&2; exit 1; }
    FTP_PORT="${FTP_PORT:-21}"
    FTP_REMOTE_DIR="${FTP_REMOTE_DIR:-}"
fi

TIMESTAMP="$(date +%y%m%d_%H%M%S)"
SQL_FILE="${OUTPUT_DIR}/${DB_NAME}_${TIMESTAMP}.sql"
ZIP_FILE="${OUTPUT_DIR}/${DB_NAME}_${TIMESTAMP}.zip"

command -v mysqldump >/dev/null 2>&1 || { echo "ERROR: mysqldump not found in PATH" >&2; exit 1; }
mkdir -p "${OUTPUT_DIR}"

# ---------------------------------------------------------------------------
# Dump: every row in a single INSERT (--skip-extended-insert),
# structure + data + routines/triggers/events
# ---------------------------------------------------------------------------
DUMP_ARGS=(
    --host="${DB_HOST}"
    --port="${DB_PORT}"
    --user="${DB_USER}"
    --skip-extended-insert
    --routines
    --triggers
    --events
    --single-transaction
    --default-character-set=utf8mb4
    --databases "${DB_NAME}"
)

if [[ -n "${DB_PASSWORD}" ]]; then
    MYSQL_PWD="${DB_PASSWORD}" mysqldump "${DUMP_ARGS[@]}" > "${SQL_FILE}"
else
    mysqldump "${DUMP_ARGS[@]}" > "${SQL_FILE}"
fi

echo "Backup created: ${SQL_FILE}"
BACKUP_FILE="${SQL_FILE}"

# ---------------------------------------------------------------------------
# Optional zip
# ---------------------------------------------------------------------------
if [[ "${ZIP}" == true ]]; then
    command -v zip >/dev/null 2>&1 || { echo "ERROR: zip not found in PATH" >&2; exit 1; }
    (cd "${OUTPUT_DIR}" && zip -q "${ZIP_FILE}" "$(basename "${SQL_FILE}")")
    rm -f "${SQL_FILE}"
    echo "Compressed:     ${ZIP_FILE}"
    BACKUP_FILE="${ZIP_FILE}"
fi

# ---------------------------------------------------------------------------
# Optional FTPS upload; keep the local backup even if the upload fails.
# Supply credentials through curl's standard input, not process arguments.
# ---------------------------------------------------------------------------
if [[ "${FTP}" == true ]]; then
    FTP_CREDENTIALS="${FTP_CREDENTIALS//\\/\\\\}"
    FTP_CREDENTIALS="${FTP_CREDENTIALS//\"/\\\"}"
    FTP_URL="ftp://${FTP_HOST}:${FTP_PORT}/${FTP_REMOTE_DIR:+${FTP_REMOTE_DIR%/}/}$(basename "${BACKUP_FILE}")"
    printf 'user = "%s"\n' "${FTP_CREDENTIALS}" | curl --config - --ssl-reqd --fail --silent --show-error \
        --ftp-create-dirs --upload-file "${BACKUP_FILE}" "${FTP_URL}"
    echo "Uploaded:       ${FTP_URL}"
fi
