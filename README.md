# Employee Directory REST API

Spring Boot REST API for managing employee records with role-based access control, request validation, consistent error responses, and practical integration tests.

This project was originally built as a CRUD API and later refactored to be more portfolio-ready by improving REST design, security consistency, configuration hygiene, testability, and documentation.

## What This Project Demonstrates

- RESTful CRUD operations for employee management
- Role-based authorization with Spring Security and HTTP Basic auth
- Request validation and consistent JSON error handling
- Persistence with Spring Data JPA
- MySQL-ready configuration for default runtime usage
- H2-backed local and test profiles for quick execution and reliable automated tests
- Integration testing of happy paths, validation failures, authorization rules, and edge cases

## Tech Stack

- Java 17
- Spring Boot 3
- Spring Web
- Spring Data JPA
- Spring Security
- Maven Wrapper
- MySQL
- H2 Database
- JUnit 5
- MockMvc

## Architecture Overview

The application follows a simple layered structure:

- `rest`: controller layer exposing the API contract
- `service`: business logic and orchestration
- `dao`: JPA repository access
- `entity`: persistence model
- `dto`: request and response objects used by the API
- `exception`: centralized REST exception handling

This is intentionally small and realistic for a portfolio CRUD API rather than an overengineered enterprise sample.

## Authentication and Roles

The API uses HTTP Basic authentication with role-based authorization:

- `EMPLOYEE`: can read employee data
- `MANAGER`: can create and update employee data
- `ADMIN`: can delete employee data

Demo credentials for the `local` profile:

- `ramiro / examplepass` -> `EMPLOYEE`
- `matias / examplepass` -> `MANAGER`
- `alejo / examplepass` -> `ADMIN`

## Running the Project

### Option 1: Quick local run with H2

This is the easiest way to review the project without setting up MySQL.

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

The application starts on `http://localhost:8080`.

### Option 2: Run with MySQL

Set the datasource values through environment variables:

```bash
export DB_URL=jdbc:mysql://localhost:3306/employee_directory
export DB_USERNAME=springstudent
export DB_PASSWORD=springstudent
```

Then start the app:

```bash
./mvnw spring-boot:run
```

### MySQL Setup Scripts

Use the SQL scripts in [`mysqlscripts`](/Users/manuelmiguezz/Documents/Code/Projects/Java/CompanySpringBootRESTAPI/mysqlscripts):

- [`employee-directory.sql`](/Users/manuelmiguezz/Documents/Code/Projects/Java/CompanySpringBootRESTAPI/mysqlscripts/employee-directory.sql): employee table and seed data
- [`spring-security-users.sql`](/Users/manuelmiguezz/Documents/Code/Projects/Java/CompanySpringBootRESTAPI/mysqlscripts/spring-security-users.sql): quick-start auth seed with demo passwords
- [`spring-security-users-bcrypt.sql`](/Users/manuelmiguezz/Documents/Code/Projects/Java/CompanySpringBootRESTAPI/mysqlscripts/spring-security-users-bcrypt.sql): hashed-password variant

## API Endpoints

| Method | Endpoint | Role Required | Description |
| --- | --- | --- | --- |
| `GET` | `/api/employees` | `EMPLOYEE` | List all employees |
| `GET` | `/api/employees/{id}` | `EMPLOYEE` | Get one employee by id |
| `POST` | `/api/employees` | `MANAGER` | Create a new employee |
| `PUT` | `/api/employees/{id}` | `MANAGER` | Update an existing employee |
| `DELETE` | `/api/employees/{id}` | `ADMIN` | Delete an employee |

## Example Requests

### Get all employees

```bash
curl -u ramiro:examplepass http://localhost:8080/api/employees
```

### Create a new employee

```bash
curl -u matias:examplepass \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Laura",
    "lastName": "Gomez",
    "email": "laura.gomez@mail.com"
  }' \
  http://localhost:8080/api/employees
```

### Example success response

```json
{
  "id": 5,
  "firstName": "Laura",
  "lastName": "Gomez",
  "email": "laura.gomez@mail.com"
}
```

### Example validation error response

```json
{
  "type": "about:blank",
  "title": "Validation failed",
  "status": 400,
  "detail": "One or more request fields are invalid.",
  "timestamp": "2026-04-24T13:55:33.000000-03:00",
  "path": "/api/employees",
  "errors": {
    "firstName": "firstName is required",
    "email": "email must be a valid email address"
  }
}
```

## Testing

Run the automated test suite with:

```bash
./mvnw test
```

The current tests cover:

- application context startup
- authenticated read access
- role-based authorization
- successful employee creation
- validation failures
- duplicate email conflicts
- not-found behavior
- delete permissions and delete flow

## Verification Commands

Commands used during the review and refactor:

```bash
./mvnw test
./mvnw -q -DskipTests package
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

## Improvements Made During Refactor

- removed unused Spring Data REST setup
- externalized datasource configuration
- added a `local` H2 profile and a dedicated `test` profile
- introduced DTOs for API input and output
- added bean validation for employee requests
- improved REST status codes and endpoint consistency
- added centralized exception handling with structured JSON errors
- aligned security roles with the seeded data
- added integration tests for API and authorization flows
- stopped tracking generated `target/` build output

## Future Improvements

- add pagination and filtering for employee listings
- document the API with OpenAPI or Swagger
- add migration tooling such as Flyway or Liquibase
- expand test coverage for repository and service-level behavior
- add CI automation for build and test execution

## Repository Naming Note

The current repository name is functional but generic. A clearer portfolio-facing name would be:

- `employee-directory-rest-api`

That name is easier to understand at a glance on GitHub, LinkedIn, a portfolio site, or a CV.
