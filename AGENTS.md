# AGENTS.md

Instructions for agents working in this repository. Do not duplicate the README.

## Stack

- Spring Boot 4.1.0, Java 21, Maven wrapper (Apache Maven 3.9.16)
- Single-module Maven artifact `mx.egd.fmre:Register`, root package `mx.egd.fmre.register`
- MySQL 8.0 via Docker Compose (`compose.yaml`), JDBC driver `mysql-connector-java` 8.0.33
- OAuth2 JWT resource server (AWS Cognito) — still wired; see Security
- Lombok, MapStruct 1.6.3, springdoc-openapi 2.8.3, spring-boot-devtools
- Apache Tika 3.0.0 (MIME detection), Thumbnailator 0.4.21 (JPEG thumbnails)
- `spring-boot-starter-restclient` for the Postalia postal-code API

## Developer commands

```bash
docker compose up -d           # start MySQL (required before run)
./mvnw spring-boot:run         # dev server (devtools hot-restart on classpath changes)
./mvnw compile                 # regenerate MapStruct impls after mapper changes
./mvnw test                    # all tests (currently only RegisterApplicationTests context-loads)
./mvnw test -Dtest=FooTest     # a single test class
```

Default HTTP port is 8080. Swagger UI: `/swagger-ui.html`. OpenAPI: `/v3/api-docs`.

Multipart uploads are capped at 15MB (`spring.servlet.multipart` + Tomcat `max-swallow-size: -1`). `GlobalExceptionHandler` maps `MaxUploadSizeExceededException` to HTTP 413.

## Environment

- `.env` at the repo root is gitignored and required at runtime.
- Loaded via `spring.config.import: "optional:file:.env[.properties]"` — no copies needed.
- Keys: `DB_ROOT_PASSWORD`, `DB_NAME`, `DB_USERNAME`, `DB_PASSWORD`, `DB_URL`, `AUTH_AUTHORITY`, `FRONT_URL`, `FILES_LOCATION`, `UPLOAD_PATH`, `POSTALIA_API`.
- CORS origin is only `FRONT_URL` (bound as `spring.frontUrl`). Never hardcode origins; inject `@Value("${spring.frontUrl}")`.
- File store is `FILES_LOCATION` + `UPLOAD_PATH` (bound as `spring.files.location` / `spring.files.upload_path`). Uploads are stored as UUID filenames; do not hardcode a local upload directory.
- `POSTALIA_API` is a bearer token for `https://postalia.com.mx` (`PostaliaRestClientConfig`). Do not change the base URL without checking that config.
- If `java -version` fails or `JAVA_HOME` is unset, do not assume Java is missing. Ask the user for the JDK location (this project needs JDK 21) and `export JAVA_HOME=<that path>` before running `./mvnw`.

## Layering

Controller → service interface → `service.impl` → Spring Data repository + mapper. Do not put persistence or mapping logic in controllers.

Domain validation that is not persistence belongs in `component/` + `component/impl` (example: `AfiliacionValidatorComponent`). Services call the component and throw `service.exceptions.*`.

```
src/main/java/mx/egd/fmre/register/
├── component/ + component/impl/   # domain validators (not Spring Data)
├── config/                        # SecurityConfig (JWT + CORS), PostaliaRestClientConfig
├── controller/                    # REST, one class per resource; GlobalExceptionHandler
├── dto/                           # Lombok request/response DTOs (not JPA)
├── dto/datatable/                 # DataTables protocol (QueryObj, DataTableResponse, DatatableObj)
├── dto/postalia/                  # Postalia JSON (Localizacion, Colonia)
├── exception/                     # storage / image / generic web exceptions
├── mapper/                        # PersonaMapper (manual); EstadoMapper (MapStruct)
├── mapper/to_dto/                 # MapStruct entity → DTO/record
├── mapper/to_entity/              # MapStruct DTO → entity
├── persistence/entity/            # JPA entities
├── persistence/repository/        # Spring Data JPA
├── record/                        # TipoImagen, UserInfo, OpenIdConfiguration, UploadResult
├── service/ + service/impl/
├── service/exceptions/            # AddressServiceException, AfiliacionServiceException, ServiceException
└── util/                          # DateTimeUtil, MimeTypesUtil, StaticValues, DistinctByKey
```

