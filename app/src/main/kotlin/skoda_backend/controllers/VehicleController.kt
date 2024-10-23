package skoda_backend.controllers

import VehicleRepository
import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.http.*
import io.ktor.server.auth.*
import java.util.*

fun Application.vehicleRoutes() {
    val vehicleRepository = VehicleRepository()

    routing {
        route("/vehicles") {
            authenticate("auth-jwt"){
                get("/") {

                    val userId = call.queryParameters["userId"]
                    val vehicleId = call.queryParameters["vehicleId"]

                    if (userId != null && vehicleId != null) {
                        val vehicle = vehicleRepository.getVehicleByUserAndVehicleId(userId, vehicleId)
                        if (vehicle != null) {
                            call.respond(HttpStatusCode.OK, vehicle)
                        } else {
                            call.respond(HttpStatusCode.NotFound, "Vehicle not found")
                        }
                    } else {
                        call.respond(HttpStatusCode.BadRequest, "Invalid or missing IDs")
                    }
                }
            }
        }
        }
    }

