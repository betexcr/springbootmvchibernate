#!/bin/bash

# Northwind API Deployment Script
# Supports multiple deployment platforms via CLI

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Default values
PLATFORM=""
PROJECT_NAME="northwind-api"
REGION="us-east-1"
BUILD_LOCAL=true

# Function to print colored output
print_status() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Function to show usage
show_usage() {
    echo "Usage: $0 [OPTIONS]"
    echo ""
    echo "Options:"
    echo "  -p, --platform PLATFORM    Deployment platform (railway|render|heroku|fly|gcp)"
    echo "  -n, --name NAME           Project name (default: northwind-api)"
    echo "  -r, --region REGION       Deployment region (default: us-east-1)"
    echo "  --no-build               Skip local build"
    echo "  -h, --help               Show this help message"
    echo ""
    echo "Examples:"
    echo "  $0 -p railway"
    echo "  $0 -p render -n my-northwind-api"
    echo "  $0 -p heroku --no-build"
}

# Parse command line arguments
while [[ $# -gt 0 ]]; do
    case $1 in
        -p|--platform)
            PLATFORM="$2"
            shift 2
            ;;
        -n|--name)
            PROJECT_NAME="$2"
            shift 2
            ;;
        -r|--region)
            REGION="$2"
            shift 2
            ;;
        --no-build)
            BUILD_LOCAL=false
            shift
            ;;
        -h|--help)
            show_usage
            exit 0
            ;;
        *)
            print_error "Unknown option: $1"
            show_usage
            exit 1
            ;;
    esac
done

# Check if platform is specified
if [[ -z "$PLATFORM" ]]; then
    print_error "Platform is required. Use -p or --platform option."
    show_usage
    exit 1
fi

# Check if required tools are installed
check_dependencies() {
    print_status "Checking dependencies..."
    
    case $PLATFORM in
        railway)
            if ! command -v railway &> /dev/null; then
                print_error "Railway CLI is not installed. Install it with: npm install -g @railway/cli"
                exit 1
            fi
            ;;
        render)
            if ! command -v render &> /dev/null; then
                print_error "Render CLI is not installed. Install it with: npm install -g @render/cli"
                exit 1
            fi
            ;;
        heroku)
            if ! command -v heroku &> /dev/null; then
                print_error "Heroku CLI is not installed. Install it from: https://devcenter.heroku.com/articles/heroku-cli"
                exit 1
            fi
            ;;
        fly)
            if ! command -v flyctl &> /dev/null; then
                print_error "Fly CLI is not installed. Install it from: https://fly.io/docs/hands-on/install-flyctl/"
                exit 1
            fi
            ;;
        gcp)
            if ! command -v gcloud &> /dev/null; then
                print_error "Google Cloud CLI is not installed. Install it from: https://cloud.google.com/sdk/docs/install"
                exit 1
            fi
            ;;
    esac
    
    if ! command -v docker &> /dev/null; then
        print_error "Docker is not installed. Please install Docker first."
        exit 1
    fi
    
    if ! command -v mvn &> /dev/null; then
        print_error "Maven is not installed. Please install Maven first."
        exit 1
    fi
    
    print_success "All dependencies are installed"
}

# Build the application
build_app() {
    if [[ "$BUILD_LOCAL" == true ]]; then
        print_status "Building application..."
        mvn clean package -DskipTests
        print_success "Application built successfully"
    else
        print_warning "Skipping local build"
    fi
}

# Deploy to Railway
deploy_railway() {
    print_status "Deploying to Railway..."
    
    # Check if logged in
    if ! railway whoami &> /dev/null; then
        print_status "Please login to Railway..."
        railway login
    fi
    
    # Create or link project
    if railway status &> /dev/null; then
        print_status "Using existing Railway project"
    else
        print_status "Creating new Railway project: $PROJECT_NAME"
        railway init "$PROJECT_NAME"
    fi
    
    # Set environment variables
    print_status "Setting environment variables..."
    railway variables set DATABASE_URL="$DATABASE_URL"
    railway variables set JWT_SECRET="$JWT_SECRET"
    railway variables set JWT_EXPIRATION="86400000"
    
    # Deploy
    print_status "Deploying application..."
    railway up
    
    print_success "Deployed to Railway successfully!"
    print_status "Get your app URL with: railway domain"
}

# Deploy to Render
deploy_render() {
    print_status "Deploying to Render..."
    
    # Check if logged in
    if ! render auth whoami &> /dev/null; then
        print_status "Please login to Render..."
        render auth login
    fi
    
    # Create render.yaml
    cat > render.yaml << EOF
services:
  - type: web
    name: $PROJECT_NAME
    env: java
    buildCommand: mvn clean package -DskipTests
    startCommand: java -jar target/northwind-0.0.1-SNAPSHOT.jar
    envVars:
      - key: DATABASE_URL
        value: $DATABASE_URL
      - key: JWT_SECRET
        value: $JWT_SECRET
      - key: JWT_EXPIRATION
        value: 86400000
EOF
    
    # Deploy
    print_status "Deploying application..."
    render deploy
    
    print_success "Deployed to Render successfully!"
}

