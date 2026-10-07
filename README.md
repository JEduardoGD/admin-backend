# Register

REST API for managing personas, built with Spring Boot. Secured as an OAuth2 JWT resource server (AWS Cognito) and backed by MySQL.

## Stack

- Java 21, Spring Boot 4.1.0, Maven wrapper
- MySQL 8.0 (Docker Compose)
- Spring Security OAuth2 resource server (JWT)
- Lombok, springdoc-openapi (Swagger UI)

## Prerequisites

- Java 21
- Docker (for MySQL)
- A `.env` file at the repo root (gitignored, required)

### Environment variables (`.env`)

```properties
DB_ROOT_PASSWORD=...
DB_NAME=...
DB_USERNAME=...
DB_PASSWORD=...
DB_URL=jdbc:mysql://localhost:3306/<DB_NAME>
AUTH_AUTHORITY=https://cognito-idp.<region>.amazonaws.com/<user-pool-id>
FRONT_URL=http://localhost:5173
```

The `.env` file is loaded automatically via `spring.config.import` — no copies needed. CORS allows only the origin in `FRONT_URL`.

### FTP file backup

Copy `db/.env.example` to `db/.env` and set the FTP connection and file backup paths (independent of the application’s root `.env`):

```bash
FTP_HOST='ftp.example.com'
FTP_PORT='21'
FTP_USER='your_ftp_user'
FTP_PASSWORD='your_ftp_password'
FTP_BACKUP_SOURCE_DIR='/home/geduardo/adminstracion files/upload files'
FTP_BACKUP_REMOTE_DIR='/xe1jeg/filebackup/fmreadmin_dev'
```

Run `bash db/backup_files_ftp.sh` from the repository root (or use its absolute path from any directory; requires `curl`). Each run uploads regular files directly in `FTP_BACKUP_SOURCE_DIR`, including hidden files, via explicit FTPS to `FTP_BACKUP_REMOTE_DIR/YYMMDD_hhmmss` on the FTP server. Local subdirectories and symlinks are skipped. Use `-s SOURCE_DIR` and `-r REMOTE_DIR` to override the paths for a single run; `-h` shows usage. An empty or missing source directory and upload errors cause a nonzero exit status.

## Running

```bash
docker compose up -d          # start MySQL (required before run)
./mvnw spring-boot:run        # dev server (devtools hot-restart enabled)
```

## Testing

```bash
./mvnw test                   # run all tests
./mvnw test -Dtest=FooTest    # run a single test class
```

## API

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI docs: `http://localhost:8080/v3/api-docs`

| Method | Path       | Description                                  |
| ------ | ---------- | -------------------------------------------- |
| POST   | `/persona` | Create a persona                             |
| GET    | `/persona` | Search personas by nombre/apellidos/fecnac   |

All endpoints require a JWT bearer token, except `/api/public/**`, `/v3/api-docs/**`, and `/swagger-ui/**`.

## Project structure

```
src/main/java/mx/egd/fmre/register/
├── RegisterApplication.java
├── config/SecurityConfig.java      # JWT auth + CORS
├── controller/PersonaController.java
└── dto/Persona.java                # Lombok DTO (not a JPA entity)
```

Controllers currently return stub data — no repository/service layer yet.
