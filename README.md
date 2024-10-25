# skoda_backend

## Overview
This Kotlin application is a backend service designed for an infotainment system that communicates with Android 
and iOS apps. This backend also has user management and subscription managements.
The project is built using Kotlin and leverages technologies like Ktor for server functionality, 
PostgreSQL for database storage, and other libraries for seamless development.

## Features
- User Management
- Subscription Management
- Vehicle Lock/Unlock
- Engine On/Off
- Record Travel History

## Prerequisites
- **JDK**: 20 or higher
- **Kotlin**: 2.0 or higher
- **Database**: PostgreSQL
- **Environment Configuration**: 
  ```
    export DB_HOST=<postgres_host>
    export DB_PORT=<postgres_port>
    export DB_USER=<db_user>
    export DB_PASSWORD=<db_password>
    export DB_NAME=<db_name>
    export PORT=<application_port> // by default 8080
    ```

## Installation

1. **Clone the repository:**
   ```bash
   git clone https://github.com/Hexad-TechSupport/skoda_Backend.git
   cd skoda_Backend
   ```
2. **Install the dependencies:**
   ```
   ./gradlew build
   ```
3. **Run the application:**
   ```bash
   ./gradlew run
   ```