# Deploy to Heroku
deploy_heroku() {
    print_status "Deploying to Heroku..."
    
    # Check if logged in
    if ! heroku auth:whoami &> /dev/null; then
        print_status "Please login to Heroku..."
        heroku login
    fi
    
    # Create or use existing app
    if heroku apps:info "$PROJECT_NAME" &> /dev/null; then
        print_status "Using existing Heroku app: $PROJECT_NAME"
    else
        print_status "Creating new Heroku app: $PROJECT_NAME"
        heroku create "$PROJECT_NAME"
    fi
    
    # Create Procfile
    echo "web: java -jar target/northwind-0.0.1-SNAPSHOT.jar" > Procfile
    
    # Set environment variables
    print_status "Setting environment variables..."
    heroku config:set DATABASE_URL="$DATABASE_URL" --app "$PROJECT_NAME"
    heroku config:set JWT_SECRET="$JWT_SECRET" --app "$PROJECT_NAME"
    heroku config:set JWT_EXPIRATION="86400000" --app "$PROJECT_NAME"
    
    # Deploy
    print_status "Deploying application..."
    git add .
    git commit -m "Deploy to Heroku" || true
    git push heroku main
    
    print_success "Deployed to Heroku successfully!"
    print_status "App URL: https://$PROJECT_NAME.herokuapp.com"
}

# Deploy to Fly.io
deploy_fly() {
    print_status "Deploying to Fly.io..."
    
    # Check if logged in
    if ! flyctl auth whoami &> /dev/null; then
        print_status "Please login to Fly.io..."
        flyctl auth login
    fi
    
    # Create fly.toml if it doesn't exist
    if [[ ! -f fly.toml ]]; then
        print_status "Creating fly.toml configuration..."
        flyctl launch --no-deploy --name "$PROJECT_NAME"
    fi
    
    # Set secrets
    print_status "Setting secrets..."
    flyctl secrets set DATABASE_URL="$DATABASE_URL" --app "$PROJECT_NAME"
    flyctl secrets set JWT_SECRET="$JWT_SECRET" --app "$PROJECT_NAME"
    flyctl secrets set JWT_EXPIRATION="86400000" --app "$PROJECT_NAME"
    
    # Deploy
    print_status "Deploying application..."
    flyctl deploy --app "$PROJECT_NAME"
    
    print_success "Deployed to Fly.io successfully!"
    print_status "App URL: https://$PROJECT_NAME.fly.dev"
}

# Deploy to Google Cloud Run
deploy_gcp() {
    print_status "Deploying to Google Cloud Run..."
    
    # Check if logged in
    if ! gcloud auth list --filter=status:ACTIVE --format="value(account)" | grep -q .; then
        print_status "Please login to Google Cloud..."
        gcloud auth login
    fi
    
    # Set project
    gcloud config set project "$PROJECT_NAME"
    
    # Build and push to Container Registry
    print_status "Building and pushing Docker image..."
    gcloud builds submit --tag gcr.io/"$PROJECT_NAME"/northwind-api .
    
    # Deploy to Cloud Run
    print_status "Deploying to Cloud Run..."
    gcloud run deploy northwind-api \
        --image gcr.io/"$PROJECT_NAME"/northwind-api \
        --platform managed \
        --region "$REGION" \
        --allow-unauthenticated \
        --set-env-vars DATABASE_URL="$DATABASE_URL",JWT_SECRET="$JWT_SECRET",JWT_EXPIRATION="86400000"
    
    print_success "Deployed to Google Cloud Run successfully!"
}

# Main deployment function
deploy() {
    print_status "Starting deployment to $PLATFORM..."
    
    # Check if environment variables are set
    if [[ -z "$DATABASE_URL" ]]; then
        print_error "DATABASE_URL environment variable is not set"
        print_status "Please set it with: export DATABASE_URL='your-database-url'"
        exit 1
    fi
    
    if [[ -z "$JWT_SECRET" ]]; then
        print_error "JWT_SECRET environment variable is not set"
        print_status "Please set it with: export JWT_SECRET='your-jwt-secret'"
        exit 1
    fi
    
    check_dependencies
    build_app
    
    case $PLATFORM in
        railway)
            deploy_railway
            ;;
        render)
            deploy_render
            ;;
        heroku)
            deploy_heroku
            ;;
        fly)
            deploy_fly
            ;;
        gcp)
            deploy_gcp
            ;;
        *)
            print_error "Unsupported platform: $PLATFORM"
            print_status "Supported platforms: railway, render, heroku, fly, gcp"
            exit 1
            ;;
    esac
}

# Run deployment
deploy
