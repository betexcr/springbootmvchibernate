# Northwind API

A comprehensive Spring Boot REST API for the Northwind database, implementing modern security practices, performance optimizations, and enterprise-grade features.

## 🌐 Live Deployment

**🚀 Production API**: https://northwind-api-815129345561.us-central1.run.app  
**📚 Swagger Documentation**: https://northwind-api-815129345561.us-central1.run.app/swagger-ui/index.html

### Quick Test
```bash
# Test public endpoints (no authentication required)
curl "https://northwind-api-815129345561.us-central1.run.app/api/categories"
curl "https://northwind-api-815129345561.us-central1.run.app/api/products?size=5"
curl "https://northwind-api-815129345561.us-central1.run.app/api/suppliers"
```

## 🚀 Features

### Core Functionality
- **Complete CRUD Operations** for all Northwind entities (Products, Categories, Suppliers, Customers, Employees, Orders, Order Details)
- **RESTful API Design** with proper HTTP methods and status codes
- **Pagination & Sorting** for all list endpoints
- **Search & Filtering** capabilities using JPA Specifications
- **Data Transfer Objects (DTOs)** for clean API contracts
- **Comprehensive Validation** with detailed error messages

### Security & Authentication
- **JWT-based Authentication** with stateless session management
- **Role-Based Access Control (RBAC)** with USER, STAFF, and ADMIN roles
- **Method-level Security** using `@PreAuthorize` annotations
- **CORS Configuration** for cross-origin requests
- **Rate Limiting** for public endpoints
- **Actuator Security** with role-based access control
- **BCrypt Password Hashing** for secure password storage

### Performance & Optimization
- **ETag Caching** for GET endpoints to reduce bandwidth
- **Database Indexes** on frequently queried columns
- **Connection Pooling** with HikariCP
- **Lazy Loading** for entity relationships
- **Query Optimization** with JPA Specifications

### Data Management
- **Flyway Database Migrations** for version-controlled schema changes
- **PostgreSQL Database** with full Northwind dataset
- **Entity Relationships** properly mapped with JPA annotations
- **Composite Keys** for order details
- **Audit Fields** for tracking data changes

### API Documentation & Testing
- **OpenAPI/Swagger UI** for interactive API documentation
- **Comprehensive Integration Tests** using Testcontainers
- **Unit Tests** with MockMvc for controller testing
- **Postman Collection** for easy API exploration
- **Technical Manual** with detailed usage examples

### DevOps & CI/CD
- **GitHub Actions** for continuous integration
- **Docker Support** with docker-compose for local development
- **Maven Build System** with proper dependency management
- **Testcontainers** for integration testing with real databases

### Cloud Infrastructure & Deployment
- **Google Cloud Run** - Serverless container platform for auto-scaling
- **Google Cloud SQL** - Managed PostgreSQL database with automatic backups
- **Google Cloud Build** - CI/CD pipeline for automated deployments
- **Docker Containerization** - Consistent deployment across environments
- **Environment-based Configuration** - Secure database credentials via environment variables
- **Cloud SQL Connector** - Secure database connections without exposing credentials

## 🏗️ Architecture

### Technology Stack
- **Java 21** with Spring Boot 3.3.2
- **Spring MVC** for REST API development
- **Spring Data JPA** with Hibernate ORM
- **Spring Security** for authentication and authorization
- **PostgreSQL** as the primary database
- **Flyway** for database migrations
- **JWT** for stateless authentication
- **Testcontainers** for integration testing
- **Maven** for dependency management

### Project Structure
```
src/
├── main/
│   ├── java/com/example/northwind/
│   │   ├── entity/          # JPA entities
│   │   ├── repository/      # Data access layer
│   │   ├── service/         # Business logic layer
│   │   ├── controller/      # REST controllers
│   │   ├── dto/            # Data transfer objects
│   │   ├── security/       # Security configuration
│   │   ├── config/         # Application configuration
│   │   └── repository/spec/ # JPA specifications
│   └── resources/
│       ├── db/migration/   # Flyway migrations
│       └── application.yml # Application configuration
└── test/
    ├── java/com/example/northwind/
    │   ├── integration/    # Testcontainers integration tests
    │   └── *Test.java      # Unit tests
    └── resources/
        └── application-test.yml
```

