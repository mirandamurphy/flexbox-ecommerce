# Flexbox

A full-stack subscription box e-commerce application. Customers browse subscription boxes, add them to a cart, check out through Stripe, and manage their orders. Built for CST8319, Group 5.

## Tech Stack

**Backend:** Java 25, Spring Boot 4.1.0, PostgreSQL, Flyway, Spring Security, Stripe API, Docker Compose, Testcontainers, JUnit 5

**Frontend:** React 19, Vite, React Router, Axios

## Project Structure

```
flexbox-backend/          Spring Boot backend
  src/main/java/com/flexbox/backend/
    auth/                 Login, registration
    security/             JWT, password hashing, user details
    user/                 User, Role, Token entities
    cart/                 Cart, CartItem, cart service and controller
    catalog/              Product, SubscriptionBox, catalog service and controller
    order/                Order, CheckoutSession, checkout service and controller
    payment/               Payment, Invoice entities
    webhook/               Stripe webhook handling
    address/               Address entity
    marketing/             Newsletter, marketing consent
    subscription/           Subscription plan
    config/                 Global exception handling
  src/main/resources/
    db/migration/            Flyway schema migrations
    application.properties   App configuration
  src/test/                  Unit and integration tests

src/                        React frontend (Vite)
  pages/                    Page components
  components/               Reusable UI components
  context/                  Cart and Auth state
  services/                 API client and per-feature service functions

database/app-init/          Postgres role and permission setup scripts
docker-compose.yaml         Local development environment
.env.example                Environment variable template
```

## Running the Backend

### Prerequisites

- Java 25
- Docker Desktop
- A Stripe test account (free): https://dashboard.stripe.com/register

### Setup

1. Copy the environment template and fill in real values:

   ```
   cp .env.example .env
   ```

   Set a database password of your choice, and add your Stripe test secret key from
   https://dashboard.stripe.com/test/apikeys. `JWT_SECRET` can be any random string
   at least 32 characters long.

2. Start the database and backend with Docker Compose:

   ```
   docker compose up -d --build
   ```

   This starts Postgres, runs the Flyway migrations automatically, and starts the
   Spring Boot application on port 8080.

3. To run the backend directly instead of through Docker (useful for active
   development), start only the database first:

   ```
   docker compose up -d app-db
   ```

   Then run the backend from `flexbox-backend/`:

   ```
   ./mvnw spring-boot:run
   ```

### Running Tests

From `flexbox-backend/`:

```
./mvnw clean test
```

Tests use Testcontainers, which starts its own throwaway Postgres container
automatically. Docker must be running, but the app's own database does not need
to be up first.

## Running the Frontend

### Prerequisites

- Node.js 20+

### Setup

From the project root:

```
npm install
npm run dev
```

The frontend runs on `http://localhost:3000` by default and expects the backend
to be running on `http://localhost:8080`.

To build for production:

```
npm run build
```

## API Overview

The backend exposes REST endpoints under `/api`. Key endpoints:

| Method | Path | Purpose |
|---|---|---|
| GET | `/api/products` | List products |
| GET | `/api/subscription-boxes` | List subscription boxes |
| GET | `/api/cart?userId={id}` | Get the current user's active cart |
| POST | `/api/cart/items?userId={id}` | Add an item to the cart |
| PATCH | `/api/cart/items/{id}` | Update an item's quantity |
| DELETE | `/api/cart/items/{id}` | Remove an item from the cart |
| POST | `/api/checkout?userId={id}` | Start a Stripe checkout session |
| POST | `/api/checkout/{orderId}/retry` | Retry a failed or expired checkout |
| POST | `/api/auth/register` | Register a new account |
| POST | `/api/auth/login` | Log in |

`userId` is currently passed as a request parameter rather than read from an
authenticated session. The JWT security filter chain that would read it from
the login token automatically is not wired up yet, this is a known next step.

## Known Limitations (as of this submission)

- The frontend uses mock data for cart and checkout, it is not yet connected
  to the real backend endpoints listed above.
- There is no JWT authentication filter enforcing login on protected routes yet.
- Admin analytics endpoints are not implemented yet.

## Team

| Role | Member |
|---|---|
| Data Engineer | Miranda Murphy |
| Security Engineer | Nancy Mikhail |
| Frontend Engineer | Yanzhen Zhang |
| Integration Engineer | Nabil Ait Belkas |
