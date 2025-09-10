#!/bin/bash

# Setup environment variables for local testing
# This script sets up the same environment variables that GitHub Actions uses

echo "🔧 Setting up test environment variables..."

export SPRING_PROFILES_ACTIVE=test
export DATABASE_URL="jdbc:postgresql://localhost:5432/northwind_test"
export DATABASE_USERNAME="test"
export DATABASE_PASSWORD="test"
export JWT_SECRET="test-secret-key-for-local-development"

echo "✅ Environment variables set:"
echo "   SPRING_PROFILES_ACTIVE=$SPRING_PROFILES_ACTIVE"
echo "   DATABASE_URL=$DATABASE_URL"
echo "   DATABASE_USERNAME=$DATABASE_USERNAME"
echo "   DATABASE_PASSWORD=$DATABASE_PASSWORD"
echo "   JWT_SECRET=$JWT_SECRET"

echo ""
echo "🚀 You can now run tests with:"
echo "   cd northwind"
echo "   mvn test"
echo ""
echo "💡 To make these permanent, add them to your ~/.bashrc or ~/.zshrc"
