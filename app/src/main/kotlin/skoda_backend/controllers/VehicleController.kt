package skoda_backend.controllers

import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import skoda_backend.repositories.VehicleRepository
import skoda_backend.services.VehicleService
import java.util.*

fun Application.vehicleRoutes() {
    val vehicleRepository = VehicleRepository()
    var vehicleService = VehicleService(vehicleRepository)

    routing {
        route("/vehicles") {
            authenticate("auth-jwt"){
                get("/") {

                    val userId = call.queryParameters["userId"]
                    val vehicleId = call.queryParameters["vehicleId"]

                    val principal = call.principal<JWTPrincipal>()
                    val uId = principal?.getClaim("userId", String::class)
                    if (uId != userId){
                        call.respond(HttpStatusCode.Unauthorized, "User dont have permissions")
                    }

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

                post("/{vehicleId}/lock") {
                    val vehicleId = call.parameters["vehicleId"]!!
                    val principal = call.principal<JWTPrincipal>()!!
                    val userId = principal.payload.getClaim("userId").asString()

                    if (vehicleService.lockVehicle(vehicleId, userId)) {
                        call.respond(HttpStatusCode.OK, "Vehicle locked successfully")
                    } else {
                        call.respond(HttpStatusCode.InternalServerError, "Unable to lock vehicle")
                    }
                }

                post("/{vehicleId}/unlock") {
                    val vehicleId = call.parameters["vehicleId"]!!
                    val principal = call.principal<JWTPrincipal>()!!
                    val userId = principal.payload.getClaim("userId").asString()

                    if (vehicleService.unlockVehicle(vehicleId, userId)) {
                        call.respond(HttpStatusCode.OK, "Vehicle unlocked successfully")
                    } else {
                        call.respond(HttpStatusCode.InternalServerError, "Unable to unlock vehicle")
                    }
                }

                get("/{vehicleId}/history") {
                    val vehicleId = call.parameters["vehicleId"]!!
                    val history = vehicleService.getVehicleHistory(vehicleId)
                    call.respond(history)
                    }
            }
        }
        }
    }

