# Client Management API

## Overview
This project is a Spring Boot backend for managing client entities. It exposes REST endpoints for CRUD operations,
supports search and sorting, provides structured error handling, and uses basic authentication. Kafka integration
for event publishing is planned but not yet implemented.

<!-- TOC -->
* [Client Management API](#client-management-api)
  * [Overview](#overview)
  * [Architecture](#architecture-)
  * [CRUD Workflow & Role Permissions](#crud-workflow--role-permissions)
  * [Setup Instructions](#setup-instructions)
    * [Prerequisites](#prerequisites)
    * [Build and Run](#build-and-run)
    * [Access Points](#access-points)
  * [Development Insights](#development-insights)
  * [Example API Usage](#example-api-usage)
    * [Get all clients](#get-all-clients)
    * [Get client by ID](#get-client-by-id)
    * [Get clients (by name or id) with sorting](#get-clients-by-name-or-id-with-sorting)
    * [Create a client](#create-a-client)
    * [Update a client](#update-a-client-)
    * [Delete a client](#delete-a-client)
  * [Challenges & Solutions](#challenges--solutions)
  * [Potential Future Improvements](#potential-future-improvements)
  * [Authentication](#authentication)
<!-- TOC -->

## Architecture 

```
[Controller] -> [Service] -> [Repository] -> [H2 Database]
                    |
                    v
              (Future Kafka)
```
* **Controller Layer**: Handles REST endpoints and request validation.
* **Service Layer**: Implements business logic.
* **Repository Layer**: Interacts with the database using Spring Data JPA.
* **H2 Database**: In-memory database with seeded sample data.
* **Kafka (Planned)**: Will publish create/update/delete events to client-events topic.

The project follows a layered architecture for maintainability and separation of concerns.

## CRUD Workflow & Role Permissions

            +---------------------------+
            |   CLIENT MANAGEMENT API   |
            +---------------------------+
                          |
          -----------------------------------
          |          |          |           |
        CREATE      READ       UPDATE      DELETE
          |          |          |           |
        (ADMIN,   (ALL ROLES)  (ADMIN,     (ADMIN)
         EDITOR)                 EDITOR)

* **ADMIN**: Full access to all endpoints.
* **EDITOR**: Can create, update, and read clients.
* **VIEWER**: Read-only access.

Currently, the user details are stored in a JSON as a resource file packaged with the app and the security is
configured at launch of the app.


## Setup Instructions

### Prerequisites
* Java 21
* Maven 3+
* Docker (for Kafka testing, future work)

### Build and Run
**1.** Clone the repository
```shell
    git clone https://github.com/vishnupriyab27/ClientManagement.git
    cd ./ClientManagement/
```

**2.** Build
```shell
    mvn clean install
```
**3.** Run
```shell
    mvn spring-boot:run
```
### Access Points
* **Swagger UI:** `http://localhost:8080/swagger-ui.html`
* **H2 Console:** `http://localhost:8080/h2-console`
    * JDBC URL: jdbc:h2:mem:clients 
    * Username: sa 
    * Password: (leave blank)

Swagger UI provides interactive documentation for all endpoints, including authentication.

## Development Insights
* Started by defining the **Client entity** 
* Implemented **CRUD endpoints** in Controller + Service + Repository layers.
* Added **global exception handling** for consistent error responses.
* Added seeding initial data in H2 at app launch. 
* Added **search and sorting** functionality to make API responses more flexible. 
* Configured **Spring Security** with three roles (ADMIN, EDITOR, VIEWER) to control access. 
* Integrated **Swagger/OpenAPI** to document endpoints and authentication requirements.

## Example API Usage

### Get all clients

```shell
   GET /api/v1/clients/all
   Authorization: Basic <Base64Creds>
```

### Get client by ID

```shell
   GET /api/v1/clients/{id}
   Authorization: Basic <Base64Creds>
```

### Get clients (by name or id) with sorting

```shell
   Get /api/v1/clients?name={name}&sortBy={name|id}&direction={ASC|DESC}
   Authorization: Basic <Base64Creds>
   Query Params:
        name  : {name}
        id    : {id}
        sortBy: {name|id}
        direction: {ASC|DESC}
```

### Create a client

```shell
   POST /api/v1/clients
   Authorization: Basic <Base64Creds>
   Body:
    {
        "fullName": "Thor Odinson",
        "displayName": "Strongest Avenger",
        "email": "thor@odinson.com",
        "details": "God of Thunder.",
        "active": true,
        "location": "Asgard"
    }
```

### Update a client 

```shell
   PUT /api/v1/clients/{id}
   Authorization: Basic <Base64Creds>
   Body:
    {
        "fullName": "Late Tony Stark",
        "displayName": "Late Tony S.",
        "email": "Tony@starkIsDead.com",
        "details": "Brilliant engineer and entrepreneur specializing in cutting-edge technology and robotics. He died in battle.",
        "active": false,
        "location": "Heaven"
    }
```
### Delete a client

```shell
   DELETE /api/v1/clients/{id}
   Authorization: Basic <Base64Creds>
```

All endpoints can also be tested via Swagger UI.

## Challenges & Solutions

* **Role-based access:** Configuring Spring Security to allow correct access for ADMIN, EDITOR, and VIEWER roles. 
Solved with deriving the user details from `users.json` added as part of resources.
* **Database seeding:** Needed H2 to populate sample clients on every app start. Implemented via CommandLineRunner to
load data from another resources file - `clients.json`.
* **Error consistency:** Wanted structured, clear error responses. Implemented a global exception handler.

## Potential Future Improvements

* Add **Kafka** integration to publish client change events. 
* Replace **H2** with a persistent database like PostgreSQL. 
* Implement **pagination** for large client datasets. 
* Add **unit and integration tests** for endpoints and services. 
* Move from **Basic Auth to JWT** for better security and scalability.
* Add **Audit logging** to track and store significant user actions

## Authentication

| Fullname      | Password | Email                          | Role   |
|---------------|----------|--------------------------------|--------|
| Sheila Cox    | sheilaC  | sheila.cox@example.com         | VIEWER |
| Angel Hansen  | angelH   | angel.hansen@example.com       | EDITOR |
| Michael Scott | michaelS | theWorldsBestBOSS@example.com  | ADMIN  |


