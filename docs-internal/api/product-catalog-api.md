# Product Catalog API Endpoints

**Authored and maintained by:** Miranda Murphy  
**Last updated:** 2026-08-04

Base URL:

```text
http://localhost:8080
```

---

# Product Catalog API

## Get All Products

### Endpoint

```http
GET /products
```

### Request

```bash
curl -X GET http://localhost:8080/products
```

### Response

```json
{
  "data": [
    {
      "id": 4,
      "category": {
        "id": 1,
        "name": "snacks"
      },
      "name": "Chocolate Whey Protein Packet",
      "description": "Single-serve whey protein powder, 25g protein",
      "brand": "FitFuel"
    },
    {
      "id": 5,
      "category": {
        "id": 1,
        "name": "snacks"
      },
      "name": "Vanilla Whey Protein Packet",
      "description": "Single-serve whey protein powder, 25g protein",
      "brand": "FitFuel"
    },
    {
      "id": 6,
      "category": {
        "id": 1,
        "name": "snacks"
      },
      "name": "Strawberry Whey Protein Packet",
      "description": "Single-serve whey protein powder, 25g protein",
      "brand": "FitFuel"
    },
    {
      "id": 7,
      "category": {
        "id": 1,
        "name": "snacks"
      },
      "name": "Recovery Drink Mix",
      "description": "Post-workout recovery drink with protein and electrolytes",
      "brand": "MuscleMaker"
    },
    {
      "id": 8,
      "category": {
        "id": 1,
        "name": "snacks"
      },
      "name": "PB Protein Crunch Bar",
      "description": "Chocolate peanut butter protein bar, 20g protein",
      "brand": "PowerBar Pro"
    }
  ]
}
```


## Get Product By ID

### Endpoint

```http
GET /products/{id}
```

### Request

```bash
curl -X GET http://localhost:8080/products/4
```

### Response

```json
{
  "id": 4,
  "name": "Chocolate Whey Protein Packet",
  "brand": "FitFuel",
  "description": "Single-serve whey protein powder, 25g protein",
  "category": {
    "id": 1,
    "name": "snacks"
  },
  "isActive": true
}
```


## Get All Subscription Boxes

### Endpoint

```http
GET /subscription-boxes
```

### Request

```bash
curl -X GET http://localhost:8080/subscription-boxes
```

### Response

```json
{
  "data": [
    {
      "id": 1,
      "name": "Essential Fitness Box",
      "price": {
        "amount": 29.99,
        "currency": "CAD"
      }
    },
    {
      "id": 2,
      "name": "Active Lifestyle Box",
      "price": {
        "amount": 49.99,
        "currency": "CAD"
      }
    },
    {
      "id": 3,
      "name": "Performance Box",
      "price": {
        "amount": 69.99,
        "currency": "CAD"
      }
    },
    {
      "id": 4,
      "name": "Elite Athlete Box",
      "price": {
        "amount": 99.99,
        "currency": "CAD"
      }
    }
  ]
}
```


## Get Subscription Box By ID

### Endpoint

```http
GET /subscription-boxes/{id}
```

### Request

```bash
curl -X GET http://localhost:8080/subscription-boxes/1
```

### Response

```json
{
  "id": 1,
  "name": "Essential Fitness Box",
  "description": "Entry-level box featuring a mix of protein snacks, hydration products, personal care items, and basic fitness accessories.",
  "price": {
    "amount": 29.99,
    "currency": "CAD"
  },
  "products": [
    {
      "productId": 8,
      "productName": "PB Protein Crunch Bar",
      "brand": "PowerBar Pro",
      "quantity": 1
    },
    {
      "productId": 9,
      "productName": "Protein Cookie",
      "brand": "ActiveLife",
      "quantity": 1
    }
  ]
}
```

---

# Admin API

## Get Admin Analytics Dashboard

### Endpoint

```http
GET /admin/analytics/dashboard
```

### Request

```bash
curl -X GET http://localhost:8080/admin/analytics/dashboard
```

---

# Service Access

| Service                | Address                 |
|------------------------|-------------------------|
| Spring Boot API        | `http://localhost:8080` |
| Application PostgreSQL | `localhost:5434`        |
| Sandbox PostgreSQL     | `localhost:5433`        |