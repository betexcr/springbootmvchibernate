# 🚀 CLI Deployment Guide

Deploy your Northwind API to Supabase using command-line tools.

## 📋 Prerequisites

1. **Supabase Account** - [Sign up here](https://supabase.com)
2. **Node.js** - For Supabase CLI
3. **Docker** - For containerization
4. **Maven** - For building the application

## 🚀 Quick Start

### 1. Setup Supabase (One-time setup)

```bash
# Run the Supabase setup script
./setup-supabase.sh
```

This script will:
- Install Supabase CLI
- Create/link to your Supabase project
- Generate environment variables
- Run database migrations
- Test the connection

### 2. Deploy to Your Preferred Platform

#### Option A: Railway (Recommended)
```bash
# Install Railway CLI
npm install -g @railway/cli

# Deploy
./deploy.sh -p railway
```

#### Option B: Render
```bash
# Install Render CLI
npm install -g @render/cli

# Deploy
./deploy.sh -p render
```

#### Option C: Heroku
```bash
# Install Heroku CLI
# Download from: https://devcenter.heroku.com/articles/heroku-cli

# Deploy
./deploy.sh -p heroku
```

#### Option D: Fly.io
```bash
# Install Fly CLI
# Download from: https://fly.io/docs/hands-on/install-flyctl/

# Deploy
./deploy.sh -p fly
```

#### Option E: Google Cloud Run
```bash
# Install Google Cloud CLI
# Download from: https://cloud.google.com/sdk/docs/install

# Deploy
./deploy.sh -p gcp
```

## 🔧 Manual Setup (Alternative)

If you prefer manual setup:

### 1. Create Supabase Project
```bash
# Login to Supabase
supabase login

# Create new project
supabase projects create northwind-api --db-password your-password --region us-east-1

# Link to project
supabase link --project-ref YOUR_PROJECT_REF
```

### 2. Set Environment Variables
```bash
# Export required variables
export DATABASE_URL="postgresql://postgres:password@db.YOUR_PROJECT_REF.supabase.co:5432/postgres"
export JWT_SECRET="your-super-secret-jwt-key"
export JWT_EXPIRATION="86400000"
```

### 3. Run Migrations
```bash
# Using Maven
mvn flyway:migrate -Dflyway.url="$DATABASE_URL" -Dflyway.user=postgres -Dflyway.password=""

# Or using Flyway CLI
flyway -url="$DATABASE_URL" -user=postgres -password="" -locations=filesystem:src/main/resources/db/migration migrate
```

### 4. Deploy
```bash
# Choose your platform
./deploy.sh -p railway
```

## 📊 Platform Comparison

| Platform | Free Tier | Ease of Use | Performance | Best For |
|----------|-----------|-------------|-------------|----------|
| **Railway** | ✅ | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐ | Quick deployment |
| **Render** | ✅ | ⭐⭐⭐⭐ | ⭐⭐⭐ | Budget-friendly |
| **Heroku** | ❌ | ⭐⭐⭐⭐ | ⭐⭐⭐⭐ | Enterprise |
| **Fly.io** | ✅ | ⭐⭐⭐ | ⭐⭐⭐⭐⭐ | High performance |
| **Google Cloud** | ✅ | ⭐⭐ | ⭐⭐⭐⭐⭐ | Scalability |

## 🔍 Troubleshooting

### Common Issues

#### 1. Database Connection Failed
```bash
# Test connection
psql "$DATABASE_URL" -c "SELECT 1;"

# Check if Supabase project is active
supabase projects list
```

#### 2. Build Failures
```bash
# Clean and rebuild
mvn clean package -DskipTests

# Check Java version
java -version
```

#### 3. Deployment Timeout
```bash
# Check platform status
railway status
# or
render services list
```

#### 4. Environment Variables Not Set
```bash
# Source environment file
source .env

# Verify variables
echo $DATABASE_URL
echo $JWT_SECRET
```

## 🧪 Testing Deployment

After deployment, test your API:

```bash
# Get your app URL (example for Railway)
APP_URL=$(railway domain)

# Test health endpoint
curl "$APP_URL/actuator/health"

# Test products endpoint
curl "$APP_URL/api/products"

# Test authentication
curl -X POST "$APP_URL/api/auth/login" \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
```

## 🔄 CI/CD Integration

### GitHub Actions Example
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

## 📈 Monitoring

### Health Checks
- **Railway**: Automatic health checks at `/actuator/health`
- **Render**: Built-in health monitoring
- **Heroku**: Custom health check endpoints
- **Fly.io**: Health check configuration in `fly.toml`

### Logs
```bash
# Railway
railway logs

# Render
render logs

# Heroku
heroku logs --tail

# Fly.io
flyctl logs
```

## 🔐 Security Best Practices

1. **Environment Variables**: Never commit secrets to Git
2. **Database Access**: Use connection pooling
3. **CORS**: Configure for your frontend domain
4. **Rate Limiting**: Adjust for production traffic
5. **JWT Secret**: Use a strong, random secret

## 📚 Next Steps

1. **Custom Domain**: Configure your own domain
2. **SSL Certificates**: Enable HTTPS
3. **Monitoring**: Set up alerts and monitoring
4. **Backups**: Configure automated backups
5. **Scaling**: Plan for traffic growth

## 🆘 Support

- **Supabase Docs**: https://supabase.com/docs
- **Railway Docs**: https://docs.railway.app
- **Render Docs**: https://render.com/docs
- **Heroku Docs**: https://devcenter.heroku.com
- **Fly.io Docs**: https://fly.io/docs

---

**Happy Deploying! 🚀**
