package skoda_backend.controllers

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import com.google.gson.Gson

fun Application.monitoringRoutes() {

    routing {
        route("") {
            get("/health") {
                val json = Gson().toJson(mapOf("status" to "UP"))

                call.respondText(
                        contentType = ContentType.Application.Json,
                        text = json
                )
            }
        }
    }
}