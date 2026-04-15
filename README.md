# FinTrack Pro API 🚀

**FinTrack Pro** is a robust Financial Management API built with **Java 21** and **Spring Boot 3**. This project serves as the backbone for a personal finance ecosystem, featuring a clean architecture, JWT-based security, financial intelligence through dashboards, and production-ready configurations.

## 🛠 Tech Stack

* **Language:** Java 21 (LTS)
* **Framework:** Spring Boot 3.4.2
* **Persistence:** Spring Data JPA / Hibernate
* **Database:** PostgreSQL 16 (Development via Docker)
* **Security:** Spring Security + JWT (jjwt 0.12.6) + Refresh Token
* **Testing:** JUnit 5, Mockito, MockMvc (Full Service & Controller Coverage)
* **Documentation:** SpringDoc OpenAPI (Swagger UI)
* **Utilities:** Lombok, Jakarta Validation

## 🏗 Architecture

The project follows the **Controller-Service-Repository** pattern to ensure separation of concerns:
* **Models:** JPA Entities representing the database schema.
* **DTOs:** Data Transfer Objects (v1) for decoupled and secure API responses.
* **Repositories:** Abstraction layer for database operations.
* **Services:** Centralized business logic, financial calculations, and null-safe mapping.
* **Config:** Security filter chain, JWT filter, and authentication provider setup.
* **Handlers:** Global Exception Handling for standardized API error responses.

## 🔒 Data Isolation (Multi-Tenancy per User)

All resources are scoped to the authenticated user. `Category` and `Transaction` entities carry a `user_id` FK. Every repository query filters by the authenticated principal — users can only read and modify their own data. Attempting to access a resource owned by another user returns `404` to avoid leaking resource existence.

## 🐳 Getting Started (Docker)

To run the database and the management tools (PgAdmin) locally:

1. Clone the repository.
2. Ensure Docker is running.
3. Execute:
```bash
docker-compose up -d
```

The API will be accessible at `http://localhost:8080`.
Swagger UI available at: `http://localhost:8080/swagger-ui.html`

## 🔐 Authentication

The API uses **stateless JWT authentication**. All endpoints except `/api/v1/auth/**` require a valid Bearer token.

### Auth Flow
1. Register via `POST /api/v1/auth/register` → receive `accessToken` + `refreshToken`.
2. Use `accessToken` in the `Authorization: Bearer {token}` header for all requests.
3. When the token expires, call `POST /api/v1/auth/refresh` with the `refreshToken` to get a new pair.

## ⚙️ Environment Profiles

* **Development** (`application-dev.properties`): Local PostgreSQL via Docker, SQL logging enabled, `ddl-auto=update`.
* **Production** (`application-prod.properties`): Credentials via environment variables (`DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `JWT_SECRET`), SQL logging disabled, `ddl-auto=validate`.

## 📌 API Endpoints (Quick Reference)

### 🔑 Auth (`/api/v1/auth`) — Public
* `POST /register` — Register a new user and receive JWT.
* `POST /login` — Authenticate and receive JWT + refresh token.
* `POST /refresh` — Exchange refresh token for a new token pair.

### 📂 Categories (`/api/v1/categories`) — Protected
* `GET /` — List all categories.
* `GET /{id}` — Get a specific category by ID.
* `POST /` — Create a new category.
* `PUT /{id}` — Update an existing category.
* `DELETE /{id}` — Remove a category.

### 💸 Transactions (`/api/v1/transactions`) — Protected
* `GET /` — List all transactions with details.
* `GET /{id}` — Get a specific transaction by ID.
* `POST /` — Register a new financial transaction.
* `PUT /{id}` — Update an existing transaction.
* `DELETE /{id}` — Remove a transaction.
* `GET /dashboard?start={date}&end={date}` — Financial summary for a date range.

## 🚧 Roadmap & Issues Progress

### ✅ V1 — Core API
- [x] **Issue #1:** Initial project setup and domain mapping.
- [x] **Issue #2:** Project Foundation and Category Management (Docker & PostgreSQL).
- [x] **Issue #3:** Transaction Implementation and Service Layer logic.
- [x] **Issue #4:** Global Exception Handling.
- [x] **Issue #5:** Data Transfer Objects (DTO) Implementation for v1.
- [x] **Issue #6:** API Versioning Implementation (/api/v1).
- [x] **Issue #7:** Unit Testing with JUnit 5 & Mockito.
- [x] **Issue #8:** Complete Category CRUD & Integration Testing.
- [x] **Issue #9:** Transaction Management & Dashboard Business Rules.
- [x] **Issue #10:** Complete Category CRUD — GET /{id}.
- [x] **Issue #11:** Domain Enrichment (Category & Transaction).
- [x] **Issue #12:** Complete Transaction CRUD.
- [x] **Issue #13:** Category DTO Refactoring (Request/Response Separation).

### 🔒 V2 — Security & Multi-Tenancy
- [x] **Issue #14:** User entity + Spring Security + JWT Authentication + Refresh Token.
- [x] **Issue #15:** Data isolation — Category and Transaction linked to authenticated User.

### 🔧 V3 — Resilience & Observability
- [x] **Issue #16:** Database Migrations with Flyway.
- [x] **Issue #17:** Input Validation Hardening.
- [x] **Issue #18:** Data Audit (Created/Updated Timestamps).
- [x] **Issue #19:** Structured Logging (JSON / GCP & AWS Ready).
- [x] **Issue #20:** Pagination & Filtering on List Endpoints.
- [ ] **Issue #21:** Dashboard Enrichment (Per-Category Analytics).

### 🚀 V4 — Deploy
- [ ] **Issue #22:** Production Deploy on Railway.

## 📄 License

This project is licensed under the MIT License.