# Product Catalog API 
REST API for the Flexbox Ecommerce application.
The API provides customer-facing catalog access, admin catalog management, and admin analytics.

**API Version:** 1.0.0  
**Base URL:** `https://localhost:8080`
---
**Authored and maintained by:** Miranda Murphy  
**Last updated:** 2026-08-17
---

## API Overview

| Area            | Description                                      |
|-----------------|--------------------------------------------------|
| Catalog         | Customer-facing subscription box catalog         |
| Admin Catalog   | Manage subscription boxes, products, and prices  |
| Admin Analytics | View box costs, product costs, and monthly sales |

---

## Customer Catalog

### Get All Subscription Boxes

```http
GET /api/catalog/boxes
```

Returns the available subscription boxes.

**Response:** `200 OK`

```json
{
  "data": [
    {
      "id": 1,
      "name": "Essential Fitness Box",
      "description": "Entry-level box featuring a mix of protein snacks, hydration products, personal care items, and basic fitness accessories.",
      "imageUrl": "essential-fitness-box.jpg",
      "price": 29.99,
      "currency": "CAD",
      "active": true
    }
  ]
}
```

**Possible errors:**

- `404 Not Found`: Active price not found for the requested box.

---
### Get Subscription Box by ID

```http
GET /api/catalog/boxes/{boxId}
```

**Path Parameters**

| Parameter | Type    | Description         |
|-----------|---------|---------------------|
| `boxId`   | `int64` | Subscription box ID |

**Response:** `200 OK`

```json
{
  "id": 1,
  "name": "Essential Fitness Box",
  "description": "Entry-level box featuring a mix of protein snacks, hydration products, personal care items, and basic fitness accessories.",
  "imageUrl": "essential-fitness-box.jpg",
  "price": 29.99,
  "currency": "CAD",
  "active": true
}
```

**Possible errors:**

- `404 Not Found`: Subscription box does not exist.
- `404 Not Found`: Active price does not exist for the box.

---

# Admin Catalog

## Subscription Boxes

### Create Subscription Box

```http
POST /api/admin/boxes
Content-Type: application/json
```

**Request Body**

```json
{
  "name": "Monthly Box",
  "description": "A monthly box",
  "imagePath": "/images/monthly_box.jpg",
  "availableUnits": 10,
  "isActive": true
}
```

**Response:** `201 Created`

```json
{
  "id": 1,
  "name": "Monthly Box",
  "description": "A monthly box",
  "imagePath": "/images/monthly_box.jpg",
  "availableUnits": 10,
  "isActive": true
}
```

**Possible errors:**

- `409 Conflict`: A subscription box with the same name already exists.

---

### Deactivate Subscription Box

```http
DELETE /api/admin/boxes/{boxId}
```

**Path Parameters**

| Parameter | Type    | Description         |
|-----------|---------|---------------------|
| `boxId`   | `int64` | Subscription box ID |

**Response:** `200 OK`

Deactivates the specified subscription box.

**Possible errors:**

- `404 Not Found`: Subscription box does not exist.

---

## Products in Subscription Boxes

### Add Product to Subscription Box

```http
POST /api/admin/boxes/{boxId}/products
Content-Type: application/json
```

**Request Body**

```json
{
  "productId": 1,
  "quantity": 3
}
```

**Response:** `201 Created`

```json
{
  "subscriptionBoxId": 1,
  "productId": 1,
  "productName": "Sunscreen",
  "quantity": 3
}
```

**Possible errors:**

- `400 Bad Request`: Product is inactive.
- `404 Not Found`: Subscription box does not exist.
- `404 Not Found`: Product does not exist.
- `409 Conflict`: Product is already included in the subscription box.

---

## Subscription Box Prices

### Set Subscription Box Price

```http
POST /api/admin/boxes/{boxId}/prices
Content-Type: application/json
```

**Request Body**

```json
{
  "price": 19.99,
  "startsAt": "2026-01-01T00:00:00Z",
  "endsAt": null
}
```

**Response:** `200 OK`

