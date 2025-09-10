# Northwind Spring Boot API – Technical Manual

This manual documents how to run, secure, and use the Northwind API.

## 🚀 Live Deployment
**Production API**: https://northwind-api-815129345561.us-central1.run.app
**Swagger UI**: https://northwind-api-815129345561.us-central1.run.app/swagger-ui/index.html

## 🏗️ Architecture & Technologies

### **Backend Stack**
- **Spring Boot 3.3.2** - Modern Java framework with auto-configuration
- **Spring Security** - JWT-based authentication and RBAC authorization
- **Spring Data JPA** - Data access layer with Hibernate ORM
- **Hibernate** - Object-relational mapping with lazy loading optimization
- **Flyway** - Database migration and version control
- **PostgreSQL** - Robust relational database with advanced features

### **Cloud Infrastructure**
- **Google Cloud Run** - Serverless container platform for auto-scaling
- **Google Cloud SQL** - Managed PostgreSQL database service
- **Google Cloud Build** - CI/CD pipeline for automated deployments
- **Docker** - Containerization for consistent deployment environments

### **Security & Performance**
- **JWT Authentication** - Stateless token-based security
- **CORS Configuration** - Cross-origin resource sharing for web clients
- **Rate Limiting** - API protection against abuse (100 requests/10s per IP)
- **ETag Caching** - HTTP caching for improved performance
- **Lazy Loading** - Optimized database queries with @JsonIgnore

## 🏃‍♂️ Local Development
```bash
cd "/Users/albertomunoz/Documents/Code/springbootmvchibernate"
docker compose up -d
cd northwind
mvn spring-boot:run
```

- **Local DB**: jdbc:postgresql://localhost:5432/northwind (northwind/northwind)
- **Local API**: http://localhost:8080
- **Migrations**: V1 schema+data, V2 indexes, V3 users

## 🔐 Security
- **JWT Authentication** via `POST /api/auth/login` (demo roles: admin→ADMIN, staff→STAFF, others→USER)
- **RBAC Authorization**: Public GET for products/categories/suppliers; protected for customers/orders/employees; writes require JWT; `/actuator/**` ADMIN only
- **CORS**: Configured for all origins (`*`) for production deployment
- **Rate Limiting**: GETs 100/10s/IP to prevent abuse
- **ETags**: Enabled for HTTP caching optimization

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

## 🧪 Production API Testing

### **Public Endpoints (No Authentication Required)**
```bash
# Get all categories
curl "https://northwind-api-815129345561.us-central1.run.app/api/categories"

# Get products with pagination and filtering
curl "https://northwind-api-815129345561.us-central1.run.app/api/products?page=0&size=10&sort=name,asc"

# Get suppliers
curl "https://northwind-api-815129345561.us-central1.run.app/api/suppliers"

# Get specific product by ID
curl "https://northwind-api-815129345561.us-central1.run.app/api/products/1"
```

### **Authentication Required Endpoints**
```bash
# Login to get JWT token
curl -X POST "https://northwind-api-815129345561.us-central1.run.app/api/auth/login" \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin"}'

# Use token for protected endpoints (replace YOUR_TOKEN)
curl -H "Authorization: Bearer YOUR_TOKEN" \
  "https://northwind-api-815129345561.us-central1.run.app/api/customers"

# Get order summary
curl -H "Authorization: Bearer YOUR_TOKEN" \
  "https://northwind-api-815129345561.us-central1.run.app/api/orders/10248/summary"
```

### **Local Development Testing**
```bash
# Local API testing
curl "http://localhost:8080/api/categories"
curl -X POST "http://localhost:8080/api/auth/login" -d '{"username":"admin","password":"admin"}'
```

## 🎯 Key Features & Benefits

### **Database & Data Management**
- **Flyway Migrations** - Version-controlled database schema with V1 (schema+data), V2 (indexes), V3 (users)
- **JPA Entities** - Object-relational mapping with optimized lazy loading
- **DTO Pattern** - Clean data transfer objects for API requests/responses
- **Advanced Filtering** - Query parameters for products, orders, and pagination

### **Security & Performance**
- **JWT RBAC** - Role-based access control with stateless authentication
- **Rate Limiting** - API protection against abuse and DDoS attacks
- **ETag Caching** - HTTP caching for improved performance and reduced bandwidth
- **CORS Support** - Cross-origin resource sharing for web applications

### **Cloud & DevOps**
- **Serverless Deployment** - Auto-scaling on Google Cloud Run
- **Managed Database** - Cloud SQL PostgreSQL with automatic backups
- **CI/CD Pipeline** - Automated builds and deployments via Cloud Build
- **Containerization** - Docker for consistent deployment environments

### **API Design**
- **RESTful Architecture** - Clean, intuitive API endpoints
- **OpenAPI Documentation** - Interactive Swagger UI for API exploration
- **Comprehensive CRUD** - Full create, read, update, delete operations
- **Advanced Queries** - Filtering, sorting, pagination, and search capabilities