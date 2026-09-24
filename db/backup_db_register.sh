#!/usr/bin/env bash
#
# Backup of the db_register MySQL database running on the local host.
# Usage: ./backup_db_register.sh [-H HOST] [-P PORT] [-u USER] [-p PASSWORD]
#                                [-d DBNAME] [-z] [-o OUTPUT_DIR] [-h]
#   -z  compress the resulting .sql file into a .zip and remove the plain file
#   -o  directory for the backup (default: ./backups relative to this script)
#
set -euo pipefail

# ---------------------------------------------------------------------------
# Connection defaults (override from the command line)
# ---------------------------------------------------------------------------
DB_HOST="127.0.0.1"
DB_PORT="3306"
DB_USER="db_user"
DB_PASSWORD=""
DB_NAME="db_name"

# ---------------------------------------------------------------------------
# Defaults / option parsing
# ---------------------------------------------------------------------------
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
OUTPUT_DIR="${SCRIPT_DIR}/backups"
ZIP=false

usage() {
    echo "Usage: $(basename "$0") [-H HOST] [-P PORT] [-u USER] [-p PASSWORD] [-d DBNAME] [-z] [-o OUTPUT_DIR] [-h]"
    echo "  -H HOST         database host (default: ${DB_HOST})"
    echo "  -P PORT         database port (default: ${DB_PORT})"
    echo "  -u USER         database user (default: ${DB_USER})"
    echo "  -p PASSWORD     database password (default: empty)"
    echo "  -d DBNAME       database name (default: ${DB_NAME})"
    echo "  -z              zip the result (removes the plain .sql after zipping)"
    echo "  -o OUTPUT_DIR   output directory (default: ./backups)"
    echo "  -h              show this help"
    exit 0
}

while getopts ":H:P:u:p:d:zo:h" opt; do
    case "${opt}" in
        H) DB_HOST="${OPTARG}" ;;
        P) DB_PORT="${OPTARG}" ;;
        u) DB_USER="${OPTARG}" ;;
        p) DB_PASSWORD="${OPTARG}" ;;
        d) DB_NAME="${OPTARG}" ;;
        z) ZIP=true ;;
        o) OUTPUT_DIR="${OPTARG}" ;;
        h) usage ;;
        \?) echo "ERROR: unknown option -${OPTARG}" >&2; usage ;;
        :)  echo "ERROR: option -${OPTARG} requires an argument" >&2; usage ;;
    esac
done

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

# ---------------------------------------------------------------------------
# Optional zip
# ---------------------------------------------------------------------------
if [[ "${ZIP}" == true ]]; then
    command -v zip >/dev/null 2>&1 || { echo "ERROR: zip not found in PATH" >&2; exit 1; }
    (cd "${OUTPUT_DIR}" && zip -q "${ZIP_FILE}" "$(basename "${SQL_FILE}")")
    rm -f "${SQL_FILE}"
    echo "Compressed:     ${ZIP_FILE}"
fi
