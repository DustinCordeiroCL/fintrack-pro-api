# FinTrack Pro API

**FinTrack Pro** is a personal finance management API built with **Java 21** and **Spring Boot 3**. It serves as the backend for a financial tracking application targeted at the Chilean market, featuring JWT-based security, multi-tenancy, category analytics, and production-ready deployment on Fly.io + Supabase.

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 (LTS) |
| Framework | Spring Boot 3.4.2 |
| Persistence | Spring Data JPA / Hibernate |
| Database | PostgreSQL 16 (Docker locally, Supabase in production) |
| Migrations | Flyway |
| Security | Spring Security + JWT (jjwt 0.12.6) + Refresh Token |
| Testing | JUnit 5, Mockito, MockMvc |
| Documentation | SpringDoc OpenAPI (Swagger UI) |
| Deploy | Fly.io (container) + Supabase (managed PostgreSQL) |
| Utilities | Lombok, Jakarta Validation |

## Architecture

The project follows the **Controller → Service → Repository** pattern:

- **Models** — JPA entities representing the database schema
- **DTOs** — versioned request/response objects (`v1`) decoupled from the domain
- **Repositories** — Spring Data abstractions per entity
- **Services** — business logic, financial calculations, user-scoped queries
- **Config** — security filter chain, CORS, JWT filter, authentication provider
- **Handlers** — global exception handling with standardized error responses

## Data Isolation

All resources are scoped to the authenticated user. `Category` and `Transaction` carry a `user_id` FK. Every repository query filters by the authenticated principal — a user can never read or modify another user's data. Accessing a resource owned by another user returns `404` to avoid leaking resource existence.

## Getting Started (local)

Requires Docker and Java 21.

```bash
# Start PostgreSQL locally
docker-compose up -d

# Run the API (dev profile active by default)
./mvnw spring-boot:run
```

API available at `http://localhost:8080`
Swagger UI at `http://localhost:8080/swagger-ui.html`

## Authentication

The API uses stateless JWT authentication. All endpoints except `/api/v1/auth/**` require a valid Bearer token.

```
POST /api/v1/auth/register  → { accessToken, refreshToken }
POST /api/v1/auth/login     → { accessToken, refreshToken }
POST /api/v1/auth/refresh   → { accessToken, refreshToken }
```

Use `Authorization: Bearer {accessToken}` on all protected requests.

## Environment Profiles

| Profile | Purpose |
|---|---|
| `dev` | Local Docker PostgreSQL, SQL logging on |
| `prod` | Credentials via env vars, SQL logging off, JSON structured logs |

**Production environment variables required:**

```
DB_URL           jdbc:postgresql://host:5432/db
DB_USERNAME      postgres
DB_PASSWORD      ...
JWT_SECRET       ...  (min 256-bit random string)
SPRING_PROFILES_ACTIVE  prod
```

## API Reference

### Auth — `/api/v1/auth` (public)
| Method | Path | Description |
|---|---|---|
| POST | `/register` | Register and receive token pair |
| POST | `/login` | Authenticate and receive token pair |
| POST | `/refresh` | Refresh expired access token |

### Categories — `/api/v1/categories` (protected)
| Method | Path | Description |
|---|---|---|
| GET | `/` | List all categories |
| GET | `/{id}` | Get category by ID |
| POST | `/` | Create category |
| PUT | `/{id}` | Update category |
| DELETE | `/{id}` | Delete category |

### Transactions — `/api/v1/transactions` (protected)
| Method | Path | Description |
|---|---|---|
| GET | `/` | List transactions (paginated, filterable) |
| GET | `/{id}` | Get transaction by ID |
| POST | `/` | Create transaction |
| PUT | `/{id}` | Update transaction |
| DELETE | `/{id}` | Delete transaction |
| GET | `/dashboard` | Financial summary with per-category analytics |

## Changelog

### V4 — Deploy
- [x] **#22** Production deploy on Fly.io + Supabase

### V3 — Resilience & Observability
- [x] **#21** Dashboard enrichment with per-category analytics
- [x] **#20** Pagination and filtering on list endpoints
- [x] **#19** Structured JSON logging (ECS format, production-ready)
- [x] **#18** Audit timestamps (`created_at`, `updated_at`)
- [x] **#17** Input validation hardening (Jakarta Validation)
- [x] **#16** Database migrations with Flyway

### V2 — Security & Multi-Tenancy
- [x] **#15** Data isolation — categories and transactions scoped to authenticated user
- [x] **#14** Spring Security + JWT authentication + refresh token

### V1 — Core API
- [x] **#1–13** Domain modeling, CRUD endpoints, DTOs, exception handling, versioning, tests

## License

MIT
