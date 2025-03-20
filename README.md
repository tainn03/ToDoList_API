# ToDoList API

## Project Overview

The ToDoList API is a backend service designed to manage tasks in a to-do list application. It provides a RESTful API for task management, including features such as task dependencies, circular dependency validation, caching and performance optimization, a notification system, and Docker support for containerization.

## Tech Stack

- **Programming Language**: Java
- **Framework**: Spring Boot
- **Database**: MySQL
- **Caching**: Redis
- **Containerization**: Docker

## Functionalities

### Task Management

- **Create Task**: Add a new task to the to-do list.
- **Read Task**: Retrieve details of a specific task.
- **Update Task**: Modify an existing task.
- **Delete Task**: Remove a task from the to-do list.
- **List Tasks**: Retrieve a list of all tasks.

### Task Dependencies

- **Add Dependency**: Specify that one task depends on another.
- **Remove Dependency**: Remove a dependency between tasks.
- **List Dependencies**: Retrieve a list of all dependencies for a task.

### Circular Dependency Validation

- **Validate Dependencies**: Ensure that adding a dependency does not create a circular dependency.

### Additional Features

- **Caching and Performance Optimization**: Use Redis to cache frequently accessed data and improve performance.
- **Notification System**: Send log notifications for upcoming or overdue tasks.
- **Deployment with Docker**: Use Docker for containerization and deployment.

## API Endpoints

### Task Management

- **GET api/v1/tasks**: Retrieve a list of all tasks.
- **GET api/v1/tasks/{id}**: Retrieve a specific task by ID.
- **POST api/v1/tasks**: Create a new task.
- **PUT api/v1/tasks/{id}**: Update a specific task by ID.
- **DELETE api/v1/tasks/{id}**: Delete a specific task by ID.

### Task Dependencies

- **POST api/v1/tasks/{id}/dependencies**: Add a dependency to a task.
- **DELETE api/v1/tasks/{id}/dependencies/{dependency_id}**: Remove a dependency from a task.
- **GET api/v1/tasks/{id}/dependencies**: Retrieve a list of all dependencies for a task.

## Getting Started

1. **Clone the repository**:
   ```
   git clone https://github.com/tainn03/ToDoList_API.git
   cd ToDoList_API
   ```
2. **Build the project**:
   ```
   docker-compose up --build
   ```
3. **Import Postman collection file to test available endpoints**

