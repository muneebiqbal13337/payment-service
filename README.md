# Payment Service API

A production-grade REST API for processing payment transactions, built with Java Spring Boot and PostgreSQL.

## Features

- Create payment transactions with full validation
- Idempotency protection — duplicate requests return the original payment, never create duplicates
- Global exception handling with structured error responses
- Automatic timestamp and status management
- RESTful API design following industry standards

## Tech Stack

- **Java 21**
- **Spring Boot 3.5.11**
- **Spring Data JPA**
- **PostgreSQL 16**
- **Maven**
- **Lombok**

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/payments` | Create a new payment |
| GET | `/api/payments` | Retrieve all payments |
| GET | `/api/payments/{id}` | Retrieve payment by ID |
| PATCH | `/api/payments/{id}/status?status=` | Update payment status |

## Getting Started

### Prerequisites
- Java 21+
- PostgreSQL 16+
- Maven 3.9+

### Setup

1. Clone the repository
```
git clone https://github.com/muneebiqbal13337/payment-service.git
```

2. Create a PostgreSQL database
```sql
CREATE DATABASE payment_db;
```

3. Configure your database credentials in `src/main/resources/application.properties`
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/payment_db
spring.datasource.username=postgres
spring.datasource.password=your_password
```

4. Run the application
```
mvn spring-boot:run
```

The API will start on `http://localhost:8080`

## Example Request

### Create a Payment
```json
POST /api/payments
{
    "payerName": "John Doe",
    "payerEmail": "john@example.com",
    "amount": 150.00,
    "currency": "USD",
    "idempotencyKey": "pay-001"
}
```

### Response
```json
{
    "id": 1,
    "payerName": "John Doe",
    "payerEmail": "john@example.com",
    "amount": 150.00,
    "currency": "USD",
    "status": "PENDING",
    "idempotencyKey": "pay-001",
    "createdAt": "2026-03-05T23:44:38.7025"
}
```

## Idempotency

This API implements idempotency key validation. If the same `idempotencyKey` is sent multiple times, the original payment is returned without creating duplicates — exactly how production payment systems like Stripe work.

## Error Handling

All errors return structured JSON responses:
```json
{
    "timestamp": "2026-03-05T23:54:31",
    "status": 400,
    "error": "Validation Failed",
    "messages": [
        "payerEmail: Payer email must be a valid email address",
        "amount: Amount must be greater than zero"
    ]
}
```

## Valid Payment Statuses

| Status | Description |
|--------|-------------|
| PENDING | Payment initiated, awaiting processing |
| COMPLETED | Payment successfully processed |
| FAILED | Payment processing failed |
| REFUNDED | Payment has been refunded |