```json
{
  "id": 1,
  "subscriptionBoxId": 1,
  "price": 19.99,
  "startsAt": "2026-01-01T00:00:00Z",
  "endsAt": null
}
```

**Possible errors:**

- `404 Not Found`: Subscription box does not exist.

---

# Admin Products

### Get All Products

```http
GET /api/admin/products
```

Returns a summary of products.

**Response:** `200 OK`

```json
{
  "data": [
    {
      "id": 4,
      "sku": "FF-PB-001",
      "name": "Chocolate Whey Protein Packet",
      "active": true
    },
    {
      "id": 5,
      "sku": "FF-PB-002",
      "name": "Vanilla Whey Protein Packet",
      "active": true
    }
  ]
}
```

---

### Get Product by ID

```http
GET /api/admin/products/{productId}
```

**Path Parameters**

| Parameter   | Type    | Description |
|-------------|---------|-------------|
| `productId` | `int64` | Product ID  |

**Response:** `200 OK`

```json
{
  "id": 56,
  "sku": "MW-PC-001",
  "brand": "MuscleWorks",
  "name": "Chocolate Protein Clusters",
  "description": "Chocolate-coated protein clusters",
  "category": {
    "id": 1,
    "name": "snacks"
  },
  "active": true,
  "costPerUnit": 1.1,
  "inventory": {
    "inStock": 0,
    "reserved": 0
  }
}
```

**Possible errors:**

- `404 Not Found`: Product does not exist.

---

### Deactivate Product

```http
DELETE /api/admin/products/{productId}
```

**Path Parameters**

| Parameter   | Type    | Description |
|-------------|---------|-------------|
| `productId` | `int64` | Product ID  |

**Response:** `200 OK`

Deactivates the specified product.

**Possible errors:**

- `404 Not Found`: Product does not exist.

---

# Admin Analytics

### Get Subscription Box Costs

```http
GET /api/admin/analytics/boxes
```

Returns the total product cost associated with each subscription box.

**Response:** `200 OK`

```json
{
  "data": [
    {
      "subscriptionBoxId": 4,
      "name": "Elite Athlete Box",
      "cost": 60.40
    },
    {
      "subscriptionBoxId": 3,
      "name": "Performance Box",
      "cost": 37.70
    }
  ]
}
```

---

### Get Product Costs for All Boxes

```http
GET /api/admin/analytics/boxes/products
```

Returns product-level cost information for subscription boxes.

**Response:** `200 OK`

```json
{
  "data": [
    {
      "subscriptionBoxId": 1,
      "productId": 8,
      "boxName": "Essential Fitness Box",
      "brand": "PowerBar Pro",
      "productName": "PB Protein Crunch Bar",
      "categoryId": 1,
      "categoryName": "snacks",
      "quantity": 2,
      "productCost": 2.20
    }
  ]
}
```

---

### Get Product Costs for a Box

```http
GET /api/admin/analytics/boxes/{boxId}/products
```

**Path Parameters**

| Parameter | Type    | Description         |
|-----------|---------|---------------------|
| `boxId`   | `int64` | Subscription box ID |

Returns the products, quantities, and costs associated with the specified box.

---

### Get Monthly Sales

```http
GET /api/admin/analytics/sales
```

Returns monthly sales data for subscription boxes.

**Response:** `200 OK`

```json
{
  "month": "2026-01-01T00:00:00Z",
  "subscriptionBoxId": 1,
  "boxName": "Essential Fitness Box",
  "unitsSold": 10,
  "grossRevenue": 299.90,
  "productCost": 139.50,
  "grossProfit": 160.40
}
```

---

# HTTP Status Codes

| Status            | Meaning                                                   |
|-------------------|-----------------------------------------------------------|
| `200 OK`          | Request completed successfully                            |
| `201 Created`     | Resource was successfully created                         |
| `400 Bad Request` | Request violates a business rule or contains invalid data |
| `404 Not Found`   | Requested resource does not exist                         |
| `409 Conflict`    | Request conflicts with an existing resource               |

---

## Notes

- Monetary values use a numeric/decimal format.
- IDs use `int64`.
- Timestamps use ISO 8601 `date-time` format.
- Nullable fields may return `null`.
