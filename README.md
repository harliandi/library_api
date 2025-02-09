# Library API

A REST API to manage a library's book lending process. This API allows administrators to add books, register borrowers, process book borrowing/returns, and track overdue borrow records.

## Table of Contents

- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Configuration](#configuration)
- [Building and Running the Application](#building-and-running-the-application)
- [OpenAPI Documentation](#openapi-documentation)
- [Dockerization](#dockerization)
  - [Using a Dockerfile](#using-a-dockerfile)
  - [Using Docker Compose](#using-docker-compose)

## Prerequisites

- **Java 21** or later
- **Maven** (or use the Maven Wrapper included with the project)
- **PostgreSQL** database (or any compatible database)
- **Docker** (for containerization)

## Installation

1. **Clone the repository**

   ```bash
   git clone <repository-url>
   cd library-api
   ```

2. **Create a .env file**
    
    In the root directory of the project, create a file named .env with your environment-specific variables. For example:

    ```env
    DB_URL=jdbc:postgresql://localhost:5432/library_db
    DB_USERNAME=your_db_username
    DB_PASSWORD=your_db_password
    ```

## Configuration

In src/main/resources/application.properties, the database configuration uses environment variable placeholders. For example:

```properties
spring.datasource.url=${DB_URL:jdbc:postgresql://localhost:5432/library_db}
spring.datasource.username=${DB_USERNAME:default_user}
spring.datasource.password=${DB_PASSWORD:default_password}
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```
This configuration means that if the environment variables (DB_URL, DB_USERNAME, DB_PASSWORD) are not set, default values will be used.    

## Building and Running the Application
### Using Maven
1. Build the project:

    ```bash
    mvn clean install
    ```
2. Run the application:
    ```bash
    mvn spring-boot:run
    ```
    The API will be available at http://localhost:8080.

## Export Environment Variables Locally
Before running locally, you can load the environment variables from your .env file by running:

```bash
export $(grep -v '^#' .env | xargs)
```
Then run your Maven command in the same terminal session so that the variables are available to your application.

## OpenAPI Documentation
The API documentation is automatically generated using Springdoc OpenAPI. Once the application is running, you can access the Swagger UI at:

http://localhost:8080/swagger-ui.html

## Dockerization
### Using a Dockerfile
A Dockerfile is provided in the project root. To build the Docker image:

```bash
docker build -t library-api .
```

Then, run the Docker container while passing the environment variables from the .env file:

```bash
docker run --env-file .env -p 8080:8080 library-api
```
### Using Docker Compose
A sample docker-compose.yml is provided. It builds the image from the Dockerfile and loads the environment variables:

```yaml
version: '3.8'
services:
  library-api:
    build:
      context: .
      dockerfile: Dockerfile
    ports:
      - "8080:8080"
    env_file:
      - .env
```

Start the service using:

```bash
docker-compose up --build
```