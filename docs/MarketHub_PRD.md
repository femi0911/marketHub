# MarketHub — Product Requirements Document (PRD)

**A secure marketplace \+ wallet backend for African SMEs**

|  |  |
| :---- | :---- |
| **Product** | MarketHub |
| **Document type** | Product Requirements Document / Technical Specification |
| **Version** | 1.0 |
| **Status** | Approved for build (course capstone baseline) |
| **Owner** | Semicolon Africa — Backend Engineering Track |
| **Audience** | Bootcamp engineers (implementers), facilitators (reviewers) |
| **Related docs** | Spring Boot Module curriculum; Capstone grading rubric |

**How to read this document.** This PRD is the single source of truth for *what* MarketHub must do and *why*. It deliberately avoids prescribing *how* to implement each feature (that is the engineer's job, guided by the course). Requirements are tagged with IDs (e.g. `FR-A1`, `NFR-S2`) so they can be referenced in commits, pull requests, and reviews. Treat every "acceptance criteria" block as the checklist your code review will run against.

---

## 1\. Overview

### 1.1 Summary

MarketHub is the **backend** for a multi-vendor online marketplace with an integrated digital wallet. Vendors list products; customers browse, order, and pay from a wallet balance; administrators keep the platform safe and healthy. It is designed to be secure enough to move money and structured to scale toward a microservices architecture.

### 1.2 Problem statement

Small and medium businesses across Africa need a trustworthy way to sell online and get paid, without each building their own commerce and payments stack. Buyers need a single account to discover products from many vendors, pay safely, and track orders. Today this is fragmented across social-media DMs, manual bank transfers, and spreadsheets — which is slow, error-prone, and hard to trust with money.

### 1.3 Vision

A backend that a real African fintech-marketplace could be built on: **secure by design, observable, horizontally scalable, and event-driven**, exposing a clean REST API that any web or mobile client can consume.

### 1.4 Currency & money handling

All monetary amounts are denominated in **Nigerian Naira (NGN)** and stored as **integer kobo** (1 NGN \= 100 kobo). Money is never represented with floating-point types. Display formatting (₦) is a client concern.

---

## 2\. Goals & Non-Goals

### Goals

- Let vendors manage their own catalog and fulfil orders.  
- Let customers discover products, place multi-item orders, and pay from a wallet.  
- Guarantee that users can only access and act on **their own** money and orders.  
- Provide administrators the controls to moderate the platform.  
- Ship a documented, tested, deployable API that demonstrates production backend practices.

### Non-Goals (v1)

- No frontend/mobile UI (API only; clients are out of scope).  
- No real bank/card integration — wallet funding is **simulated** (mock top-up).  
- No shipping/logistics integration (status is tracked, not physically fulfilled).  
- No multi-currency, no FX, no tax/VAT engine.  
- No recommendation engine, reviews, or chat.

---

## 3\. Personas & Stakeholders

| Persona | Role | Primary needs |
| :---- | :---- | :---- |
| **Amaka — Customer** | Buys products | Register/login, browse & search, order, pay from wallet, track orders, see history |
| **Bola — Vendor** | Sells products | Manage own catalog, view & fulfil incoming orders, see settlements |
| **Chidi — Admin** | Platform operator | Oversee users/orders, deactivate bad actors, view platform health |
| **Facilitator** | Reviewer | Assess against this PRD and the rubric |

---

## 4\. Success Metrics

These are the signals that MarketHub "works" (measured in test/seed conditions, not a live market):

- **Correctness:** 100% of acceptance criteria in Section 6 pass.  
- **Security:** 0 endpoints leak data across users; 0 plaintext secrets in the repo; all protected routes reject missing/invalid/expired tokens.  
- **Integrity:** 0 wallet balance discrepancies after concurrent order/payment tests (debits and credits always reconcile).  
- **Performance:** product listing P95 \< 300 ms on a 10k-row seed under pagination.  
- **Quality:** ≥ 80% of critical paths covered by automated tests, including one end-to-end auth+order+payment integration test.

---

## 5\. Scope

### 5.1 In scope (MVP)

Accounts & authentication; role-based access; product catalog with search & pagination; categories; multi-item orders with a status lifecycle; per-user wallet with simulated funding and atomic order payment; transaction history; administration; domain events (order lifecycle) with at least one asynchronous consumer; OpenAPI documentation; automated tests; containerization and cloud deployment.

### 5.2 Out of scope / Future (v2+)

Real payment-gateway integration (Paystack/Flutterwave); payouts/settlement to vendor bank accounts; product reviews & ratings; search relevance ranking; refunds & disputes; notifications via email/SMS providers; multi-currency; analytics dashboards; splitting into independently deployed microservices.

---

## 6\. Functional Requirements & User Stories

Each requirement has an ID, a user story, and acceptance criteria. Status codes follow REST conventions (Section 8).

### Epic A — Accounts & Authentication

**FR-A1 — Registration** *As a visitor, I can create an account so that I can use MarketHub.*

- Accepts email, password, display name, and requested role (`CUSTOMER` or `VENDOR`; `ADMIN` is never self-assignable).  
- Password is stored only as a BCrypt hash; the raw password is never persisted or logged.  
- Duplicate email is rejected with `409 Conflict`.  
- Invalid input (bad email, weak/short password) returns `400` with field-level errors.  
- On success, a wallet is automatically created for the user with a zero balance.

**FR-A2 — Login** *As a registered user, I can log in to obtain access.*

- Valid credentials return a short-lived **access token (JWT)** and a **refresh token**.  
- Invalid credentials return `401` with no detail about which field was wrong.  
- Tokens encode the user identity and roles/authorities; the access token has a configurable TTL.

**FR-A3 — Token refresh** *As a logged-in user, I can refresh my access token without re-entering credentials.*

- A valid, non-revoked refresh token issues a new access token.  
- An expired, unknown, or revoked refresh token returns `401`.

**FR-A4 — Logout** *As a logged-in user, I can log out so my session can't be resumed.*

- Logout revokes the presented refresh token; subsequent refresh attempts with it fail.

**FR-A5 — Current user profile** *As a logged-in user, I can fetch my own profile.*

- Returns id, email, display name, roles — never the password hash.

### Epic B — Product Catalog

**FR-B1 — Create product (Vendor)** *As a vendor, I can list a product for sale.*

- Requires `VENDOR` (or `ADMIN`). Fields: name, description, price (kobo, \> 0), stock quantity (≥ 0), category.  
- The product is owned by the creating vendor.  
- Validation failures return `400` with field errors.

**FR-B2 — Update / delete own product (Vendor)** *As a vendor, I can edit or remove only my own products.*

- A vendor editing/deleting another vendor's product receives `403`.  
- `ADMIN` may edit/delete any product.

**FR-B3 — Browse & view products (Public/Customer)** *As anyone, I can list and view products.*

- Listing supports **pagination & sorting** (page, size, sort) and returns page metadata (total elements/pages).  
- A single product is retrievable by id; unknown id returns `404`.

**FR-B4 — Search & filter** *As a customer, I can find products by criteria.*

- Supports optional, combinable filters: name contains, price range, category, in-stock-only.  
- Any combination (including none) is valid and returns correct results.

**FR-B5 — Categories** *As a vendor/admin, I can organize products into categories.*

- A product belongs to exactly one category; a category lists its products without triggering N+1 queries.

### Epic C — Orders & Checkout

**FR-C1 — Place an order (Customer)** *As a customer, I can order one or more products in a single order.*

- An order contains line items, each referencing a product and a quantity (\> 0).  
- The order total is computed server-side from current product prices (never trusted from the client).  
- Ordering more than the available stock returns `400` (insufficient stock) and creates no order.  
- A successful order is created in status `PENDING`.

**FR-C2 — Pay for an order (Customer)** *As a customer, I can pay for my order from my wallet.*

- Payment debits the customer's wallet by the order total **atomically** (see FR-D2); on success the order moves to `PAID` and stock is decremented.  
- Insufficient wallet balance returns `402`/`400` (documented), leaves the order `PENDING`, and changes no balances or stock.

**FR-C3 — Order status lifecycle** *As the system, I enforce valid status transitions.*

- Allowed transitions: `PENDING → PAID → SHIPPED → DELIVERED`, plus `PENDING → CANCELLED` and `PAID → CANCELLED` (with wallet refund on cancel of a paid order).  
- Invalid transitions (e.g. `DELIVERED → PENDING`) return `409`.  
- `SHIPPED`/`DELIVERED` transitions are performed by the owning vendor or an admin.

**FR-C4 — View orders (ownership-scoped)** *As a customer, I see only my orders; as a vendor, I see orders containing my products.*

- A customer requesting another customer's order receives `403`.  
- Listing is paginated; responses use DTOs (no entities leaked).

### Epic D — Wallet & Payments

**FR-D1 — Fund wallet (simulated)** *As a user, I can top up my wallet (mock).*

- A funding request credits the user's own wallet by a positive amount and records a `CREDIT` transaction.  
- Funding another user's wallet is impossible (ownership-scoped).

**FR-D2 — Atomic order payment** *As the system, I guarantee money correctness.*

- Debiting the wallet, decrementing stock, and advancing the order to `PAID` occur in a **single transaction**; any failure rolls all of them back.  
- Concurrent payments from the same wallet never overdraw it (no negative balance under race conditions).

**FR-D3 — Transaction history** *As a user, I can view my wallet transactions.*

- Returns a paginated, time-ordered list of `CREDIT`/`DEBIT` entries with amount, type, reference (e.g. order id), and timestamp — scoped to the requesting user only.

### Epic E — Administration

**FR-E1 — Manage users** *As an admin, I can list users and deactivate a vendor/customer.*

- A deactivated user cannot log in or obtain tokens; their existing access token is rejected once expired (refresh is revoked immediately).

**FR-E2 — Oversee orders** *As an admin, I can view all orders across the platform.*

**FR-E3 — Admin-only protection**

- All admin endpoints require the `ADMIN` role; non-admins receive `403`.

### Epic F — Notifications & Events

**FR-F1 — Domain events** *As the system, I publish events when significant things happen.*

- Placing/paying for an order publishes an event (e.g. `OrderPlaced`, `OrderPaid`) **after the database transaction commits**.  
- At least one consumer reacts **asynchronously** (e.g. a receipt/notification handler) without blocking the request.

**FR-F2 — Messaging-ready (stretch)** *As the system, I can emit order events to a message broker.*

- An order event can be published to a Kafka topic and processed by a separate, **idempotent** consumer (safe to receive the same event twice).

---

## 7\. Domain Model

Core entities and relationships:

AppUser 1───1 Wallet 1───\* WalletTransaction

   │

   │ 1

   ▼ \*

 Order \*───1 AppUser (customer)

   │ 1

   ▼ \*

 OrderItem \*───1 Product \*───1 Category

                    │ \*

                    ▼ 1

                 AppUser (vendor)

AppUser \*───\* Role \*───\* Permission

Key entities (non-exhaustive):

- **AppUser** — identity, credentials (hashed), roles, active flag, auditing timestamps.  
- **Role / Permission** — RBAC (`CUSTOMER`, `VENDOR`, `ADMIN`) with fine-grained authorities (e.g. `PRODUCT_WRITE`, `WALLET_READ`).  
- **Product** — name, description, priceKobo, stockQty, vendor (owner), category.  
- **Category** — name; one-to-many products.  
- **Order** — customer, status, totalKobo, items, timestamps.  
- **OrderItem** — product, quantity, unitPriceKobo (captured at order time).  
- **Wallet** — owner (one-to-one), balanceKobo.  
- **WalletTransaction** — wallet, type (CREDIT/DEBIT), amountKobo, reference, timestamp.  
- **RefreshToken** — user, hashed token, expiry, revoked flag.

All persistent entities carry `createdAt`/`updatedAt` auditing fields.

---

## 8\. API Surface (high-level)

REST, JSON, versioned under `/api/v1`. Representative endpoints (not exhaustive):

| Method | Path | Auth | Purpose |
| :---- | :---- | :---- | :---- |
| POST | `/auth/register` | public | Create account (FR-A1) |
| POST | `/auth/login` | public | Obtain tokens (FR-A2) |
| POST | `/auth/refresh` | public | Refresh access token (FR-A3) |
| POST | `/auth/logout` | user | Revoke refresh token (FR-A4) |
| GET | `/users/me` | user | Current profile (FR-A5) |
| GET | `/products` | public | List/search products (FR-B3/B4) |
| POST | `/products` | vendor/admin | Create product (FR-B1) |
| PUT/DELETE | `/products/{id}` | owner/admin | Edit/remove own product (FR-B2) |
| GET | `/categories` | public | List categories (FR-B5) |
| POST | `/orders` | customer | Place an order (FR-C1) |
| POST | `/orders/{id}/pay` | owner | Pay from wallet (FR-C2) |
| PATCH | `/orders/{id}/status` | vendor/admin | Advance status (FR-C3) |
| GET | `/orders` | user (scoped) | List own/relevant orders (FR-C4) |
| POST | `/wallet/fund` | user | Simulated top-up (FR-D1) |
| GET | `/wallet` | user | Balance (FR-D) |
| GET | `/wallet/transactions` | user | History (FR-D3) |
| GET | `/admin/users` · `/admin/orders` | admin | Oversight (FR-E) |

**Standard error shape** (all 4xx/5xx):

{ "timestamp": "2026-01-15T10:22:03Z", "status": 404, "error": "Not Found",

  "message": "Product 9999 not found", "path": "/api/v1/products/9999",

  "fieldErrors": { } }

The full, live contract is published via **OpenAPI/Swagger UI** generated from the code.

---

## 9\. Architecture

- **Style:** layered (controller → service → repository), with DTO boundaries at the controller and an event-publishing seam at the service layer. Clean separation so the system can later be decomposed into services.  
- **Persistence:** relational (PostgreSQL) via JPA; correct relationship mapping; no N+1 on list endpoints.  
- **Eventing:** in-process domain events for v1, with a Kafka-ready path for order events (Epic F).  
- **Future direction:** the order, wallet, and catalog domains are the natural seams for extracting independently deployable microservices later.

Client ──HTTP──▶ API (controllers/DTOs) ──▶ Services (tx \+ events) ──▶ Repositories ──▶ PostgreSQL

                                                  │

                                                  └──events──▶ async consumers / Kafka

---

## 10\. Non-Functional Requirements

**Security (NFR-S)**

- `NFR-S1` All non-public endpoints require a valid access token.  
- `NFR-S2` Passwords stored only as BCrypt hashes; no secret (JWT signing key, DB password) is committed — all come from environment/config.  
- `NFR-S3` Authorization is enforced by **role** and by **ownership** (a user can never read or mutate another user's wallet/orders).  
- `NFR-S4` Tampered, expired, or malformed tokens are rejected with `401`.  
- `NFR-S5` Input is validated server-side; the server never trusts client-supplied prices, totals, or ownership claims.

**Performance & Scalability (NFR-P)**

- `NFR-P1` List endpoints are paginated; unbounded result sets are disallowed.  
- `NFR-P2` Product listing P95 \< 300 ms over a 10k-row seed.  
- `NFR-P3` The service is stateless (no server-side session), enabling horizontal scaling behind a load balancer.

**Reliability & Integrity (NFR-R)**

- `NFR-R1` Money operations are transactional and never leave partial state.  
- `NFR-R2` Concurrent wallet operations cannot overdraw a wallet (enforced via locking/optimistic concurrency).  
- `NFR-R3` Events fire only after successful commit (no notifications for rolled-back actions).

**Observability (NFR-O)**

- `NFR-O1` A health endpoint reports service status.  
- `NFR-O2` Requests are logged with method, path, status, and latency; errors are logged server-side without leaking to clients.

**Maintainability (NFR-M)**

- `NFR-M1` ≥ 80% test coverage on critical paths; unit \+ slice \+ one integration (real DB) test.  
- `NFR-M2` Consistent naming, layered structure, and a clean Git history.  
- `NFR-M3` API documented via OpenAPI; README explains run, test, and deploy.

**Portability (NFR-D)**

- `NFR-D1` Runs via `docker compose up` (app \+ database) locally.  
- `NFR-D2` Deployable to a managed platform (Railway/Render) with config via environment variables.

---

## 11\. Security Requirements (detail)

- **Authentication:** JWT access tokens (short TTL) \+ refresh tokens (long TTL, DB-backed, revocable).  
- **Authorization:** route-level role checks plus method-level ownership checks (`@PreAuthorize`\-style). Defends against Insecure Direct Object Reference (IDOR) — the highest-severity risk for a wallet system.  
- **Secrets:** signing keys and DB credentials injected from the environment; `.env` is git-ignored; a leaked secret must be rotated.  
- **Transport:** production deployment served over HTTPS (platform-provided).  
- **Least privilege:** `ADMIN` is provisioned out-of-band, never self-granted at registration.

---

## 12\. Data & Privacy

- Personal data stored is minimal: email, display name, hashed password.  
- Passwords and tokens are never returned in any API response or written to logs.  
- Wallet/transaction data is visible only to its owner and to admins for oversight.  
- Hard deletion is avoided for financial records; deactivation/soft-state is preferred for auditability.

---

## 13\. Assumptions, Constraints & Dependencies

- **Assumptions:** single currency (NGN); simulated funding stands in for a real gateway; clients are trusted only for input, never for authority.  
- **Constraints:** money in integer kobo; stateless services; PostgreSQL as the system of record.  
- **Dependencies:** JDK 17+, Spring Boot 3.x, PostgreSQL, Docker; Kafka for the stretch messaging path; a cloud platform for deployment.

---

## 14\. Release Plan / Milestones

Mapped to the course so the capstone grows as skills are taught:

| Milestone | Delivers | Aligns with |
| :---- | :---- | :---- |
| **M1 — Catalog** | Products, categories, search, pagination, DTOs, validation, errors, OpenAPI | Sessions 1–6 |
| **M2 — Secure** | Auth (JWT+refresh), RBAC, ownership on orders/wallet | Sessions 7–8 |
| **M3 — Money & Orders** | Orders, status lifecycle, atomic wallet payment, transactions | Sessions 5–8 |
| **M4 — Events** | Order events, async consumer, idempotent Kafka path (stretch) | Session 9 |
| **M5 — Ship** | Tests (incl. Testcontainers), Dockerize, deploy, README | Session 10 |

---

## 15\. Risks & Mitigations

| Risk | Impact | Mitigation |
| :---- | :---- | :---- |
| IDOR (cross-user data access) | Critical (money) | Mandatory ownership checks; security tests for cross-user access |
| Race condition overdraws wallet | High | Transactional payment \+ locking; concurrency test |
| Money rounding/float errors | High | Integer kobo only; no floating point |
| Event fired on rolled-back order | Medium | Publish only after commit (`AFTER_COMMIT`) |
| Secret leakage in Git | High | Env-based config; `.gitignore`; rotate on leak |
| Scope creep (real payments, reviews) | Medium | Explicit Non-Goals; defer to v2 |

---

## 16\. Definition of Done

A milestone is "done" when: all its acceptance criteria pass; protected endpoints reject unauthorized/cross-user access; money operations reconcile under concurrent tests; the relevant tests are green in a clean checkout; the API is documented in Swagger; and (for M5) the service runs via Docker and is reachable at a deployed URL with no secrets in the repository.

---

## 17\. Open Questions

- Should partial order fulfilment (per-line shipping) be supported, or whole-order only? *(v1: whole-order.)*  
- Should vendors see customer identities on orders, or only anonymized shipping info? *(Default: minimal PII to vendors.)*  
- Refund policy on `CANCELLED` paid orders — full only, or partial? *(v1: full refund.)*

---

## 18\. Glossary

- **Kobo** — minor unit of the Naira (1/100 NGN); the integer money unit used throughout.  
- **IDOR** — Insecure Direct Object Reference; accessing another user's resource by guessing/altering an id.  
- **RBAC** — Role-Based Access Control.  
- **Idempotent consumer** — processes the same event more than once without side effects.  
- **DTO** — Data Transfer Object; the API's shape, distinct from persistence entities.

---

*This PRD is the canonical specification for the MarketHub capstone. Engineers implement against the requirement IDs; facilitators assess against the acceptance criteria and the capstone rubric. Changes are versioned at the top of this document.*  
