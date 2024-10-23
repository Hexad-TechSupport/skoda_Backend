package skoda_backend

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import skoda_backend.controllers.userRoutes
import org.jetbrains.exposed.sql.Database
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import skoda_backend.utils.verifier
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.serialization.json.Json
import skoda_backend.controllers.vehicleRoutes

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

    install(Authentication) {
        jwt("auth-jwt") {
            verifier(verifier)
            validate { credential ->
                println("JWT claims: ${credential.payload.claims}")
                if (credential.payload.audience.contains("skoda-audience")) {
                    JWTPrincipal(credential.payload)
                } else null
            }
        }
    }

    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        })
    }
    // Call the routing configuration from controllers
    configureRouting()
}

fun Application.configureRouting() {
    // Configure routes from user controller (can add more controllers here)
    userRoutes()
    vehicleRoutes()
}
