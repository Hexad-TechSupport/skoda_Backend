#!/bin/bash

# Exit on any error
# set -e

# Update system packages
sudo yum update -y

# Install Java 20 (Corretto)
sudo yum install -y java-20-amazon-corretto-devel

# Install PostgreSQL client
sudo yum install -y postgresql15

# Create application directory
sudo mkdir -p /opt/skoda-backend
sudo chown -R ec2-user:ec2-user /opt/skoda-backend

# Set environment variables
cat << EOF | sudo tee /etc/profile.d/skoda-env.sh
export JAVA_HOME=/usr/lib/jvm/java-20-amazon-corretto
export PATH=\$JAVA_HOME/bin:\$PATH

# Application specific environment variables
export KTOR_ENV=production
export KTOR_PORT=8080
export DB_HOST=${DB_HOST}
export DB_PORT=${DB_PORT}
export DB_NAME=${DB_NAME}
export DB_USER=${DB_USER}
export DB_PASSWORD=${DB_PASSWORD}
EOF

# Make the environment file executable
sudo chmod +x /etc/profile.d/skoda-env.sh
source /etc/profile.d/skoda-env.sh

# Create log directory
sudo mkdir -p /var/log/skoda-backend
sudo chown -R ec2-user:ec2-user /var/log/skoda-backend

# Create service file for systemd
cat << EOF | sudo tee /etc/systemd/system/skoda-backend.service
[Unit]
Description=Skoda Backend Service
After=network.target

[Service]
Type=simple
User=ec2-user
Environment="JAVA_HOME=/usr/lib/jvm/java-20-amazon-corretto"
Environment="PATH=/usr/lib/jvm/java-20-amazon-corretto/bin:/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin"
EnvironmentFile=/etc/profile.d/skoda-env.sh
WorkingDirectory=/opt/skoda-backend
ExecStart=/usr/bin/java -jar app.jar
Restart=always

[Install]
WantedBy=multi-user.target
EOF

# Reload systemd
sudo systemctl daemon-reload

# Install additional dependencies if needed
sudo yum install -y curl wget

# Create health check endpoint directory
sudo mkdir -p /opt/skoda-backend/health
cat << EOF | sudo tee /opt/skoda-backend/health/index.html
{"status": "UP"}
EOF

# Set correct permissions
sudo chown -R ec2-user:ec2-user /opt/skoda-backend

echo "Before install script completed successfully"
