# Wallet Features — CRUD Implementation Plan

**Scope:** Wallet CRUD operations from Epic D of the [MarketHub PRD](MarketHub_PRD.md) — `FR-D1` (fund wallet / read balance) and `FR-D3` (transaction history).

This plan tells you *what to build and in what order*. It deliberately does not hand you the code — the PRD's acceptance criteria are the checklist your review will run against.

---

## 0. Read first

- PRD §1.4 — **money is integer kobo, never floating point.** Your wallet balance and transaction amounts are `Long` kobo.
- PRD §7 — domain model: `Wallet` owns `balanceKobo`, and has many `WalletTransaction` entries.
- PRD §11 — IDOR is the highest-severity risk for a wallet system. Every endpoint you build must be ownership-scoped.

**Ownership model:** Each `Wallet` is scoped to a user id. Endpoints that read or modify a wallet require the requesting user id to match the wallet's owner — write the test that tries to access another user's wallet and asserts it fails.

---

## 1. Phase 1 — Domain model & persistence

**Deliverables**

| Item | Notes |
| :--- | :--- |
| `Wallet` entity | userId (String or UUID), `balanceKobo` (`Long`, never null, default 0), `createdAt`/`updatedAt` |
| `WalletTransaction` entity | many-to-one `Wallet`, `type` (`CREDIT`/`DEBIT` enum), `amountKobo`, `reference` (free text, e.g. an order id later), timestamp |
| Repositories | Spring Data JPA, following `ProductRepository` |

**Definition of done:** entities map cleanly (app boots, schema created), a repository slice test can save and reload a wallet with transactions.

---

## 2. Phase 2 — Fund wallet & read balance (FR-D1)

**Deliverables**

- `WalletService` interface + impl (follow the `ProductService` / `ProductServiceImpl` pattern).
- `POST /api/v1/users/{userId}/wallet/fund` — credits the wallet by a **positive** amount and records a `CREDIT` transaction, ownership-scoped.
- `GET /api/v1/users/{userId}/wallet` — returns the balance, ownership-scoped.
- DTOs at the controller boundary (`FundWalletRequest`, `WalletResponse`) — never expose entities.

**Acceptance criteria (from FR-D1)**

- [ ] Funding credits *my own* wallet and records a `CREDIT` transaction in the same database transaction.
- [ ] Zero or negative amounts are rejected with `400` and a field-level error (use Bean Validation).
- [ ] Funding another user's wallet is impossible — the endpoint returns `403` or fails with clear ownership validation.
- [ ] Balance is persisted correctly.

---

## 3. Phase 3 — Transaction history (FR-D3)

**Deliverables**

- `GET /api/v1/users/{userId}/wallet/transactions` — paginated (`Pageable`), time-ordered (newest first), ownership-scoped.
- Response includes amount, type, reference, timestamp, plus page metadata.

**Acceptance criteria (from FR-D3, NFR-P1)**

- [ ] Results are paginated — an unbounded list is a failing review.
- [ ] A user can never see another user's transactions.
- [ ] Ordering is stable and documented (e.g. `timestamp DESC`).

---

## 4. Testing expectations

Follow the patterns already in the repo:

- **Unit tests** — Mockito, like `ProductServiceTest`: service logic with mocked repositories.
- **Integration tests** — `@SpringBootTest` + MockMvc, like `ProductControllerTest`: full request → database flow, `@Transactional` rollback.

Target: every acceptance checkbox above has at least one test proving it.

---

## 5. Workflow

- Branch from `dev` per the README branching model: `feat/wallet-domain-model`, `feat/wallet-funding`, `feat/wallet-history`.
- Commit per the README conventions, scope `(wallet)`: e.g. `feat(wallet): create wallet and transaction entities`, `feat(wallet): add fund endpoint with transaction recording`.
- Reference requirement IDs (`FR-D1`, `FR-D3`) in PR descriptions so review can trace code to spec.
- One phase per PR. Small PRs get reviewed; big ones get bounced.

## 6. Suggested order & pacing

| Phase | Depends on | Suggested effort |
| :--- | :--- | :--- |
| 1 — Domain model | nothing | 1–2 sessions |
| 2 — Fund & balance | Phase 1 | 1–2 sessions |
| 3 — History | Phase 1 | 1 session |

---

## 7. Key principles

- **Integer kobo only.** No floating point on money.
- **Ownership-scoped.** A user can only read/modify their own wallet.
- **Transactional consistency.** Balance updates and transaction records happen together, or not at all.
