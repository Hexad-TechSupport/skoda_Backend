package skoda_backend.controllers

import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import skoda_backend.models.CarTopSpeed
import skoda_backend.models.CarTopSpeeds
import skoda_backend.models.EngineStatusRequest
import skoda_backend.repositories.VehicleRepository
import skoda_backend.services.VehicleService

fun Application.vehicleRoutes() {
    val vehicleRepository = VehicleRepository()
    var vehicleService = VehicleService(vehicleRepository)

    routing {
        route("/vehicles") {
            authenticate("auth-jwt") {
                get("/") {

                    val userId = call.queryParameters["userId"]
                    val vehicleId = call.queryParameters["vehicleId"]

                    val principal = call.principal<JWTPrincipal>()
                    val uId = principal?.getClaim("userId", String::class)
                    if (uId != userId) {
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
                post("/updateEngineStatus") {

                    val engineStatusReq = call.receive<EngineStatusRequest>()
                    val principal = call.principal<JWTPrincipal>()!!
                    val userId = principal?.getClaim("userId", String::class)
                    println(userId)
                    if (userId != engineStatusReq.userId) {
                        call.respond(HttpStatusCode.Unauthorized, "User dont have permissions")
                    }
                    if (vehicleService.updateEngineStatus(engineStatusReq)) {
                        call.respond(HttpStatusCode.OK, "Vehicle's engine status updated successfully")
                    } else {
                        call.respond(HttpStatusCode.InternalServerError, "Unable to update engine status")
                    }
                }
                get("/{vehicleId}/history") {
                    val vehicleId = call.parameters["vehicleId"]!!
                    val history = vehicleService.getVehicleHistory(vehicleId)
                    call.respond(history)
                }
            }

            get("/top-speeds") {
                val make = call.request.queryParameters["make"]
                val model = call.request.queryParameters["model"]
                val country = call.request.queryParameters["country"]

                val filteredCarTopSpeeds = transaction {
                    CarTopSpeeds.selectAll().where {
                        ((make?.let { CarTopSpeeds.make eq it }) ?: Op.TRUE) and
                        ((model?.let { CarTopSpeeds.model eq it }) ?: Op.TRUE) and
                                ((country?.let { CarTopSpeeds.country eq it }) ?: Op.TRUE)
                    }.map {
                        CarTopSpeed(
                                id = it[CarTopSpeeds.id],
                                make = it[CarTopSpeeds.make],
                                model = it[CarTopSpeeds.model],
                                country = it[CarTopSpeeds.country],
                                topSpeedKmh = it[CarTopSpeeds.topSpeedKmh],
                                topSpeedMph = it[CarTopSpeeds.topSpeedMph]
                        )
                    }
                }

                call.respond(filteredCarTopSpeeds)
            }
        }
    }
}

