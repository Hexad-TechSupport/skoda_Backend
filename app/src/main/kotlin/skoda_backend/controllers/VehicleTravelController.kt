package skoda_backend.controllers

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import skoda_backend.models.TravelHistoryResponse
import skoda_backend.repositories.VehicleTravelRepository
import skoda_backend.services.VehicleTravelService

fun Application.vehicleTravelRoutes() {
    val vehicleTravelRepository = VehicleTravelRepository()
    var vehicleTravelService = VehicleTravelService(vehicleTravelRepository)

    routing {
        route("/vehiclesTravel") {
            authenticate("auth-jwt") {
                post("/trips") {
                    val request = call.receive<TravelHistoryResponse>()

                    val principal = call.principal<JWTPrincipal>()
                    val userId = principal?.getClaim("userId", String::class)

                    if (userId != request.userId) {
                        call.respond(HttpStatusCode.Unauthorized, "User dont have permissions")
                    }

                    val resp = vehicleTravelService.addTravel(request)
                    if (resp != null) {
                        call.respond(HttpStatusCode.Created, resp)
                    } else {
                        call.respond(HttpStatusCode.InternalServerError, "Unable to add travel trip, please try again later")
                    }
                }

                get("/trips") {

                    val principal = call.principal<JWTPrincipal>()
                    val userId = principal?.getClaim("userId", String::class)
                    val vehicleId = call.queryParameters.get("vehicleId")

                    if (vehicleId == ""){
                        call.respond(HttpStatusCode.BadRequest, "please provide the vehicle id")
                    }
                    if (!vehicleId.isNullOrBlank() && !userId.isNullOrBlank()){
                        val resp = vehicleTravelService.getTrips(vehicleId, userId)
                        if (resp != null) {
                            call.respond(HttpStatusCode.OK, resp)
                        } else {
                            call.respond(HttpStatusCode.InternalServerError, "Unable get travel trips, please try again later")
                        }
                    } else{
                        call.respond(HttpStatusCode.BadRequest, "please provide the vehicle id or user is not authenticated")
                    }
                }

                delete("/trips/{id}") {
                    val historyId = call.parameters["id"]?.toIntOrNull()

                    if (historyId == null) {
                        call.respond(HttpStatusCode.BadRequest, "Invalid history ID")
                        return@delete
                    }

                    // Call the service to delete the travel history
                    val isDeleted = vehicleTravelService.deleteTrip(historyId)

                    if (isDeleted) {
                        call.respond(HttpStatusCode.NoContent) // Successfully deleted
                    } else {
                        call.respond(HttpStatusCode.NotFound, "Travel history not found")
                    }
                }
            }
        }
    }
}