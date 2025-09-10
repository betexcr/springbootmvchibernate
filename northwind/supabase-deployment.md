# Supabase Deployment Guide

## 🚀 Deploying Northwind API to Supabase

### Prerequisites
- Supabase account (free tier available)
- Docker installed locally
- Maven installed

## Step 1: Create Supabase Project

1. Go to [supabase.com](https://supabase.com)
2. Create a new project
3. Note down your project URL and API keys

## Step 2: Database Setup

### Option A: Use Supabase Dashboard
1. Go to your Supabase project dashboard
2. Navigate to SQL Editor
3. Run the Northwind schema migration

### Option B: Use Supabase CLI
```bash
# Install Supabase CLI
npm install -g supabase

# Initialize Supabase in your project
supabase init

# Link to your project
supabase link --project-ref YOUR_PROJECT_REF

# Push migrations
supabase db push
```

## Step 3: Update Application Configuration

### Update application.yml
```yaml
spring:
  datasource:
    url: jdbc:postgresql://db.YOUR_PROJECT_REF.supabase.co:5432/postgres
    username: postgres
    password: YOUR_DATABASE_PASSWORD
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: false
  flyway:
    enabled: true
    locations: classpath:db/migration

# Supabase specific settings
supabase:
  url: https://YOUR_PROJECT_REF.supabase.co
  anon-key: YOUR_ANON_KEY
  service-role-key: YOUR_SERVICE_ROLE_KEY
```

## Step 4: Deploy Spring Boot Application

### Option A: Railway (Recommended for Spring Boot)
```bash
# Install Railway CLI
npm install -g @railway/cli

# Login to Railway
railway login

# Deploy
railway deploy
```

### Option B: Render
1. Connect your GitHub repository to Render
2. Set environment variables
3. Deploy automatically

### Option C: Heroku
```bash
# Install Heroku CLI
# Create Procfile
echo "web: java -jar target/northwind-0.0.1-SNAPSHOT.jar" > Procfile

# Deploy
git add .
git commit -m "Deploy to Heroku"
git push heroku main
```

## Step 5: Environment Variables

Set these in your deployment platform:

```bash
# Database
DATABASE_URL=jdbc:postgresql://db.YOUR_PROJECT_REF.supabase.co:5432/postgres
DB_USERNAME=postgres
DB_PASSWORD=YOUR_DATABASE_PASSWORD

# JWT
JWT_SECRET=your-super-secret-jwt-key-here
JWT_EXPIRATION=86400000

# Supabase
SUPABASE_URL=https://YOUR_PROJECT_REF.supabase.co
SUPABASE_ANON_KEY=YOUR_ANON_KEY
SUPABASE_SERVICE_ROLE_KEY=YOUR_SERVICE_ROLE_KEY
```

## Step 6: Database Migration

### Run Flyway migrations
```bash
# Local migration to Supabase
mvn flyway:migrate -Dflyway.url=jdbc:postgresql://db.YOUR_PROJECT_REF.supabase.co:5432/postgres \
  -Dflyway.user=postgres \
  -Dflyway.password=YOUR_DATABASE_PASSWORD
```

## Step 7: Test Deployment

```bash
# Test API endpoints
curl https://your-app-url.railway.app/api/products
curl https://your-app-url.railway.app/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
```

## 🔧 Configuration Files

### Dockerfile for deployment
```dockerfile
FROM openjdk:21-jdk-slim

WORKDIR /app

COPY target/northwind-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
```

### docker-compose.yml for local testing
```yaml
version: '3.8'
services:
  app:
    build: .
    ports:
      - "8080:8080"
    environment:
      - DATABASE_URL=jdbc:postgresql://db.YOUR_PROJECT_REF.supabase.co:5432/postgres
      - DB_USERNAME=postgres
      - DB_PASSWORD=YOUR_DATABASE_PASSWORD
      - JWT_SECRET=your-jwt-secret
```

## 🚀 Deployment Platforms

### 1. Railway (Recommended)
- **Pros**: Easy Spring Boot deployment, automatic builds
- **Cons**: Limited free tier
- **Setup**: Connect GitHub, auto-deploy on push

### 2. Render
- **Pros**: Free tier, easy setup
- **Cons**: Slower cold starts
- **Setup**: Connect repository, set environment variables

### 3. Heroku
- **Pros**: Mature platform, good documentation
- **Cons**: No free tier anymore
- **Setup**: Git-based deployment

### 4. Google Cloud Run
- **Pros**: Serverless, pay-per-use
- **Cons**: More complex setup
- **Setup**: Container-based deployment

## 🔐 Security Considerations

1. **Environment Variables**: Never commit secrets to Git
2. **Database Access**: Use connection pooling
3. **CORS**: Configure for your frontend domain
4. **Rate Limiting**: Adjust for production traffic
5. **JWT Secret**: Use a strong, random secret

## 📊 Monitoring

### Supabase Dashboard
- Monitor database performance
- View query logs
- Check connection usage

### Application Monitoring
- Use Actuator endpoints
- Set up logging
- Monitor response times

## 🧪 Testing Production

```bash
# Test all endpoints
curl https://your-app-url/api/products
curl https://your-app-url/api/categories
curl https://your-app-url/api/orders

# Test authentication
TOKEN=$(curl -s -X POST https://your-app-url/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}' | jq -r '.token')

# Test authenticated endpoint
curl -H "Authorization: Bearer $TOKEN" \
  https://your-app-url/api/customers
```

## 🔄 CI/CD Pipeline

### GitHub Actions
```yaml
name: Deploy to Railway
on:
  push:
    branches: [main]
jobs:
  deploy:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - name: Set up JDK 21
        uses: actions/setup-java@v2
        with:
          java-version: '21'
          distribution: 'temurin'
      - name: Build with Maven
        run: mvn clean package -DskipTests
      - name: Deploy to Railway
        uses: railway-app/railway-deploy@v1
        with:
          railway-token: ${{ secrets.RAILWAY_TOKEN }}
```

## 📈 Performance Optimization

1. **Connection Pooling**: Configure HikariCP for Supabase
2. **Caching**: Use Redis for session storage
3. **CDN**: Use Cloudflare for static assets
4. **Database Indexes**: Already configured in migrations

## 🆘 Troubleshooting

### Common Issues
1. **Connection Timeout**: Check Supabase connection limits
2. **Migration Failures**: Verify database permissions
3. **JWT Issues**: Check secret configuration
4. **CORS Errors**: Update CORS configuration

### Debug Commands
```bash
# Check database connection
psql "postgresql://postgres:password@db.YOUR_PROJECT_REF.supabase.co:5432/postgres"

# Test API locally
mvn spring-boot:run -Dspring.profiles.active=prod

# Check logs
railway logs
# or
heroku logs --tail
```

## 📚 Next Steps

1. Set up monitoring and alerting
2. Configure automated backups
3. Set up staging environment
4. Implement API versioning
5. Add rate limiting per user
6. Set up SSL certificates
7. Configure custom domain
