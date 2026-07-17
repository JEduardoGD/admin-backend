# AGENTS.md

## Stack

- Spring Boot 4.1.0, Java 21, Maven wrapper (Apache Maven 3.9.16)
- MySQL 8.0 (Docker Compose), OAuth2 JWT resource server (Cognito)
- Lombok, springdoc-openapi (Swagger), spring-boot-devtools

## Developer commands

```bash
docker compose up -d          # start MySQL (required before run)
./mvnw spring-boot:run        # dev server (devtools hot-restart on classpath changes)
./mvnw test                   # run all tests
./mvnw test -Dtest=FooTest    # run a single test class
```

## Environment

- `.env` at the repo root supplies all runtime config (gitignored, required).
- Loaded via `spring.config.import: "optional:file:.env[.properties]"` — no copies needed.
- CORS allows only the origin in `FRONT_URL`.

## Architecture

- Single-module Maven project, root package `mx.egd.fmre.register`.
- Security: all endpoints except `/api/public/**`, `/v3/api-docs/**`, and `/swagger-ui/**` require JWT auth (`SecurityConfig.java`).
- Swagger UI at `/swagger-ui.html`, API docs at `/v3/api-docs`.
- Controllers currently return stub data — no repository/service layer yet.
- `Persona` is a Lombok DTO (`@Data + @AllArgsConstructor`), not a JPA entity (no `@Entity`).
- IDE: enable Lombok annotation processing in your IDE settings.

## Conventions

- Follow existing Spring Boot conventions — `@Configuration`, `@RestController`, `@Service`, etc.
- Add new endpoints under `/api/public/**` if they must skip auth.
- Use `spring.frontUrl` property (via `@Value`) for CORS origins, not hardcoded URLs.