## 🚀 Quick Start

### Prerequisites
- Java 21+
- Maven 3.6+
- Docker & Docker Compose
- PostgreSQL (or use Docker)

### 1. Clone and Setup
```bash
git clone <repository-url>
cd northwind
```

### 2. Start Database
```bash
docker-compose up -d
```

### 3. Run Application
```bash
mvn spring-boot:run
```

### 4. Access API
- **API Base URL**: http://localhost:8080/api
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **Actuator**: http://localhost:8080/actuator (Admin only)

## 🔐 Authentication

### Demo Users
| Username | Password | Role | Description |
|----------|----------|------|-------------|
| `admin` | `admin123` | ADMIN | Full access to all operations |
| `staff` | `staff123` | STAFF | Read/write access to business data |
| `user` | `user123` | USER | Read-only access |

### Getting JWT Token
```bash
# Production API
curl -X POST https://northwind-api-815129345561.us-central1.run.app/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'

# Local development
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
```

### Using JWT Token
```bash
# Production API
curl -H "Authorization: Bearer <your-jwt-token>" \
  https://northwind-api-815129345561.us-central1.run.app/api/products

# Local development
curl -H "Authorization: Bearer <your-jwt-token>" \
  http://localhost:8080/api/products
```

## 📚 API Endpoints

### Products
- `GET /api/products` - List all products (with pagination, sorting, filtering)
- `GET /api/products/{id}` - Get product by ID
- `POST /api/products` - Create new product (STAFF/ADMIN)
- `PUT /api/products/{id}` - Update product (STAFF/ADMIN)
- `DELETE /api/products/{id}` - Delete product (ADMIN)

**Query Parameters:**
- `q` - Search by product name
- `categoryId` - Filter by category
- `supplierId` - Filter by supplier
- `page` - Page number (default: 0)
- `size` - Page size (default: 20)
- `sort` - Sort field and direction (e.g., `name,asc`)

### Categories
- `GET /api/categories` - List all categories
- `GET /api/categories/{id}` - Get category by ID
- `POST /api/categories` - Create new category (STAFF/ADMIN)
- `PUT /api/categories/{id}` - Update category (STAFF/ADMIN)
- `DELETE /api/categories/{id}` - Delete category (ADMIN)

### Suppliers
- `GET /api/suppliers` - List all suppliers
- `GET /api/suppliers/{id}` - Get supplier by ID
- `POST /api/suppliers` - Create new supplier (STAFF/ADMIN)
- `PUT /api/suppliers/{id}` - Update supplier (STAFF/ADMIN)
- `DELETE /api/suppliers/{id}` - Delete supplier (ADMIN)

### Customers
- `GET /api/customers` - List all customers (STAFF/ADMIN)
- `GET /api/customers/{id}` - Get customer by ID (STAFF/ADMIN)
- `POST /api/customers` - Create new customer (STAFF/ADMIN)
- `PUT /api/customers/{id}` - Update customer (STAFF/ADMIN)
- `DELETE /api/customers/{id}` - Delete customer (ADMIN)

### Employees
- `GET /api/employees` - List all employees (STAFF/ADMIN)
- `GET /api/employees/{id}` - Get employee by ID (STAFF/ADMIN)
- `POST /api/employees` - Create new employee (STAFF/ADMIN)
- `PUT /api/employees/{id}` - Update employee (STAFF/ADMIN)
- `DELETE /api/employees/{id}` - Delete employee (ADMIN)

### Orders
- `GET /api/orders` - List all orders (STAFF/ADMIN)
- `GET /api/orders/{id}` - Get order by ID (STAFF/ADMIN)
- `POST /api/orders` - Create new order (STAFF/ADMIN)
- `PUT /api/orders/{id}` - Update order (STAFF/ADMIN)
- `DELETE /api/orders/{id}` - Delete order (ADMIN)
- `GET /api/orders/{id}/summary` - Get order with details and totals
- `GET /api/orders/{id}/summary/total` - Get order total amount

