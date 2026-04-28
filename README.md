# Employee Directory REST API

[![CI](https://github.com/manumiguezz/employee-directory-rest-api/actions/workflows/ci.yml/badge.svg)](https://github.com/manumiguezz/employee-directory-rest-api/actions/workflows/ci.yml)

Spring Boot REST API for managing employee records with role-based access control, request validation, consistent error responses, MySQL persistence, and H2-backed automated tests.

This project is intentionally scoped as a small but polished backend API sample. It focuses on clean REST behavior, maintainable structure, validation, security rules, documentation, and repeatable API testing practices.

## What This Project Demonstrates

- CRUD endpoints for employee management
- DTO-based request and response contracts
- Role-based authorization with Spring Security and HTTP Basic auth
- Bean validation for request quality
- Centralized JSON error handling using Spring `ProblemDetail`
- Spring Data JPA persistence
- MySQL runtime configuration with environment variables
- H2 local and test profiles for quick review
- Integration tests for happy paths, validation errors, authorization, and edge cases

## Tech Stack

- Java 17
- Spring Boot 3.5
- Spring Web
- Spring Data JPA
- Spring Security
- MySQL
- H2 Database
- Maven Wrapper
- JUnit 5
- MockMvc

## Architecture

The application uses a simple layered structure:

- `rest`: REST controllers and endpoint behavior
- `service`: business rules and orchestration
- `dao`: Spring Data JPA repository access
- `entity`: persistence model
- `dto`: API request and response records
- `exception`: centralized REST exception handling

The structure is deliberately compact so the project stays realistic for a portfolio REST API.

## Authentication and Roles

The API uses HTTP Basic authentication with these roles:

| Role | Permissions |
| --- | --- |
| `EMPLOYEE` | Read employees |
| `MANAGER` | Create and update employees |
| `ADMIN` | Delete employees |

Demo credentials for the H2 `local` profile:

| Username | Password | Role |
| --- | --- | --- |
| `ramiro` | `examplepass` | `EMPLOYEE` |
| `matias` | `examplepass` | `EMPLOYEE`, `MANAGER` |
| `alejo` | `examplepass` | `EMPLOYEE`, `MANAGER`, `ADMIN` |

## Running Locally

### Quick Run with H2

Use the `local` profile to run the API without installing MySQL:

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

The API starts at `http://localhost:8080`.

### Run with MySQL

Create the database using the scripts in [`mysqlscripts`](mysqlscripts), then provide datasource configuration through environment variables:

```bash
export DB_URL=jdbc:mysql://localhost:3306/employee_directory
export DB_USERNAME=your_mysql_user
export DB_PASSWORD=your_mysql_password
```

Start the application:

```bash
./mvnw spring-boot:run
```

## Database Scripts

- [`mysqlscripts/employee-directory.sql`](mysqlscripts/employee-directory.sql): employee table and seed data
- [`mysqlscripts/spring-security-users.sql`](mysqlscripts/spring-security-users.sql): quick-start users with `{noop}` demo passwords
- [`mysqlscripts/spring-security-users-bcrypt.sql`](mysqlscripts/spring-security-users-bcrypt.sql): same users with BCrypt hashes for `examplepass`
- [`mysqlscripts/spring-security-finished.sql`](mysqlscripts/spring-security-finished.sql): BCrypt security setup aligned with the application roles

## API Endpoints

| Method | Endpoint | Role Required | Description |
| --- | --- | --- | --- |
| `GET` | `/api/employees` | `EMPLOYEE` | List all employees |
| `GET` | `/api/employees/{id}` | `EMPLOYEE` | Get one employee by id |
| `POST` | `/api/employees` | `MANAGER` | Create an employee |
| `PUT` | `/api/employees/{id}` | `MANAGER` | Update an employee |
| `DELETE` | `/api/employees/{id}` | `ADMIN` | Delete an employee |

## Example Requests

List employees:

```bash
curl -u ramiro:examplepass http://localhost:8080/api/employees
```

Create an employee:

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

Successful response:

```json
{
  "id": 5,
  "firstName": "Laura",
  "lastName": "Gomez",
  "email": "laura.gomez@mail.com"
}
```

Validation error response:

```json
{
  "type": "about:blank",
  "title": "Validation failed",
  "status": 400,
  "detail": "One or more request fields are invalid.",
  "timestamp": "2026-04-27T22:55:00.000000-03:00",
  "path": "/api/employees",
  "errors": {
    "firstName": "firstName is required",
    "email": "email must be a valid email address"
  }
}
```

Manual request examples are available in [`docs/api-requests.http`](docs/api-requests.http).

## Testing

Run the automated tests:

```bash
./mvnw test
```

The test suite covers:

- application context startup
- authenticated read access
- unauthenticated requests
- role-based authorization
- successful create, update, and delete flows
- validation failures
- malformed JSON requests
- duplicate email conflicts
- not-found behavior

## Verification Commands

Useful commands for local review:

```bash
./mvnw test
./mvnw -q -DskipTests package
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

## Future Improvements

- Add pagination and filtering for employee listings
- Document the API with OpenAPI or Swagger
- Add Flyway or Liquibase migrations
- Add Docker Compose for MySQL-based local runs
- Expand service-level test coverage

## QA Automation Relevance

This project supports a QA Automation Engineer profile by showing practical API understanding: authentication, authorization, request validation, error contracts, persistence behavior, repeatable test data, integration testing, and API smoke-test documentation.
