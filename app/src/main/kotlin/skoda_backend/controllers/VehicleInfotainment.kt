package skoda_backend.controllers

import com.google.gson.Gson
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import skoda_backend.models.*
import skoda_backend.repositories.UserRepository
import skoda_backend.repositories.VehicleRepository
import skoda_backend.services.UserService
import skoda_backend.services.VehicleService


fun Application.vehicleInfotainmentRoute() {
    val vehicleRepository = VehicleRepository()
    //var vehicleService = VehicleService(vehicleRepository)

    routing {
        route("device") {
            // Create User
            get("/speed") {
                val secret = call.queryParameters.get("secret")
                val vehicleId = call.queryParameters.get("vehicleId")

                if (secret.isNullOrBlank() && secret != "SkodaHexad9922") {
                    call.respond(HttpStatusCode.Unauthorized, "User dont have permissions")
                }
                if (vehicleId.isNullOrBlank() || vehicleId == "") {
                    call.respond(HttpStatusCode.Unauthorized, "Bad request vehicle id is not present")
                } else {
                    val resp = vehicleRepository.getVehicleByVehicleId(vehicleId)
                    if (resp != null) {
                        val json = Gson().toJson(
                                mapOf(
                                        "speed" to resp?.speed,
                                        "OEMSpeed" to resp?.topSpeed
                                )
                        )
                        call.respondText(
                                contentType = ContentType.Application.Json,
                                text = json
                        )
                    } else {
                        call.respond(HttpStatusCode.NotFound, "Vehicle not found")
                    }
                }
            }

            post("/totalDistance") {
                val secret = call.queryParameters.get("secret")
                val vehicleId = call.queryParameters.get("vehicleId")

                if (secret.isNullOrBlank() && secret != "SkodaHexad9922") {
                    call.respond(HttpStatusCode.Unauthorized, "User dont have permissions")
                }

                val mileageRequest = call.receive<MileageRequest>()
                if (vehicleId.isNullOrBlank() || vehicleId == "") {
                    call.respond(HttpStatusCode.Unauthorized, "Bad request vehicle id is not present")
                } else {
                    val resp = vehicleRepository.updateMileage(vehicleId, mileageRequest.value)
                    if (resp) {
                        call.respond(HttpStatusCode.OK, "Total distance updated successfully")
                    } else {
                        call.respond(HttpStatusCode.InternalServerError, "Server error plese try again later")
                    }
                }
            }

            post("/doorLock") {
                val secret = call.queryParameters.get("secret")
                val vehicleId = call.queryParameters.get("vehicleId")

                if (secret.isNullOrBlank() && secret != "SkodaHexad9922") {
                    call.respond(HttpStatusCode.Unauthorized, "User dont have permissions")
                }

                val doorLockRequest = call.receive<DoorLockRequest>()
                if (vehicleId.isNullOrBlank() || vehicleId == "") {
                    call.respond(HttpStatusCode.Unauthorized, "Bad request vehicle id is not present")
                } else {
                    val resp = vehicleRepository.updateLockStatus(vehicleId, doorLockRequest.value)
                    if (resp) {
                        call.respond(HttpStatusCode.OK, "Door lock updated successfully")
                    } else {
                        call.respond(HttpStatusCode.InternalServerError, "Server error please try again later")
                    }
                }
            }

            post("/engineStatus") {
                val secret = call.queryParameters.get("secret")
                val vehicleId = call.queryParameters.get("vehicleId")

                if (secret.isNullOrBlank() && secret != "SkodaHexad9922") {
                    call.respond(HttpStatusCode.Unauthorized, "User dont have permissions")
                }

                val engineStatusRequest = call.receive<EngineStatusReq>()
                if (vehicleId.isNullOrBlank() || vehicleId == "") {
                    call.respond(HttpStatusCode.Unauthorized, "Bad request vehicle id is not present")
                } else {
                    val engineStsReq = EngineStatusRequest(
                        vehicleId= vehicleId,
                        engineStatus = engineStatusRequest.value,
                        userId = ""
                    )
                    val resp = vehicleRepository.updateEngineStatus( engineStsReq)
                    if (resp) {
                        call.respond(HttpStatusCode.OK, "Door lock updated successfully")
                    } else {
                        call.respond(HttpStatusCode.InternalServerError, "Server error please try again later")
                    }
                }
            }

            post("/fuelLevel") {
                val secret = call.queryParameters.get("secret")
                val vehicleId = call.queryParameters.get("vehicleId")

                if (secret.isNullOrBlank() && secret != "SkodaHexad9922") {
                    call.respond(HttpStatusCode.Unauthorized, "User dont have permissions")
                }

                val fuelRequest = call.receive<FuelRequest>()
                if (vehicleId.isNullOrBlank() || vehicleId == "") {
                    call.respond(HttpStatusCode.Unauthorized, "Bad request vehicle id is not present")
                } else {
                    val resp = vehicleRepository.updateFuelLevel(vehicleId ,fuelRequest.value)
                    if (resp) {
                        call.respond(HttpStatusCode.OK, "Fuel level updated successfully")
                    } else {
                        call.respond(HttpStatusCode.InternalServerError, "Server error please try again later")
                    }
                }
            }

            get("/vehicle") {
                val secret = call.queryParameters.get("secret")
                val vehicleId = call.queryParameters.get("vehicleId")

                if (secret.isNullOrBlank() && secret != "SkodaHexad9922") {
                    call.respond(HttpStatusCode.Unauthorized, "User dont have permissions")
                }
                if (vehicleId.isNullOrBlank() || vehicleId == "") {
                    call.respond(HttpStatusCode.Unauthorized, "Bad request vehicle id is not present")
                } else {
                    val vehicle = vehicleRepository.getVehicleByVehicleId(vehicleId)
                    if (vehicle != null) {
                        call.respond(HttpStatusCode.OK, vehicle)
                    } else {
                        call.respond(HttpStatusCode.NotFound, "Vehicle not found")
                    }
                }
            }

        }
    }
}