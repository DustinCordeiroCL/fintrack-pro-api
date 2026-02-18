# FinTrack Pro API 🚀

**FinTrack Pro** is a robust Financial Management API built with **Java 21** and **Spring Boot 3**. This project serves as the backbone for a personal finance ecosystem, featuring a clean architecture and production-ready configurations.

## 🛠 Tech Stack

* **Language:** Java 21 (LTS)
* **Framework:** Spring Boot 3.4.2
* **Persistence:** Spring Data JPA / Hibernate
* **Database:** PostgreSQL 16 (Development via Docker)
* **Documentation:** SpringDoc OpenAPI (Swagger UI)
* **Utilities:** Lombok, Jakarta Validation

## 🏗 Architecture

The project follows the **Controller-Service-Repository** pattern to ensure separation of concerns:
* **Models:** JPA Entities representing the database schema.
* **Repositories:** Abstraction layer for database operations.
* **Services:** Centralized business logic and validations.
* **Controllers:** REST endpoints for external communication.

## 🐳 Getting Started (Docker)

To run the database and the management tools (PgAdmin) locally:

1.  Clone the repository.
2.  Ensure Docker is running.
3.  Execute:
    ```bash
    docker-compose up -d
    ```

The API will be accessible at `http://localhost:8080`.

## 📌 API Endpoints (Quick Reference)

### Categories
* `GET /api/categories` - List all categories.
* `POST /api/categories` - Create a new category.

### Transactions
* `GET /api/transactions` - List all transactions (including category details).
* `POST /api/transactions` - Register a new financial transaction.

## 🚧 Roadmap & Issues

- [x] **Issue #1:** Project Bootstrap & Docker Setup.
- [x] **Issue #2:** Category Management Implementation.
- [x] **Issue #3:** Transaction Logic & Service Layer.
- [x] **Issue #4:** Global Exception Handling (Upcoming).
- [x] **Issue #5:** DTO Pattern Implementation (Upcoming).
- [ ] **Issue #6:** Unit Testing with JUnit 5 & Mockito (Upcoming).

## 📄 License

This project is licensed under the MIT License.