#!/bin/bash
echo "Running SQL script..."
# Define database connection parameters
# DB_HOST="your-database-host"
# DB_PORT="5433"
# DB_NAME="your-database-name"
# DB_USER="your-database-user"
# DB_PASSWORD="your-database-password"
# Run the SQL script using psql
PGPASSWORD=$DB_PASSWORD psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME -f /scripts/init.sql
echo "SQL script executed."


