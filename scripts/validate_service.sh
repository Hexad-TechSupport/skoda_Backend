#!/bin/bash
# Wait for the application to start
sleep 10
# Check if the application is running
curl -f http://localhost:8080/health || exit 1