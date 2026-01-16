# Task Manager Application

A Spring Boot application with PostgreSQL database integration featuring user and task management capabilities.

## Features

- User management (CRUD operations)
- Task management (CRUD operations)
- RESTful API endpoints
- PostgreSQL database integration
- Comprehensive unit and integration tests
- Maven build system

## Technologies Used

- Java 17+
- Spring Boot 3.x
- Spring Data JPA
- PostgreSQL
- Hibernate
- Maven
- JUnit 5 for testing

## Setup Instructions

1. Clone the repository
2. Set up PostgreSQL database
3. Update database credentials in `src/main/resources/application.properties`
4. Run the application using `mvn spring-boot:run`

## API Endpoints

### User Management
- GET `/api/users` - Get all users
- GET `/api/users/{id}` - Get user by ID
- POST `/api/users` - Create new user
- PUT `/api/users/{id}` - Update user
- DELETE `/api/users/{id}` - Delete user

### Task Management
- GET `/api/tasks` - Get all tasks
- GET `/api/tasks/{id}` - Get task by ID
- POST `/api/tasks` - Create new task
- PUT `/api/tasks/{id}` - Update task
- DELETE `/api/tasks/{id}` - Delete task

## Testing

Run tests using: `mvn test`