# AGENTS.md

## Stack

- Spring Boot 4.1.0, Java 21, Maven wrapper (Apache Maven 3.9.16)
- MySQL 8.0 (Docker Compose), OAuth2 JWT resource server (Cognito)
- Lombok, MapStruct 1.6.3, springdoc-openapi (Swagger), spring-boot-devtools

## Developer commands

```bash
docker compose up -d           # start MySQL (required before run)
./mvnw spring-boot:run         # dev server (devtools hot-restart on classpath changes)
./mvnw compile                 # regenerate MapStruct impls after mapper changes
./mvnw test                    # run all tests (currently only a context-loads smoke test)
./mvnw test -Dtest=FooTest     # run a single test class
```

## Environment

- `.env` at the repo root supplies all runtime config (gitignored, required).
- Loaded via `spring.config.import: "optional:file:.env[.properties]"` — no copies needed.
- CORS allows only the origin in `FRONT_URL` (exposed as `spring.frontUrl`).

## Architecture

- Single-module Maven project, root package `mx.egd.fmre.register`.
- Security: all endpoints except `/api/public/**`, `/v3/api-docs/**`, and `/swagger-ui/**` require JWT auth (`SecurityConfig.java`). Endpoints at `/persona` and `/domicilio` are secured.
- Swagger UI at `/swagger-ui.html`, API docs at `/v3/api-docs`.
- DTOs (`Persona`, `Domicilio`) are separate from JPA entities (`PersonaEntity`, `DomicilioEntity`).
- Hibernate `PhysicalNamingStrategyStandardImpl` — column names in `@Column` are taken literally (uppercase, no automatic snake_case conversion).
- MapStruct annotation processor is in `maven-compiler-plugin` — mapper interfaces (`@Mapper`) generate implementations at compile time.

## Mapper conventions

Two styles coexist; prefer MapStruct for new code:

- **MapStruct** (preferred): `DomicilioMapper.INSTANCE`, `DomicilioEntityMapper.INSTANCE` — `@Mapper` interfaces with `@Mapping` annotations.
- **Manual**: `PersonaMapper` — abstract class with hand-written static mapping methods.

## Conventions

- Add new endpoints under `/api/public/**` if they must skip auth.
- Use `spring.frontUrl` property (`@Value`) for CORS origins, not hardcoded URLs.
- After editing a MapStruct mapper interface, run `./mvnw compile` to regenerate the impl.
