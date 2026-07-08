# CONTRIBUTING.md

Git commit message conventions for this repository.  
All contributors are expected to follow these directives before opening a pull request.

---

## Table of Contents

- [Commit Format](#commit-format)  
- [Commit Types](#commit-types)  
- [Scopes](#scopes)  
- [Breaking Changes](#breaking-changes)  
- [Commit Body & Footer](#commit-body--footer)  
- [Rules](#rules)  
- [Branching Model](#branching-model)  
- [Examples](#examples)

---

## Commit Format

type(scope)\!: short description

\[optional body\]

\[optional footer(s)\]

| Part | Required | Description |
| :---- | :---- | :---- |
| `type` | ✅ Yes | Category of change (see types below) |
| `(scope)` | No | Module or layer affected, e.g. `(auth)`, `(wallet)` |
| `!` | No | Marks a **breaking change** — triggers a major version bump |
| `short description` | ✅ Yes | Imperative, lowercase, no period, max 72 characters |
| `body` | No | Explains *why*, not *what*. Separated from subject by a blank line |
| `footer` | No | `BREAKING CHANGE:`, `Closes #123`, `Co-authored-by:` |

---

## Commit Types

### `feat` — New Feature

Introduces a new feature visible to the user or API consumer.  
Triggers a **minor** version bump (`1.x.0`).

feat(auth): add JWT refresh token rotation

feat(products): expose bulk price update endpoint

feat: add order history pagination

---

### `fix` — Bug Fix

Fixes a bug in the codebase through the normal development cycle.  
Triggers a **patch** version bump (`1.0.x`).

fix(auth): handle expired tokens on /me endpoint

fix(wallet): prevent negative balance on concurrent transfers

fix(repo): correct N+1 query in OrderService

---

### `hotfix` — Critical Production Fix

A fix merged **directly to `main`** outside the normal release cycle.  
Reserved for production emergencies that require immediate deployment.  
Must be back-merged to `develop` after landing on `main`.

hotfix(payments): correct Paystack webhook signature validation

hotfix(auth): revoke leaked service account credentials

hotfix(wallet): resolve race condition in debit endpoint

⚠️ **Do not use `hotfix` for routine bugs.** If it can wait for the next release cycle, use `fix`.

---

### `refactor` — Code Restructuring

A code change that neither fixes a bug nor adds a feature.  
Restructuring, extracting, renaming — no behaviour change.

refactor(service): extract transfer logic into TransferDomainService

refactor: replace raw JDBC with Spring Data JPA

refactor\!(api): rename /v1/users to /v1/accounts

---

### `docs` — Documentation Only

Changes to documentation only — README, Swagger/OpenAPI annotations, Javadoc, wiki pages.  
No production code changes.

docs(api): add OpenAPI examples for transfer endpoint

docs: update local development setup instructions

docs(arch): add system architecture diagram to wiki

---

### `test` — Tests

Adding or updating tests. No changes to production logic.  
Use even when fixing a previously broken test.

test(wallet): add Testcontainers integration test for transfer

test(auth): cover JWT expiry edge case in JwtUtilTest

test: add @WebMvcTest suite for ProductController

---

### `chore` — Maintenance

Routine maintenance with no production code changes.  
Dependency bumps, build config, CI pipeline, tooling updates.

chore: bump spring-boot to 3.3.2

chore(ci): add Docker layer caching to GitHub Actions workflow

chore(deps): upgrade jackson-databind to 2.17.1

---

### `perf` — Performance Improvement

A code change that measurably improves performance.  
Caching, query optimisation, indexing — no behaviour change.

perf(products): add index on category\_id to fix full table scan

perf(cache): cache wallet balance in Redis with 60s TTL

perf(db): switch to batch inserts for transaction history writes

---

### `style` — Formatting

Formatting, whitespace, missing semicolons.  
Changes that do not affect meaning or logic whatsoever.

style: apply Google Java Format across all source files

style(controller): reorder imports per project convention

⚠️ `style` commits must contain **no logic changes**. Run your formatter in a dedicated commit, separate from any functional change.

---

### `revert` — Revert

Reverts a previous commit. The description must reference the commit being undone.

revert: feat(auth): add biometric unlock endpoint

revert: "chore: bump jackson to 2.17" — broke deserialization on nested types

Include the original commit hash in the body:

revert: feat(auth): add biometric unlock endpoint

Reverts commit a3f92bc.

Reason: caused 401s on all authenticated routes in staging.

---

## Scopes

Scope is **optional but strongly recommended**. Use the module, package, or architectural layer the change touches. Keep scope names short and consistent across the team.

| Scope | Use for |
| :---- | :---- |
| `auth` | Authentication, JWT, session management |
| `wallet` | Wallet balance, funding, debits |
| `payments` | Payment gateway integrations (Paystack, Flutterwave) |
| `products` | Product catalogue, categories, pricing |
| `orders` | Order creation, fulfilment, history |
| `users` | User profile, registration, account management |
| `notifications` | Email, SMS, push notifications |
| `api` | Global API config, versioning, routing |
| `db` | Database schema, migrations (Flyway/Liquibase) |
| `config` | Application config, environment properties |
| `ci` | GitHub Actions, pipelines, deployment scripts |
| `docker` | Dockerfile, docker-compose, container config |

Add new scopes as your codebase grows. Document them in this table.

---

## Breaking Changes

A breaking change is any change that forces API consumers or dependent services to update their code.

Mark breaking changes by appending `!` after the type (and scope), and add a `BREAKING CHANGE:` footer in the commit body.

refactor\!(api): rename /v1/users to /v1/accounts

BREAKING CHANGE: the /v1/users/\* route namespace has been removed.

All clients must update their base URL to /v1/accounts/\*.

The old route returns 301 redirects until 2025-09-01.

Breaking changes trigger a **major** version bump (`x.0.0`) in semantic versioning.

**Examples of breaking changes:**

- Removing or renaming a public API endpoint  
- Changing a response field name or type  
- Removing a previously required request parameter  
- Changing authentication scheme

---

## Commit Body & Footer

Use the body to explain **why** the change was made, not what it does — the diff already shows that.

fix(wallet): prevent negative balance on concurrent transfers

Two simultaneous debit requests were both passing the balance check

before either completed the write, resulting in negative balances.

Added a pessimistic row-level lock (SELECT FOR UPDATE) on the wallet

row inside the transaction to serialize concurrent debits.

Closes \#214

**Supported footers:**

| Footer | Purpose |
| :---- | :---- |
| `BREAKING CHANGE: <description>` | Signals a breaking change |
| `Closes #<issue>` | Links and closes a GitHub issue on merge |
| `Fixes #<issue>` | Links and closes a bug report |
| `Refs #<issue>` | References an issue without closing it |
| `Co-authored-by: Name <email>` | Credits a co-author (e.g. pair programming) |

---

## Rules

1. **Imperative mood.** Write `add`, not `added` or `adds`. The subject line completes the sentence: *"This commit will…"*  
     
2. **Lowercase subject.** The description starts with a lowercase letter: `fix(auth): handle expired tokens`, not `Fix(auth): Handle expired tokens`.  
     
3. **No period at the end** of the subject line.  
     
4. **72-character limit** on the subject line. Use the body for anything longer.  
     
5. **One logical change per commit.** If you find yourself writing "and" in the subject line, split it into two commits.  
     
6. **`hotfix` is an emergency lane.** It bypasses the normal PR → `develop` flow and lands directly on `main`. Never use it for a bug that can wait for the next sprint.  
     
7. **Never commit directly to `main` or `develop`.** All changes go through a feature branch and pull request — except `hotfix`, which goes to `main` directly and is then back-merged to `develop`.  
     
8. **`style` and logic never mix.** Run formatters in a dedicated commit before or after any functional change.  
     
9. **Reference what you revert.** A `revert` commit must include the original commit hash and a reason in the body.  
     
10. **`BREAKING CHANGE` footer is mandatory** whenever `!` is used. The `!` alone is not enough for automated tooling to generate changelogs correctly.

---

## Branching Model

main          ←── production. tagged releases only.

  │

  └── develop ←── integration branch. all PRs target here.

        │

        ├── feat/wallet-transfer

        ├── fix/jwt-expiry-handling

        ├── chore/bump-spring-boot

        └── refactor/extract-transfer-domain-service

**Normal flow:**

1. Branch from `develop` — name the branch after the commit type and a short slug:  
     
   git checkout \-b feat/wallet-transfer develop  
     
2. Commit with the conventions above. Push and open a PR against `develop`.  
     
3. At least one peer review before merge. **Squash-merge** is preferred to keep history linear.  
     
4. Release: merge `develop` → `main`. Tag the release commit:  
     
   git tag \-a v1.4.0 \-m "release: v1.4.0"  
     
   git push origin v1.4.0

**Hotfix flow:**

main ──→ hotfix/paystack-signature ──→ main  (tag v1.4.1)

                                    └──→ develop (back-merge)

git checkout \-b hotfix/paystack-signature main

\# make the fix

git commit \-m "hotfix(payments): correct Paystack webhook signature validation"

\# PR → main, merge, tag, then:

git checkout develop && git merge main

---

## Examples

Complete commit messages showing all conventions in practice.

feat(auth): add role-based access control to wallet endpoints

Users with ROLE\_USER can only access their own wallet.

Admins with ROLE\_ADMIN can read any wallet.

Closes \#87

fix(db): add missing index on transactions.wallet\_id

Full table scans on the transactions table were causing p99 latency

spikes of 4s+ on the /history endpoint under load.

Refs \#102

hotfix(payments): correct currency conversion rounding error

Integer division was truncating sub-kobo amounts on NGN→USD

conversions. Replaced with BigDecimal arithmetic throughout.

Fixes \#311

refactor\!(api): consolidate user and account endpoints under /v1/accounts

BREAKING CHANGE: /v1/users/\* is removed. Clients must migrate to

/v1/accounts/\*. A 301 redirect shim will remain until 2025-10-01.

chore(ci): cache Maven dependencies between GitHub Actions runs

Reduces average CI build time from 4m 20s to 1m 45s by restoring

\~/.m2 from cache on cache hit.

test(wallet): add Testcontainers integration test for concurrent transfers

Covers the race condition fixed in commit b2e91fa. Uses two threads

firing simultaneous debit requests to assert balance never goes negative.

Refs \#214

---

*Conventions based on the [Conventional Commits 1.0.0](https://www.conventionalcommits.org/en/v1.0.0/) specification.*  