## Security

`SecurityConfig` currently `permitAll`s `/**` (JWT resource server is still configured). CSRF is disabled. CORS allows GET/POST/PUT/DELETE/OPTIONS with `Authorization`, `Cache-Control`, `Content-Type`, and `allowCredentials`.

Intended public prefixes (commented matchers in `SecurityConfig`): `/api/public/**`, swagger, `/static_catalog/**`, `/imagen/**`, `/file/**`. There is no `/api` prefix on existing controllers. Paths are resource names at the root (`/persona`, `/domicilio`, `/afiliacion`, `/sumary`, `/static_catalog/...`, `/file`, `/imagen`, `/address`).

New skip-auth endpoints go under `/api/public/**` **or** extend the matcher list in `SecurityConfig` the same way `/static_catalog/**` was added. Do not rely on the current `/**` permitAll remaining.

## HTTP surface

| Method | Path | Notes |
| ------ | ---- | ----- |
| POST | `/persona` | Create; service nulls `idPersona` |
| POST | `/persona/update` | Update |
| GET | `/persona` | Search by `nombre`, `primerApellido`, `segundoApellido`, `fecnac` |
| GET | `/persona/{idPersona}` | By id |
| POST | `/domicilio` | Create; controller nulls `idDomicilio`. Reads `Authorization` header (unused today) |
| POST | `/domicilio/update` | Update; returns `null` if `idDomicilio` is missing |
| GET | `/domicilio/find_by/idpersona/{idPersona}` | List by person |
| POST | `/afiliacion` | Create; controller nulls `idAfiliacion` |
| PUT | `/afiliacion` | Update; 400 if `idAfiliacion` is missing or `<= 0` |
| GET | `/afiliacion/find_by/id_afiliacion/{idAfiliacion}` | By id |
| GET | `/afiliacion/find_by/id_persona/{idPersona}` | List by person; 400 if `idPersona <= 0` |
| POST | `/imagen` | Create; controller nulls `idImagen`. Metadata only — file bytes go through `/file` |
| POST | `/imagen/update` | Update; returns `null` if `idImagen` is missing |
| GET | `/imagen/find_by/idpersona/{idPersona}` | List by person |
| GET | `/imagen/find_by/id/{id}` | By id |
| GET | `/imagen/thumbnail/{uuid}` | JPEG thumbnail (128px) of the stored file |
| POST | `/file` | Multipart upload; returns `UploadResult` (`filename` is a UUID) |
| GET | `/file/files/{filename}` | Download stored file; `Content-Type` from Tika |
| GET | `/address/by_cp/{cp}` | Postalia lookup → `Localizacion` (colonias). Maps 401/404/429 from the remote API |
| POST | `/sumary` | DataTables listing of personas (`QueryObj` in, `DataTableResponse` out) |
| GET | `/static_catalog/tipo_imagen` | Active image-document types |
| GET | `/static_catalog/tipo_imagen/for_persona` | Types for persona (`StaticValues.FOR_PERSONA`) |
| GET | `/static_catalog/tipo_imagen/for_afiliacion` | Types for afiliación (`StaticValues.FOR_AFILIACION`) |
| GET | `/static_catalog/estado` | All Mexican states (`C_ESTADO`) |
| GET | `/static_catalog/estado/{idEstado}` | One state by id |

`UserinfoService` can resolve an email from a bearer token via Cognito `userinfo_endpoint`. It is not wired into controllers yet.

## Persistence

