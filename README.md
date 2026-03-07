# FinTrack Pro API 🚀

**FinTrack Pro** is a robust Financial Management API built with **Java 21** and **Spring Boot 3**. This project serves as the backbone for a personal finance ecosystem, featuring a clean architecture, financial intelligence through dashboards, and production-ready configurations.

## 🛠 Tech Stack

* **Language:** Java 21 (LTS)
* **Framework:** Spring Boot 3.4.2
* **Persistence:** Spring Data JPA / Hibernate
* **Database:** PostgreSQL 16 (Development via Docker)
* **Testing:** JUnit 5, Mockito, MockMvc (Full Service & Controller Coverage)
* **Documentation:** SpringDoc OpenAPI (Swagger UI)
* **Utilities:** Lombok, Jakarta Validation

## 🏗 Architecture

The project follows the **Controller-Service-Repository** pattern to ensure separation of concerns:
* **Models:** JPA Entities representing the database schema.
* **DTOs:** Data Transfer Objects (v1) for decoupled and secure API responses.
* **Repositories:** Abstraction layer for database operations (including custom Date Range queries).
* **Services:** Centralized business logic, financial calculations, and null-safe mapping.
* **Handlers:** Global Exception Handling for standardized API error responses.

## 🐳 Getting Started (Docker)

To run the database and the management tools (PgAdmin) locally:

1.  Clone the repository.
2.  Ensure Docker is running.
3.  Execute:
```bash
    docker-compose up -d
```

The API will be accessible at `http://localhost:8080`.
Swagger UI available at: `http://localhost:8080/swagger-ui.html`

## ⚙️ Environment Profiles

The project uses Spring Boot profiles to separate environment configurations:

* **Development** (`application-dev.properties`): Local PostgreSQL via Docker, SQL logging enabled, `ddl-auto=update`.
* **Production** (`application-prod.properties`): Credentials via environment variables (`DB_URL`, `DB_USERNAME`, `DB_PASSWORD`), SQL logging disabled, `ddl-auto=validate`.

To switch profiles, update `spring.profiles.active` in `application.properties`.

## 📌 API Endpoints (Quick Reference)

### 📂 Categories (`/api/v1/categories`)
* `GET /` - List all categories.
* `GET /{id}` - Get a specific category by ID.
* `POST /` - Create a new category.
* `PUT /{id}` - Update an existing category.
* `DELETE /{id}` - Remove a category.

### 💸 Transactions (`/api/v1/transactions`)
* `GET /` - List all transactions with details.
* `GET /{id}` - Get a specific transaction by ID.
* `POST /` - Register a new financial transaction.
* `PUT /{id}` - Update an existing transaction.
* `DELETE /{id}` - Remove a transaction.
* `GET /dashboard?start={date}&end={date}` - Financial summary (Income, Expense, Balance) for a specific date range.

## 🚧 Roadmap & Issues Progress

### ✅ V1 — Core API
- [x] **Issue #1:** Initial project setup and domain mapping (Transaction & Category).
- [x] **Issue #2:** Project Foundation and Category Management (Docker & PostgreSQL).
- [x] **Issue #3:** Transaction Implementation and Service Layer logic.
- [x] **Issue #4:** Global Exception Handling (StandardError & ResourceNotFound).
- [x] **Issue #5:** Data Transfer Objects (DTO) Implementation for v1.
- [x] **Issue #6:** API Versioning Implementation (/api/v1).
- [x] **Issue #7:** Unit Testing with JUnit 5 & Mockito (Service Layer).
- [x] **Issue #8:** Complete Category CRUD & Integration Testing (MockMvc).
- [x] **Issue #9:** Transaction Management & Dashboard Business Rules.
- [x] **Issue #10:** Complete Category CRUD — GET /{id} endpoint.
- [x] **Issue #11:** Domain Enrichment (Category & Transaction).
- [x] **Issue #12:** Complete Transaction CRUD — GET /{id}, PUT /{id}, DELETE /{id}.
- [x] **Issue #13:** Category DTO Refactoring (Request/Response Separation).

### 🔒 V2 — Security & Multi-Tenancy
- [ ] **Issue #14:** User entity + Spring Security + JWT Authentication.
- [ ] **Issue #15:** Data isolation — Category and Transaction linked to authenticated User.

### 🖥️ Frontend Integration
- [ ] Issues to be defined after FinTrack Pro Web V1 is complete.

## 📄 License

This project is licensed under the MIT License.