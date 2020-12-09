# CodeSupport Website Backend

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/448f0a0fbf14480ca9735246d3ade25f)](https://app.codacy.com/gh/codesupport/website-backend?utm_source=github.com&utm_medium=referral&utm_content=codesupport/website-backend&utm_campaign=Badge_Grade_Settings)

## About
This repository contains the code for the backend of the CodeSupport website. The project is written in Java using the Spring framework. 

Documentation is powered by Swagger and can be reached at `/swagger-ui.html` when running the application locally.

## Dependencies
Please see [pom.xml](https://github.com/codesupport/website-backend/blob/develop/pom.xml) for a list of dependencies.

## Setup
1. Navigate into the repository on your computer
2. On Unix systems run `./mnvw clean package` and on Windows run `./mvnw.cmd clean package`
3. Run `java -jar target/api-service-0.0.1-SNAPSHOT.jar`

## Structure
- Domain is the business model and handles business related validation
- Controller defines the API contract and enforces it with validation
- ControllerImpl translates the HTTP requests to service calls and handles simple parameter transformations prior to service calls
- Service is the business logic, the bulk of logic in the request and gathering data happens here
- Repository is the DAL (data access layer) and handles communication with the database
- Entity is the database contract definition and validation enforcement of that contract

## Conventions
### Resource
This is a consumable offered via API interaction. The naming for an example resource:
- `User` - the domain model
- `api/v1/users/{ID}` - the API endpoint (plural)
- `UserController` - the API contract
- `UserControllerImpl` - the API contract implementation
- `UserService` - the business logic
- `UserRepository` - the data access
- `UserEntity` - the database contract
- `UserValidation` - the persistence level validation

### Exceptions
Any business logic exceptions throw `ServiceLayerExceptio`n in the service class. Validation and Resource exceptions are handled by the framework if utilizing provided APIs. All exceptions are caught and handled by the framework to provide feedback to the user and log to the system.

### Validations
#### Domain Level (Business Validations)
Domain level validations are done on the domain models via implementing the `Validatable` interface. This is for business validations such as required fields and field checks.

#### Persistence Level (Database Data)
Persistence level validations are done via validation classes that extend the `AbstractPersistenceValidation` class and are used for validation checks against the DB, such as unique column value checks.

#### Entity Validations (Database Contracts)
Validations on the entities are only to protect the integrity of the database and enforce the DB contracts. No business logic validations are done here (meaning no messages back to the user).

#### API Validations (API Contracts)
These validations are done on the API interfaces, and are used as basic API contract enforcements, done via the constraint annotations on the API interfaces. API implementations contain no validation checks.

### CRUD Operations
Basic crud requirements are fulfilled (with preset mechanics) by utilizing the `CrudOperation`s class:
- `getAll()` gets all avalaible entities
- `getById()` gets by a specific id
  - Throws `ResourceNotFoundException`, resulting in 404, if not found
- `createEntities()` saves entities to the database
  - Throws `Exception` if already exists by id, performs validation checks (domain and persistence level)
- `updateEntities()` updates entities in the database
  - Throws `ResourceNotFoundException`, resulting in 404, if not found, performs validation checks (domain and persistence level)
- `deleteEntities()` deletes entities from the database
  - Throws `ResourceNotFoundException`, resulting in 404, if not found

## Tests
We are using [JUnit](https://junit.org/junit4/) for our tests. **All code should be tested**.

## Scripts
- To delete the `target/` build folder use `./mvnw clean`
- To compile `.class` files, run tests and package into `.jar` use `./mvnw package`
- To compile, package and install to a local m2 repo use `./mvnw install`
- To run unit tests use `./mnvw test`
- To delete the `target/` build folder and repackage use `./mvnw clean package` (this is the prefered way to build)
- To run the application use `java -jar target/api-service-0.0.1-SNAPSHOT.jar`

## Environment Variables
Name | Default | Description
---|---|---
`DATABASE_URL` |  | The URL to the database
`DATABASE_USERNAME` | | The username for the database
`DATABASE_PASSWORD` | | The password for the database
`DATABASE_DRIVER` | `com.mysql.jdbc.Driver` | The driver for the database
`DATABASE_DIALECT` | `org.hibernate.dialect.MySQLDialect ` | The dialect for the database
`DATABASE_POOL_SIZE` | `4` | The maximum number of database connections
`DATABASE_POOL_IDLE` | `2` | The minimum number of database connections to keep open
`DISCORD_APP_ID` | | The Discord app's ID
`DISCORD_APP_SECRET` | | The Discord app's secret
`DISCORD_APP_REDIRECT` | | The Discord app's redirect for OAuth
`HEALTHCHECK_PING_DELAY` | `5000` | Delay between healthchecks (ms)
`HEALTHCHECK_PING_URL` | | Url of the external healthcheck api to hit
`JWT_ISSUER` | `codesupport.dev` | The JWT issuer
`JWT_EXPIRATION` | `10m` | The length of time a JWT lasts
`LOG_ROOT_LEVEL` | `INFO` | Root logging level for spring logs
`SERVICE_PORT` | `8080` | The port to run the application on
`SSL_KEY_ALIAS` | `tomcat` | Alias for certificate, used with openssl step
`SSL_KEY_STORE` | | Path to the keystore (/etc/.../keystore.p12)
`SSL_KEY_STORE_PASSWORD` | | Password to access keystore
`SSL_KEY_STORE_TYPE` | `PKCS12` | Format for keystore (Spring likes PKCS12)

**Any Questions?** Feel free to mention @LamboCreeper#6510 in the [CodeSupport Discord](https://discord.gg/Hn9SETt).