- Hibernate `PhysicalNamingStrategyStandardImpl` — `@Column` / `@Table` names are taken literally (uppercase, no snake_case conversion). Match the MySQL identifiers.
- Schema is **not** auto-created (`spring.jpa.hibernate.ddl-auto` is unset). Apply `db/db_register.sql` to the MySQL from Compose.
- Catalog seed: `db/C_TIPOIMAGENDOCUMENTO_*.sql`, `db/C_ESTADO_*.sql`. Workbench model: `db/BDAFILIACION.mwb`.
- Table naming: `C_*` catalogs, `T_*` transactional. Schema name `db_register`.
- Mapped entities: `T_PERSONA`, `T_DOMICILIO`, `T_IMAGEN`, `T_AFILIACION`, `C_TIPOIMAGENDOCUMENTO`, `C_ESTADO`.
- SQL also defines `T_DATOCONTACTO`, `C_TIPODATOCONTACTO`, `T_RADIOAFICIONADO`, `T_ASPIRANTE` with **no** JPA mapping yet.
- IDs: `T_PERSONA` / `T_DOMICILIO` / `T_IMAGEN` / `T_AFILIACION` use `IDENTITY`. `C_TIPOIMAGENDOCUMENTO` PK is not auto-increment in SQL; the entity currently uses `GenerationType.TABLE`. `C_ESTADO` PK is not auto-increment in SQL (seeded 1–32); the entity currently uses `GenerationType.IDENTITY` — treat the catalog as seed-only, do not insert from the app.
- `T_IMAGEN` links optionally to `T_PERSONA` and/or `T_AFILIACION`, plus required `C_TIPOIMAGENDOCUMENTO`. File bytes live on disk keyed by `UUID`, not in the row.
- `T_AFILIACION` requires `IDPERSONA` and `IDESTADO`. `VITALICIA` / `DELETED` are `TINYINT`. `MODIFIED_AT` is set in `@PrePersist` / `@PreUpdate`.

## Afiliación rules

`AfiliacionServiceImpl` runs `AfiliacionValidatorComponent` before save. Failures become `AfiliacionServiceException` (controller maps to HTTP 500 with the message body).

- `fechaInicio` is required.
- `fechaFin` may be null only when `vitalicia` is true; otherwise `fechaInicio` must be strictly before `fechaFin`.
- A person may not have a second non-deleted vitalicia.
- New/edited dates must not sit before existing non-deleted afiliación start/end dates (excluding the row being edited).
- Edits are rejected more than 5 days after `MODIFIED_AT`.

Keep those checks in the component; do not copy them into the controller.

## Mapper conventions

Prefer MapStruct for new mappings.

- **MapStruct**: interfaces in `mapper/to_dto` or `mapper/to_entity` (exception: `EstadoMapper` lives in `mapper/` next to the legacy persona mapper). `INSTANCE = Mappers.getMapper(...)`, `@Mapping` for nested ids (e.g. `persona.idPersona` ↔ `idPersona`, `estado.idEstado` ↔ `idEstado`). After editing a mapper interface, run `./mvnw compile` to regenerate `*Impl` under `target/generated-sources/annotations`.
- **Manual** (legacy): `PersonaMapper` abstract class with static methods. Do not extend this style.

`maven-compiler-plugin` `annotationProcessorPaths` lists **lombok then** `mapstruct-processor`. Keep that order. If you add processors, include Lombok as well (`lombok` before `mapstruct-processor`) or Maven compile will skip Lombok.

## Code conventions

- Constructor injection via Lombok `@RequiredArgsConstructor` / `@AllArgsConstructor` on services and controllers.
- DTOs stay out of the JPA package. API records (`TipoImagen`, `UploadResult`) are fine for read-only / simple payloads.
- Domain names and DB columns are Spanish (`primerApellido`, `entidadFederativa`, `fecNac`, `idEstado`). Keep that vocabulary; do not rename to English. `estado` here is a Mexican federative entity (`C_ESTADO`), not a workflow status.
- Image-type groups live in `StaticValues` (`FOR_PERSONA` / `FOR_AFILIACION` and the `PERSONAL_FOTO`…`SOLICITUD` ids). Reuse those constants; do not scatter magic numbers.
- Listing goes through `PersonaRepository.searchByTerm` and `DatatableServiceImpl`.
- Tests live under `src/test/java/mx/egd/fmre/register`. There is no testcontainers / security test setup yet; `RegisterApplicationTests` needs a running MySQL and a valid `.env`.
