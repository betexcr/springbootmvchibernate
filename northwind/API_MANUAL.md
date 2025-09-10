# Northwind Spring Boot API – Technical Manual

This manual documents how to run, secure, and use the Northwind API.

## Run
```bash
cd "/Users/albertomunoz/Documents/Code/springbootmvchibernate"
docker compose up -d
cd northwind
mvn spring-boot:run
```

- DB: jdbc:postgresql://localhost:5432/northwind (northwind/northwind)
- API: http://localhost:8080
- Migrations: V1 schema+data, V2 indexes

## Security
- JWT via `POST /api/auth/login` (demo roles: admin→ADMIN, staff→STAFF, others→USER)
- RBAC: Public GET for products/categories/suppliers; protected for customers/orders/employees; writes require JWT; `/actuator/**` ADMIN only
- CORS: http://localhost:3000
- Rate limiting: GETs 100/10s/IP
- ETags: enabled

## Endpoints

### Auth
- `POST /api/auth/login` {username,password} → access_token
- `POST /api/auth/refresh` Authorization: Bearer <token> → new access_token

### Products (public; filters)
- `GET /api/products` page,size,sort,q,categoryId,supplierId,minPrice,maxPrice,discontinued
- `GET /api/products/{id}`

### Categories (writes STAFF/ADMIN)
- `GET /api/categories` (pageable)
- `GET /api/categories/{id}`
- `POST /api/categories`
- `PUT /api/categories/{id}`
- `DELETE /api/categories/{id}`

### Suppliers (writes STAFF/ADMIN)
- `GET /api/suppliers` (pageable)
- `GET /api/suppliers/{id}`
- `POST /api/suppliers`
- `PUT /api/suppliers/{id}`
- `DELETE /api/suppliers/{id}`

### Customers (STAFF/ADMIN)
- `GET /api/customers` (pageable)
- `GET /api/customers/{id}`
- `POST /api/customers`
- `PUT /api/customers/{id}`
- `DELETE /api/customers/{id}`

### Employees (GET STAFF/ADMIN; writes ADMIN)
- `GET /api/employees` (pageable)
- `GET /api/employees/{id}`
- `POST /api/employees`
- `PUT /api/employees/{id}`
- `DELETE /api/employees/{id}`

### Orders (filters; DTO writes)
- `GET /api/orders` page,size,sort,customerId,employeeId,from,to
- `GET /api/orders/{id}`
- `POST /api/orders` OrderCreateDto
- `PUT /api/orders/{id}` OrderCreateDto
- `DELETE /api/orders/{id}`
- `GET /api/orders/{id}/summary`
- `GET /api/orders/summary/total?customerId=&from=&to=`

### Order details (nested; DTO writes)
- `GET /api/orders/{orderId}/details` (pageable)
- `GET /api/orders/{orderId}/details/{productId}`
- `POST /api/orders/{orderId}/details` OrderDetailCreateDto
- `PUT /api/orders/{orderId}/details/{productId}` OrderDetailCreateDto
- `DELETE /api/orders/{orderId}/details/{productId}`
- `GET /api/orders/{orderId}/details/summary/total`

### Actuator (ADMIN)
- `GET /actuator/health`, `GET /actuator/info`

## Curl cheatsheet
(See README body for full commands.)
- Login: `curl -X POST /api/auth/login -d '{"username":"admin","password":"x"}'`
- Products: `curl /api/products?page=0&size=10&sort=name,asc`
- Orders summary: `curl /api/orders/10248/summary`

## Benefits
- Flyway+indexes, JPA entities, DTO writes, JWT RBAC, filtering+pagination, ETag caching, rate limiting, CORS, actuator.

Angular Java, Python, NodeJS, React, Angular,