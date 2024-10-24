package skoda_backend

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import skoda_backend.controllers.userRoutes
import org.jetbrains.exposed.sql.Database
import io.ktor.server.plugins.autohead.*
import io.ktor.server.auth.jwt.*
import skoda_backend.utils.verifier
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.serialization.json.Json
import skoda_backend.controllers.vehicleRoutes
import io.ktor.server.plugins.forwardedheaders.*
import io.ktor.server.plugins.cors.routing.*
import skoda_backend.controllers.subscriptionRoutes
import org.jetbrains.exposed.sql.transactions.transaction
import java.nio.file.Files
import java.nio.file.Paths

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
            .start(wait = true)
}

fun Application.module() {

    val dbHost = System.getenv("DB_HOST") ?: "localhost"
    val dbPort = System.getenv("DB_PORT") ?: "5432"
    val dbUser = System.getenv("DB_USER") ?: "postgres"
    val dbPassword = System.getenv("DB_PASSWORD") ?: ""
    val dbName = System.getenv("DB_NAME") ?: "postgres"
    // Connect to PostgreSQL database

    val jdbcUrl = "jdbc:postgresql://$dbHost:$dbPort/$dbName?sslmode=disable"
    Database.connect(
            url = jdbcUrl,
            driver = "org.postgresql.Driver",
            user = dbUser,
            password = dbPassword
    )

    install(ForwardedHeaders)
    install(AutoHeadResponse)
    install(CORS) {
        allowNonSimpleContentTypes = true
        anyHost() // For development only. In production, use specific domains.
    }


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
    subscriptionRoutes()
}

