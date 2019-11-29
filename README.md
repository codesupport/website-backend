# CodeSupport Website Backend

## Maven commands

``./mvnw clean`` - Deletes `target/` folder (build folder)

``./mvnw package`` - Compiles .class files, runs tess, and packages into jar (``./mvn compile`` just compiles)

``./mvnw install`` - Compiles, packages, and installs to local m2 repo (``<user>/.m2/repository``)

``./mvnw test`` - Runs unit tests

``./mvn clean package`` - Deletes `target/` folder and repackages (preferred way to build)


## Usage
### Build the App
(From api-service root folder)
#### Unix

``./mvnw clean package``

#### Windows

``./mvnw.cmd clean package``


### Run the App

(From app-service root dir)

``java -jar target/api-service-0.0.1-SNAPSHOT.jar``

### Hit the endpoints

#### Get user by ID

``http://localhost:8082/api/v1/users/2``

#### Get all users

``http://localhost:8082/api/v1/users``


## Structure

#### Layer Separation
- Domain - Business model, handles business related validation (required fields, etc.)
- Controller - Defines the API contract and enforces it with validations.
- ControllerImpl - Translates HTTP requests to service calls, and can rarely help with
supplemental API Contract validations, but no business logic.  Handles simple parameter transformations prior to
Service calls (i.e. parsing delimited strings to lists)
- Service - Business logic, handles bulk of logic in the request, gathering all data needed
for the response.
- Repository - DAL (Data access layer), handles communication with DB
- Entity - DB Contract definition, and validation enforcement of that contract

### Response

All responses wrapped in `RestResponse` providing basic feedback from request.

- Status (OK, FAIL, WARNING, NOT_FOUND)
- Message (error/warning message if required)
- ReferenceId (ReferenceID to use for troubleshooting/logging)
- Response (The requested Resource)

### Packaging

    dev.codesupport.web.api*

Resource logic
- Controllers (rest endpoints)
- Service Classes
- Entities
- Repositories
- Validation rules


    dev.codesupport.web.common.*

Framework
- Common utilities
- Error/Exception Handling
- CRUD framework


    dev.codesupport.web.domain.*

Rich domain models (Business models) used for API interactions and business validations

### Validations

#### Domain level (Business Validations)
Domain level validations are done on the domain models via implementing the `Validatable` interface.
This is for business validations such as required fields and field checks.

#### Persistence level (DB data)
Persistence level validations are done via validation classes that extend the `AbstractPersistenceValidation`
class, and are used for validation checks against the DB, such as unique column value checks.

#### Entity Validations (DB Contracts)
Validations on the entities are only to protect the integrity of the database and enforce the DB contracts.
No business logic validations done here (meaning no messages back to the user).

#### API Validations (Api Contracts)
These validations are done on the API interfaces, and are used as basic API contract enforcements,
done via the constraint annotations on the API interfaces.  API implementations contain no validation checks.

### CRUD operations
Basic crud requirements are fulfilled (with preset mechanics) by utilizing the `CrudOperations` class.
```
getAll() - get all available entities
getById() - get by a specific id, throws `ResourceNotFoundException`, resulting in 404, if not found
createEntities() - saves entities to db, throws Exception if already exists by id, performs validation checks (domain & persistence level)
updateEntities() - updates entities in db, throws `ResourceNotFoundException`, resulting in 404, if not found, performs validation checks (domain & persistence level)
deleteEntities() - deletes entities from db, throws `ResourceNotFoundException`, resulting in 404, if not found
```

### Conventions

#### API
##### Http methods
- GET - Get resource
- POST - Create resource
- PUT - Update resource
- DELETE - Delete resource

##### Resource
A consumable offered via API interaction

Naming for example resource: User
- User - Domain model (Business model)
- api/v1/users/{id} - Api uri (plural)
- UserController - API Contract (interface)
- UserControllerImpl - Api Contract implementation
- UserService - Service class (business logic)
- UserRepository - Data Access Layer
- UserEntity - DB Contract
- UserValidation - Persistence level validation

##### Exceptions
Any business logic exceptions throw `ServiceLayerException` in the service class.
Validation and Resource exceptions are handled by the framework if utilizing provided APIs.
All exceptions are caught and handled by the framework to provide feedback to the user and log
to the system.

**Any Questions?** Feel free to mention @LamboCreeper#6510 in the [CodeSupport Discord](https://discord.gg/Hn9SETt).