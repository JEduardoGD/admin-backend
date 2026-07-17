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
