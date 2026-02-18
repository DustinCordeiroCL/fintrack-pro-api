# FinTrack Pro API 🚀

**FinTrack Pro** is a professional Financial Management API built with **Java 21** and **Spring Boot 3**. This project follows clean architecture principles and production-ready configurations to ensure scalability and maintainability.

## 🛠 Tech Stack

* **Language:** Java 21 (LTS)
* **Framework:** Spring Boot 3.4.2
* **Persistence:** Spring Data JPA / Hibernate
* **Database:** PostgreSQL 16 (Development via Docker)
* **API Versioning:** URI-based (v1)
* **Utilities:** Lombok, Jakarta Validation, Jackson (JSON)

## 🏗 Architecture & Patterns

The project follows a layered architecture to ensure separation of concerns:
* **Models (Entities):** JPA Entities representing the database schema.
* **DTOs (Data Transfer Objects):** Decoupling the persistence layer from the web layer for security and flexibility (v1).
* **Repositories:** Abstraction layer for database operations.
* **Services:** Centralized business logic, transaction management, and validations.
* **Controllers:** Versioned REST endpoints (v1) for external communication.
* **Global Exception Handling:** Centralized error management returning standardized JSON responses.

## 🐳 Getting Started (Docker)

To run the database and the management tools locally:

1. Clone the repository.
2. Ensure Docker is running.
3. Execute:
    ```bash
    docker-compose up -d
    ```

The API will be accessible at `http://localhost:8080/api/v1`.

## 📌 API Endpoints (v1)

### Categories
* `GET /api/v1/categories` - List all categories as DTOs.
* `POST /api/v1/categories` - Create a new category.

### Transactions
* `GET /api/v1/transactions` - List all transactions (including nested CategoryDTO).
* `POST /api/v1/transactions` - Register a new financial transaction.

## 🛡️ Standardized Error Response (Sample)
```json
{
  "timestamp": "2026-02-18T19:24:16Z",
  "status": 404,
  "error": "Resource Not Found",
  "message": "Resource not found. ID: 99",
  "path": "/api/v1/transactions"
}