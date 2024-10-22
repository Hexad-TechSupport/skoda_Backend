package skoda_backend

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import skoda_backend.controllers.userRoutes
import org.jetbrains.exposed.sql.Database

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
            .start(wait = true)
}

fun Application.module() {
    // Connect to PostgreSQL database
    Database.connect("jdbc:postgresql://localhost:5433/postgres?sslmode=disable",
            driver = "org.postgresql.Driver",
            user = "postgres",
            password = "")

    // Call the routing configuration from controllers
    configureRouting()
}

fun Application.configureRouting() {
    // Configure routes from user controller (can add more controllers here)
    userRoutes()
}
