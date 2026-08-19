# Flexbox


A full-stack subscription box e-commerce application built for **CST8319: Software Development Project**.

Customers can browse subscription boxes and products, manage their cart, complete checkout through Stripe, and manage their orders.

## Documentation

- [Database Documentation](docs/database/DATABASE_README.md)
- [Catalog API Documentation](docs/api/product-catalog-api.md)


### Backend

- Java 25
- Spring Boot 4.1.0
- PostgreSQL 18
- Flyway
- Spring Security
- Stripe API
- Docker Compose
- Testcontainers
- JUnit 5
- Mockito

### Frontend

- React 19
- Vite
- React Router
- Axios

## Project Structure

```text
flexbox/
├── database/
│   └── app-init/
│
├── docs/
│   ├── api/
│   └── database/
│       └── schema/
│
├── flexbox-backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/flexbox/backend/
│   │   │   └── resources/
│   │   │       └── db/migration/
│   │   └── test/
│   │       ├── java/com/flexbox/backend/
│   │       └── resources/
│   └── pom.xml
│
├── http-requests/
├── public/
├── src/
│   ├── assets/
│   ├── components/
│   ├── context/
│   ├── data/
│   ├── layouts/
│   ├── pages/
│   ├── routes/
│   └── services/
│
├── .gitignore
├── docker-compose.yaml
├── package.json
├── vite.config.js
└── README.md
```


### Backend Package Structure

```text
com.flexbox.backend/
├── address/          # Address management
├── admin/
│   ├── analytics/    # Admin analytics
│   └── catalog/      # Admin catalog management
├── auth/             # Authentication
├── cart/             # Shopping cart
├── catalog/
│   ├── box/          # Subscription boxes
│   └── product/      # Products
├── common/            # Shared DTOs and exceptions
├── config/            # Application configuration
├── marketing/        # Marketing functionality
├── order/             # Order management
├── payment/           # Payment processing
├── security/          # Spring Security configuration
├── stripe/            # Stripe integration
├── subscription/      # Subscription management
├── user/              # User management
└── webhook/           # Webhook handling
```

### Frontend Structure

```text
src/
├── assets/            # Static assets
├── components/        # Reusable UI components
├── context/           # React context providers
├── data/              # Frontend data and configuration
├── layouts/           # Page layouts
├── pages/              # Application pages
├── routes/             # Client-side routing
└── services/           # API and service integrations
```


## Getting Started

### Prerequisites

- Java 25
- Node.js 20+
- Docker Desktop
- Stripe test account ([register here](https://dashboard.stripe.com/register))

### Environment Configuration

Copy the environment template:

```bash
cp .env.example .env
```

Configure the required environment variables in `.env`.

For Stripe test credentials, use the [Stripe API keys dashboard](https://dashboard.stripe.com/test/apikeys).

`JWT_SECRET` should be a randomly generated secret of at least 32 characters.



## Running the Application

### Option 1: Run Backend with Docker Compose

From the project root:

```bash
docker compose up --build
```

This starts:

- PostgreSQL
- Flyway database migrations
- Spring Boot backend

The backend is available at:

```text
http://localhost:8080
```

To run the containers in the background:

```bash
docker compose up -d --build
```

### Option 2: Run Backend Locally

Start only PostgreSQL:

```bash
docker compose up -d app-db
```

Then run the backend from `flexbox-backend/`:

```bash
./mvnw spring-boot:run
```

### Running Tests

From `flexbox-backend/`:

```bash
./mvnw clean test
```

Integration tests use **Testcontainers** to create a temporary PostgreSQL database. Docker must be running, but the application's PostgreSQL container does not need to be running.

## Running the Frontend

From the project root:

```bash
npm install
npm run dev
```

The frontend runs at:

```text
http://localhost:5173
```

It expects the backend to be available at:

```text
http://localhost:8080
```

### Production Build

```bash
npm run build
```

## API Overview

The backend exposes REST endpoints under `/api`. Key endpoints:

| Method | Path                                          | Purpose                                       |
|--------|-----------------------------------------------|-----------------------------------------------|
| GET    | `/api/catalog/boxes`                          | Get all boxes                                 |
| GET    | `/api/catalog/boxes/{boxId}`                  | Get one box                                   |
| GET    | `/api/admin/products`                         | Admin: get all products                       |
| GET    | `/api/admin/products/{productId}`             | Admin: get one product                        |
| GET    | `/api/admin/analytics/boxes`                  | Admin: get box costs                          |
| GET    | `/api/admin/analytics/boxes/products`         | Admin: get product costs for boxes            |
| GET    | `/api/admin/analytics/boxes/{boxId}/products` | Admin: get product costs for one box          |
| GET    | `/api/admin/analytics/sales`                  | Admin: get monthly sales summary (all months) |
| GET    | `/api/cart?userId={id}`                       | Get the current user's active cart            |
| POST   | `/api/cart/items?userId={id}`                 | Add an item to the cart                       |
| PATCH  | `/api/cart/items/{id}`                        | Update an item's quantity                     |
| POST   | `/api/admin/boxes`                            | Admin: create a box                           |
| POST   | `/api/admin/boxes/{boxId}/products`           | Admin: Add product to box                     |
| POST   | `/api/admin/boxes/{boxId}/prices`             | Admin: Set box price                          |
| POST   | `/api/checkout?userId={id}`                   | Start a Stripe checkout session               |
| POST   | `/api/checkout/{orderId}/retry`               | Retry a failed or expired checkout            |
| POST   | `/api/auth/register`                          | Register a new account                        |
| POST   | `/api/auth/login`                             | Log in                                        |
| DELETE | `/api/admin/boxes/{boxId}`                    | Admin: Deactive a box                         |
| DELETE | `/api/admin/products/{productId}`             | Admin: Deactive a product                     |
| DELETE | `/api/cart/items/{id}`                        | Remove an item from the cart                  |


For detailed API documentation, see:
- [Catalog API Documentation](docs/api/product-catalog-api.md).

## Known Limitations

- The JWT filter chain does not enforce authentication yet, `SecurityConfig` currently permits all traffic so the application is usable while that piece is still being built. Role-based access control has no enforcement mechanism in place either.
- `userId` is passed as a request parameter on cart and checkout endpoints rather than read from the authenticated session, since there is no JWT filter yet to provide it.
- Newsletter subscription opt-in is not implemented. Order confirmation email is implemented and sends automatically on successful checkout.
- The frontend has no automated test coverage yet.

## Team

| Role                 | Member           |
|----------------------|------------------|
| Data Engineer        | Miranda Murphy   |
| Security Engineer    | Nancy Mikhail    |
| Frontend Engineer    | Yanzhen Zhang    |
| Integration Engineer | Nabil Ait Belkas |