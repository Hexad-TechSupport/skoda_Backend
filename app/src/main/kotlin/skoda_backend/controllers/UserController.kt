package skoda_backend.controllers

import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.http.*
import skoda_backend.models.User
import skoda_backend.repositories.UserRepository
import skoda_backend.services.UserService
import kotlinx.serialization.serializer
import java.util.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString

fun Application.userRoutes() {

    val userRepository = UserRepository()
    val userService = UserService(userRepository)

    routing {
        route("/users") {
            // Create User
            post {
                //val user = call.receive<User>()
                val user = call.receive<String>() // Receive raw JSON string
                val deserializedUser = Json.decodeFromString<User>(user)
                val createdUser = userService.createUser(deserializedUser)
                val jsonResponse = Json.encodeToString(createdUser)
                call.respond(HttpStatusCode.Created, jsonResponse)
            }

            // Get all Users
            get {
                val users = userService.getAllUsers()
                call.respond(HttpStatusCode.OK, Json.encodeToString(users))
            }

            // Get User by ID
            get("/{id}") {
                val id = call.parameters["id"]
                val user = id?.let { userService.getUserById(it) }
                if (user != null) {
                    call.respond(HttpStatusCode.OK, Json.encodeToString(user))
                } else {
                    call.respond(HttpStatusCode.NotFound, "User not found")
                }
            }

            // Update User
            put("/{id}") {
                val id = call.parameters["id"]
                //val updatedUser = call.receive<User>()
                val updatedUser = call.receive<String>() // Receive raw JSON string
                val deserializedUser = Json.decodeFromString<User>(updatedUser)
                val user = id?.let { userService.updateUser(deserializedUser.copy(userId = it)) }
                if (user != null) {
                    call.respond(HttpStatusCode.Created, Json.encodeToString(user))
                } else {
                    call.respond(HttpStatusCode.NotFound, "User not found")
                }
            }

            // Delete User
            delete("/{id}") {
                val id = call.parameters["id"]
                val success = id?.let { userService.deleteUser(it) }
                if (success == true) {
                    call.respond(HttpStatusCode.OK, "User deleted successfully")
                } else {
                    call.respond(HttpStatusCode.NotFound, "User not found")
                }
            }
        }
    }
}
