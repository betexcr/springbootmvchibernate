#!/bin/bash

# Supabase Setup Script for Northwind API
# This script helps you set up Supabase and get the connection details

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

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

# Check if Supabase CLI is installed
check_supabase_cli() {
    if ! command -v supabase &> /dev/null; then
        print_status "Installing Supabase CLI..."
        npm install -g supabase
        print_success "Supabase CLI installed"
    else
        print_success "Supabase CLI is already installed"
    fi
}

# Setup Supabase project
setup_supabase() {
    print_status "Setting up Supabase project..."
    
    # Check if user is logged in
    if ! supabase projects list &> /dev/null; then
        print_status "Please login to Supabase..."
        supabase login
    fi
    
    # List existing projects
    print_status "Your existing Supabase projects:"
    supabase projects list
    
    echo ""
    read -p "Enter your Supabase project reference (or press Enter to create new): " PROJECT_REF
    
    if [[ -z "$PROJECT_REF" ]]; then
        print_status "Creating new Supabase project..."
        read -p "Enter project name: " PROJECT_NAME
        read -p "Enter database password: " DB_PASSWORD
        read -p "Enter region (e.g., us-east-1): " REGION
        
        # Create new project
        supabase projects create "$PROJECT_NAME" --db-password "$DB_PASSWORD" --region "$REGION"
        print_success "Project created successfully!"
        
        # Get project reference
        PROJECT_REF=$(supabase projects list | grep "$PROJECT_NAME" | awk '{print $1}')
    fi
    
    # Link to project
    print_status "Linking to project: $PROJECT_REF"
    supabase link --project-ref "$PROJECT_REF"
    
    # Get project details
    print_status "Getting project details..."
    PROJECT_URL="https://$PROJECT_REF.supabase.co"
    DATABASE_URL="postgresql://postgres:$(supabase projects api-keys --project-ref "$PROJECT_REF" | grep "db_password" | awk '{print $2}'):@db.$PROJECT_REF.supabase.co:5432/postgres"
    
    # Get API keys
    API_KEYS=$(supabase projects api-keys --project-ref "$PROJECT_REF")
    ANON_KEY=$(echo "$API_KEYS" | grep "anon" | awk '{print $2}')
    SERVICE_ROLE_KEY=$(echo "$API_KEYS" | grep "service_role" | awk '{print $2}')
    
    print_success "Supabase project setup complete!"
    
    # Create .env file
    cat > .env << EOF
# Supabase Configuration
SUPABASE_URL=$PROJECT_URL
SUPABASE_ANON_KEY=$ANON_KEY
SUPABASE_SERVICE_ROLE_KEY=$SERVICE_ROLE_KEY
DATABASE_URL=$DATABASE_URL

# JWT Configuration
JWT_SECRET=$(openssl rand -base64 32)
JWT_EXPIRATION=86400000

# Application Configuration
SPRING_PROFILES_ACTIVE=production
EOF
    
    print_success "Environment variables saved to .env file"
    
    # Display connection details
    echo ""
    print_status "=== Supabase Connection Details ==="
    echo "Project URL: $PROJECT_URL"
    echo "Database URL: $DATABASE_URL"
    echo "Anon Key: $ANON_KEY"
    echo "Service Role Key: $SERVICE_ROLE_KEY"
    echo ""
    print_warning "Keep these credentials secure!"
    echo ""
    
    # Ask if user wants to run migrations
    read -p "Do you want to run database migrations now? (y/n): " RUN_MIGRATIONS
    
    if [[ "$RUN_MIGRATIONS" == "y" || "$RUN_MIGRATIONS" == "Y" ]]; then
        run_migrations
    fi
}

# Run database migrations
run_migrations() {
    print_status "Running database migrations..."
    
    # Check if Flyway is available
    if command -v flyway &> /dev/null; then
        print_status "Using Flyway CLI..."
        flyway -url="$DATABASE_URL" -user=postgres -password="" -locations=filesystem:src/main/resources/db/migration migrate
    else
        print_status "Using Maven Flyway plugin..."
        mvn flyway:migrate -Dflyway.url="$DATABASE_URL" -Dflyway.user=postgres -Dflyway.password=""
    fi
    
    print_success "Database migrations completed!"
}

# Test database connection
test_connection() {
    print_status "Testing database connection..."
    
    # Check if psql is available
    if command -v psql &> /dev/null; then
        if psql "$DATABASE_URL" -c "SELECT 1;" &> /dev/null; then
            print_success "Database connection successful!"
        else
            print_error "Database connection failed!"
            exit 1
        fi
    else
        print_warning "psql not found. Skipping connection test."
    fi
}

# Main setup function
main() {
    print_status "Starting Supabase setup for Northwind API..."
    
    check_supabase_cli
    setup_supabase
    test_connection
    
    echo ""
    print_success "Supabase setup complete!"
    print_status "You can now deploy your application using:"
    echo "  ./deploy.sh -p railway"
    echo "  ./deploy.sh -p render"
    echo "  ./deploy.sh -p heroku"
    echo "  ./deploy.sh -p fly"
    echo ""
    print_status "Make sure to source the environment variables:"
    echo "  source .env"
}

# Run main function
main