**Query Parameters:**
- `customerId` - Filter by customer
- `employeeId` - Filter by employee
- `startDate` - Filter by order date (from)
- `endDate` - Filter by order date (to)

### Order Details
- `GET /api/orders/{orderId}/details` - List order details (STAFF/ADMIN)
- `GET /api/orders/{orderId}/details/{productId}` - Get specific order detail
- `POST /api/orders/{orderId}/details` - Add order detail (STAFF/ADMIN)
- `PUT /api/orders/{orderId}/details/{productId}` - Update order detail (STAFF/ADMIN)
- `DELETE /api/orders/{orderId}/details/{productId}` - Remove order detail (ADMIN)

### Authentication
- `POST /api/auth/login` - Login and get JWT token
- `POST /api/auth/refresh` - Refresh JWT token

## 🧪 Testing

### Unit Tests
```bash
mvn test
```

### Integration Tests (requires Docker)
```bash
mvn test -Dtest="*IntegrationTest"
```

### Test Coverage
- **Unit Tests**: Controller, Service, and Repository layers
- **Integration Tests**: Full-stack testing with Testcontainers
- **Security Tests**: Authentication and authorization scenarios
- **API Tests**: End-to-end API functionality

## 🔧 Configuration

### Application Properties
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/northwind
    username: northwind
    password: northwind
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: false
  flyway:
    enabled: true
    locations: classpath:db/migration

server:
  port: 8080

jwt:
  secret: your-secret-key
  expiration: 86400000 # 24 hours
```

### Security Configuration
- **CORS**: Configured for specific origins
- **Rate Limiting**: 100 requests per minute for public endpoints
- **Actuator Security**: Admin-only access to management endpoints
- **Method Security**: Role-based access control on service methods

## 📊 Database Schema

### Key Entities
- **Products**: Product catalog with categories and suppliers
- **Categories**: Product categorization
- **Suppliers**: Product suppliers
- **Customers**: Customer information
- **Employees**: Employee records
- **Orders**: Customer orders
- **Order Details**: Order line items with composite keys

### Relationships
- Products → Categories (Many-to-One)
- Products → Suppliers (Many-to-One)
- Orders → Customers (Many-to-One)
- Orders → Employees (Many-to-One)
- Orders → Order Details (One-to-Many)
- Order Details → Products (Many-to-One)

## 🚀 Deployment

### Docker Deployment
```bash
# Build image
docker build -t northwind-api .

# Run container
docker run -p 8080:8080 northwind-api
```

### Production Considerations
- Set strong JWT secret
- Configure proper CORS origins
- Use production database
- Enable HTTPS
- Configure proper logging
- Set up monitoring and alerting

## 📈 Performance Features

### Caching
- **ETag Support**: Conditional GET requests for bandwidth optimization
- **Connection Pooling**: HikariCP for efficient database connections

### Database Optimization
- **Indexes**: On frequently queried columns (order_date, customer_id, etc.)
- **Lazy Loading**: For entity relationships
- **Query Optimization**: Using JPA Specifications

### API Optimization
- **Pagination**: Default page size of 20, configurable
- **Sorting**: Multiple field sorting support
- **Filtering**: Dynamic query building with Specifications

## 🔍 Monitoring & Observability

### Actuator Endpoints (Admin only)
- `/actuator/health` - Application health
- `/actuator/info` - Application information
- `/actuator/metrics` - Application metrics
- `/actuator/env` - Environment properties

### Logging
- Structured logging with Spring Boot
- Request/response logging for debugging
- Security event logging

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Ensure all tests pass
6. Submit a pull request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🆘 Support

For support and questions:
- Check the [API Manual](API_MANUAL.md) for detailed usage examples
- Review the [Postman Collection](postman_collection.json) for API exploration
- Open an issue for bugs or feature requests

---

**Built with ❤️ using Spring Boot, PostgreSQL, and modern Java practices.**
