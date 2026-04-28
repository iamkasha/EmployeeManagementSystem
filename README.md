# Employee Management Platform

A full-stack employee management application built with Spring Boot, React, and MySQL. The backend exposes a REST API with validation, pagination, search, structured error responses, and a service/repository architecture. The frontend provides a simple React UI for creating, viewing, updating, searching, and deleting employees.

## Tech Stack

- Java 17
- Spring Boot 3
- Spring Web
- Spring Data JPA
- MySQL
- React 16
- Axios
- Bootstrap
- JUnit 5, Mockito, MockMvc
- Docker and Docker Compose

## Features

- Create, read, update, and delete employees
- Search employees by first name, last name, or email
- Paginated employee listing
- DTO-based request and response objects
- Request validation with clear `400 Bad Request` responses
- Duplicate email detection with `409 Conflict`
- Centralized API error handling
- Environment-based database and frontend API configuration
- Docker Compose setup for backend, frontend, and MySQL

## API Endpoints

| Method | Endpoint | Description |
| --- | --- | --- |
| `GET` | `/api/v1/employees` | List employees with pagination |
| `GET` | `/api/v1/employees?search=akash&page=0&size=10` | Search employees |
| `GET` | `/api/v1/employees/{id}` | Get an employee by id |
| `POST` | `/api/v1/employees` | Create an employee |
| `PUT` | `/api/v1/employees/{id}` | Update an employee |
| `DELETE` | `/api/v1/employees/{id}` | Delete an employee |

## Example Request

```http
POST /api/v1/employees
Content-Type: application/json

{
  "firstName": "Akash",
  "lastName": "Kasha",
  "emailId": "akash@example.com"
}
```

## Run With Docker

```bash
docker compose up --build
```

Services:

```text
Frontend: http://localhost:3000
Backend:  http://localhost:8080
Swagger:  http://localhost:8080/swagger-ui.html
MySQL:    localhost:3306
```

## Run Backend Locally

Set up MySQL with a database named `emsdb`, then run:

```bash
./mvnw spring-boot:run
```

On Windows PowerShell:

```powershell
.\mvnw.cmd spring-boot:run
```

The backend reads configuration from:

```text
DATABASE_URL
DATABASE_USERNAME
DATABASE_PASSWORD
CORS_ALLOWED_ORIGINS
```

## Run Frontend Locally

```bash
cd cms
npm install
npm start
```

Optional frontend API override:

```text
REACT_APP_EMPLOYEE_API_URL=http://localhost:8080/api/v1/employees
```

## Run Tests

```bash
./mvnw test
```

On Windows PowerShell:

```powershell
.\mvnw.cmd test
```

## Project Structure

```text
src/main/java/com/employee/ems
  controller/   REST API endpoints
  dto/          Request and response models
  exception/    API error handling
  model/        JPA entities
  repository/   Spring Data repositories
  service/      Business logic

cms/src
  components/   React views
  services/     Axios API client
```

## Future Improvements

- Add authentication and role-based access control
- Add Flyway database migrations
- Add end-to-end tests for the React workflow

