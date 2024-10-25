package skoda_backend.controllers

import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import skoda_backend.models.SubscriptionRequest
import skoda_backend.repositories.SubscriptionRepository
import skoda_backend.services.SubscriptionService

fun Application.subscriptionRoutes() {

    val subscriptionRepository = SubscriptionRepository()
    val subscriptionService =  SubscriptionService(subscriptionRepository)
    routing {
        route("/subscriptions") {
            authenticate("auth-jwt") {
                // Create Subscription
                post {
                    val subReq = call.receive<SubscriptionRequest>()
                    val createdSubscription = subscriptionService.createSubscription(subReq)
                    if (createdSubscription != null) {
                        call.respond(HttpStatusCode.Created, createdSubscription)
                    } else {
                        call.respond(HttpStatusCode.BadRequest, "Unable to create subscription")
                    }
                }

                // Get all subscriptions for a user and vehicle
                get("/{userId}/{vehicleId}") {
                    val userId = call.parameters["userId"]
                    val vehicleId = call.parameters["vehicleId"]
                    val principal = call.principal<JWTPrincipal>()
                    val uId = principal?.getClaim("userId", String::class)
                    if (uId != userId){
                        call.respond(HttpStatusCode.Unauthorized, "User dont have permissions")
                    }

                    if (userId != null && vehicleId != null) {
                        val subscriptions = subscriptionService.getSubscriptionsByUserAndVehicle(userId, vehicleId)
                        call.respond(subscriptions)
                    } else {
                        call.respond(HttpStatusCode.BadRequest, "Missing userId or vehicleId")
                    }
                }

                // Get a subscription by ID
                get("/{subscriptionId}") {
                    val subscriptionId = call.parameters["subscriptionId"]?.toIntOrNull()
                    if (subscriptionId != null) {
                        val subscription = subscriptionService.getSubscriptionById(subscriptionId)
                        if (subscription != null) {
                            call.respond(subscription)
                        } else {
                            call.respond(HttpStatusCode.NotFound, "Subscription not found")
                        }
                    } else {
                        call.respond(HttpStatusCode.BadRequest, "Invalid subscription ID")
                    }
                }

                // Update a subscription
                put("/{subscriptionId}") {
                    val subscriptionId = call.parameters["subscriptionId"]?.toIntOrNull()
                    if (subscriptionId != null) {
                        val subReq = call.receive<SubscriptionRequest>()
                        val isUpdated = subscriptionService.updateSubscription(subscriptionId, subReq)
                        if (isUpdated) {
                            call.respond(HttpStatusCode.OK, "Subscription updated successfully")
                        } else {
                            call.respond(HttpStatusCode.BadRequest, "Unable to update subscription")
                        }
                    } else {
                        call.respond(HttpStatusCode.BadRequest, "Invalid subscription ID")
                    }
                }

                // Delete a subscription
                delete("/{subscriptionId}") {
                    val subscriptionId = call.parameters["subscriptionId"]?.toIntOrNull()
                    if (subscriptionId != null) {
                        val isDeleted = subscriptionService.deleteSubscription(subscriptionId)
                        if (isDeleted) {
                            call.respond(HttpStatusCode.OK, "Subscription deleted successfully")
                        } else {
                            call.respond(HttpStatusCode.BadRequest, "Unable to delete subscription")
                        }
                    } else {
                        call.respond(HttpStatusCode.BadRequest, "Invalid subscription ID")
                    }
                }
            }
        }
    }
}
