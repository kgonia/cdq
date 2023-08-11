# Task Management API

This project provides a simple REST API for asynchronous tasks processing, primarily designed to find the best match between a pattern and an input string. The task will locate the position of the pattern in the input with the fewest character differences.

## Project Architecture

**Project is written with KISS rule in mind. It's just one app with two modules with very low coupling between them so it can be easlly changed to microservice solution. I'm happy to talk about production like solution. Task and results are kept in memoery so all stuff is gone after restart.**

Caching isn't implemented

## Features

1. **Create a Task**: Allows users to create a task with a unique ID.
2. **List All Tasks**: Users can retrieve a list of all created tasks.
3. **Read Task Status & Results**: Users can check the status and results of a task using the unique ID received during task creation.

The task accepts two strings as parameters:
- `pattern`: The pattern to match.
- `input`: The input string where the pattern will be searched for.

The task will find the first best match – the position of the pattern in the input with the least number of different characters.

## Running the Application

To run the application using Docker Compose, make sure you have `docker-compose` installed and follow these steps:

1. Clone the repository.
2. Navigate to the project directory where the `docker-compose.yml` file is located.
3. Run the following commands:

   ```bash
   docker-compose build
   docker-compose up
   ```

The application will be accessible at `http://localhost:8080`.

## API Endpoints

The API includes the following endpoints:

- `POST /tasks`: Creates a new task.
- `GET /tasks`: Returns a list of tasks.
- `GET /tasks/{task_id}`: Returns the details of the task with the given task ID.

Detailed Swagger API documentation is available under [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html).

### Example Request & Response

#### Create Task

**Request**:

POST /tasks
```json
{
  "pattern": "abc",
  "input": "abcdabc"
}
```

**Response**:

```json
{
  "task_id": "123e4567-e89b-12d3-a456-426614174000"
}
```

For more details, please refer to the Swagger documentation.

