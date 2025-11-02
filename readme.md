# Currency Rate Service

## Overview
**Currency Rate Service** is a Spring Boot application that provides an endpoint to retrieve and combine **fiat** and **cryptocurrency** exchange rates from external mock APIs.  
If the external services are unavailable, the service falls back to previously stored rates in the database.

---

## Features
- Fetches currency rates from **two external mock APIs**:
    - Fiat currency rates
    - Cryptocurrency rates
- Asynchronous, non-blocking HTTP requests using **Spring WebClient**
- Automatic fallback to the **latest stored data** in case of API failure or timeout
- Data persistence using **JPA** and a relational database (PostgreSQL)

---

## API Specification

### **GET /currency-rates**

### ✅ Success (200) - when both APIs respond successfully:
```json
{
  "fiat": [
    { "currency": "USD", "rate": 123.33 },
    { "currency": "EUR", "rate": 1232.22 }
  ],
  "crypto": [
    { "currency": "BTC", "rate": 1232.22 },
    { "currency": "ETH", "rate": 234.56 }
  ]
}
```
### ⚠️ Partial Data Retrieved (e.g., Crypto API failed)
```json
{
  "fiat": [
    { "currency": "USD", "rate": 123.33 },
    { "currency": "EUR", "rate": 1232.22 }
  ],
  "crypto": []
}
```
### ❌ No Data Retrieved (API failure + empty DB)
```json
{
  "fiat": [],
  "crypto": []
}
```

## Technologies Used

###  Backend
- **Java 21**
- **Spring Boot 3.5.7**
    - `spring-boot-starter-web` — REST API endpoints
    - `spring-boot-starter-data-jpa` — data persistence layer
- **OpenFeign** — for HTTP client communication with external APIs (`feign-core`, `feign-okhttp`, `feign-gson`, `feign-slf4j`)
- **MapStruct** — automatic DTO ↔ entity mapping
- **Liquibase** — database change management and version control
- **PostgreSQL** — main relational database
- **Lombok** — boilerplate code reduction (getters, setters, builders, etc.)

### Testing
- **Spring Boot Starter Test** — testing utilities and JUnit support
- **Testcontainers** — running PostgreSQL in isolated Docker containers for integration tests

