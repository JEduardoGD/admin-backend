# AGENTS.md

Instructions for agents working in this repository. Do not duplicate the README.

## Stack

- Spring Boot 4.1.0, Java 21, Maven wrapper (Apache Maven 3.9.16)
- Single-module Maven artifact `mx.egd.fmre:Register`, root package `mx.egd.fmre.register`
- MySQL 8.0 via Docker Compose (`compose.yaml`), JDBC driver `mysql-connector-java` 8.0.33
- OAuth2 JWT resource server (AWS Cognito)
- Lombok, MapStruct 1.6.3, springdoc-openapi 2.8.3, spring-boot-devtools

## Developer commands

```bash
docker compose up -d           # start MySQL (required before run)
./mvnw spring-boot:run         # dev server (devtools hot-restart on classpath changes)
./mvnw compile                 # regenerate MapStruct impls after mapper changes
./mvnw test                    # all tests (currently only RegisterApplicationTests context-loads)
./mvnw test -Dtest=FooTest     # a single test class
```

Default HTTP port is 8080. Swagger UI: `/swagger-ui.html`. OpenAPI: `/v3/api-docs`.

## Environment

- `.env` at the repo root is gitignored and required at runtime.
- Loaded via `spring.config.import: "optional:file:.env[.properties]"` — no copies needed.
- Keys: `DB_ROOT_PASSWORD`, `DB_NAME`, `DB_USERNAME`, `DB_PASSWORD`, `DB_URL`, `AUTH_AUTHORITY`, `FRONT_URL`.
- CORS origin is only `FRONT_URL` (bound as `spring.frontUrl`). Never hardcode origins; inject `@Value("${spring.frontUrl}")`.

## Layering

Controller → service interface → `service.impl` → Spring Data repository + mapper. Do not put persistence or mapping logic in controllers.

```
src/main/java/mx/egd/fmre/register/
├── config/                 # SecurityConfig (JWT + CORS)
├── controller/             # REST, one class per resource
├── dto/                    # Lombok request/response DTOs (not JPA)
├── dto/datatable/          # DataTables protocol (QueryObj, DataTableResponse, DatatableObj)
├── mapper/                 # PersonaMapper (manual static methods)
├── mapper/to_dto/          # MapStruct entity → DTO/record
├── mapper/to_entity/       # MapStruct DTO → entity
├── persistence/entity/     # JPA entities
├── persistence/repository/ # Spring Data JPA
├── record/                 # Java records (TipoImagen, UserInfo, OpenIdConfiguration)
├── service/ + service/impl/
└── util/
```

## Security

`SecurityConfig` permits unauthenticated access only to:

- `/api/public/**` (none exist yet — put skip-auth endpoints here)
- `/v3/api-docs/**`, `/swagger-ui/**`, `/swagger-ui.html`
- `/static_catalog/**`

Everything else requires a Cognito JWT (`oauth2ResourceServer.jwt`). CSRF is disabled. CORS allows GET/POST/PUT/DELETE/OPTIONS with `Authorization`, `Cache-Control`, `Content-Type`.

There is no `/api` prefix on existing controllers. Paths are resource names at the root (`/persona`, `/domicilio`, `/sumary`, `/static_catalog/...`).

## HTTP surface

| Method | Path | Auth | Notes |
| ------ | ---- | ---- | ----- |
| POST | `/persona` | JWT | Create; service nulls `idPersona` |
| POST | `/persona/update` | JWT | Update |
| GET | `/persona` | JWT | Search by `nombre`, `primerApellido`, `segundoApellido`, `fecnac` |
| GET | `/persona/{idPersona}` | JWT | By id |
| POST | `/domicilio` | JWT | Create; controller nulls `idDomicilio`. Reads `Authorization` header (unused today) |
| POST | `/domicilio/update` | JWT | Update; returns `null` if `idDomicilio` is missing |
| GET | `/domicilio/find_by/idpersona/{idPersona}` | JWT | List by person |
| POST | `/sumary` | JWT | DataTables listing of personas (`QueryObj` in, `DataTableResponse` out) |
| GET | `/static_catalog/tipo_imagen` | public | Active image-document types |

`UserinfoService` can resolve an email from a bearer token via Cognito `userinfo_endpoint`. It is not wired into controllers yet.

## Persistence

- Hibernate `PhysicalNamingStrategyStandardImpl` — `@Column` / `@Table` names are taken literally (uppercase, no snake_case conversion). Match the MySQL identifiers.
- Schema is **not** auto-created (`spring.jpa.hibernate.ddl-auto` is unset). Apply `db/db_register.sql` to the MySQL from Compose.
- Catalog seed: `db/C_TIPOIMAGENDOCUMENTO_*.sql`. Workbench model: `db/BDAFILIACION.mwb`.
- Table naming: `C_*` catalogs, `T_*` transactional. Schema name `db_register`.
- Entities cover `T_PERSONA`, `T_DOMICILIO`, `T_IMAGEN`, `C_TIPOIMAGENDOCUMENTO`. SQL also defines `T_DATOCONTACTO`, `C_TIPODATOCONTACTO`, `T_AFILIACION`, `T_RADIOAFICIONADO`, `T_ASPIRANTE` with **no** JPA mapping yet.
- IDs: `T_PERSONA` / `T_DOMICILIO` / `T_IMAGEN` use `IDENTITY`. `C_TIPOIMAGENDOCUMENTO` PK is not auto-increment in SQL; the entity currently uses `GenerationType.TABLE`.
- `T_IMAGEN` (`ImagenEntity`) has no repository, service, or controller yet.

## Mapper conventions

Prefer MapStruct for new mappings.

- **MapStruct**: interfaces in `mapper/to_dto` or `mapper/to_entity`, `INSTANCE = Mappers.getMapper(...)`, `@Mapping` for nested ids (e.g. `persona.idPersona` ↔ `idPersona`). After editing a mapper interface, run `./mvnw compile` to regenerate `*Impl` under `target/generated-sources/annotations`.
- **Manual** (legacy): `PersonaMapper` abstract class with static methods. Do not extend this style.

`maven-compiler-plugin` lists only `mapstruct-processor` in `annotationProcessorPaths`. If you add processors there, include Lombok as well (`lombok` before `mapstruct-processor`) or Maven compile will skip Lombok.

## Code conventions

- Constructor injection via Lombok `@RequiredArgsConstructor` / `@AllArgsConstructor` on services and controllers.
- DTOs stay out of the JPA package. API records (`TipoImagen`) are fine for read-only catalog payloads.
- Domain names and DB columns are Spanish (`primerApellido`, `entidadFederativa`, `fecNac`). Keep that vocabulary; do not rename to English.
- New skip-auth endpoints go under `/api/public/**` **or** extend the matcher list in `SecurityConfig` the same way `/static_catalog/**` was added.
- `DatatableRepository` is an empty unused class; listing goes through `PersonaRepository.searchByTerm` and `DatatableServiceImpl`.
- Tests live under `src/test/java/mx/egd/fmre/register`. There is no testcontainers / security test setup yet; `RegisterApplicationTests` needs a running MySQL and a valid `.env`.
