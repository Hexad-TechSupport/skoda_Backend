package skoda_backend.controllers

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import skoda_backend.repositories.UserRepository
import skoda_backend.services.UserService
import com.google.gson.Gson

fun Application.monitoringRoutes() {

    val userRepository = UserRepository()
    val userService = UserService(userRepository)

    routing {
        route("") {
            // Create User
            get("/health") {
                val json = Gson().toJson(mapOf("status" to "UP"))

                // Use respondText to send the JSON response with the correct content type
                call.respondText(
                        contentType = ContentType.Application.Json,
                        text = json
                )
            }
        }
    }
}