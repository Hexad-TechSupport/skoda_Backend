package skoda_backend.controllers

import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import skoda_backend.models.User
import skoda_backend.repositories.UserRepository
import skoda_backend.services.UserService
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import skoda_backend.models.LoginRequest
import skoda_backend.models.toUserResponse
import org.mindrot.jbcrypt.BCrypt
import skoda_backend.models.TokenResponse
import skoda_backend.utils.generateToken
import skoda_backend.utils.isValidEmail

fun Application.userRoutes() {

    val userRepository = UserRepository()
    val userService = UserService(userRepository)

    routing {
        route("/users") {
            // Create User
            post {
                val user = call.receive<User>()

                if (!isValidEmail(user.email)) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid email format")
                    return@post
                }
                if (user.password.length < 8) {
                    call.respond(HttpStatusCode.BadRequest, "Password must be at least 8 characters long")
                    return@post
                }
                val createdUser = userService.createUser(user)
                //val jsonResponse = Json.encodeToString(createdUser.toUserResponse())
                call.respond(HttpStatusCode.Created, createdUser)
            }

            authenticate("auth-jwt"){

                // Get all Users
                get {
                    val users = userService.getAllUsers()
                    call.respond(HttpStatusCode.OK, users)
                }

                // Get User by ID
                get("/{id}") {
                    val principal = call.principal<JWTPrincipal>()
                    val userId = principal?.getClaim("userId", String::class)
                    val id = call.parameters["id"]

                    if (userId != id){
                        call.respond(HttpStatusCode.Unauthorized, "User dont have permissions")
                    }
                    val user = id?.let { userService.getUserById(it) }
                    if (user != null) {
                        call.respond(HttpStatusCode.OK, user)
                    } else {
                        call.respond(HttpStatusCode.NotFound, "User not found")
                    }
                }

                // Update User
                put("/{id}") {
                    val id = call.parameters["id"]
                    val updatedUser = call.receive<User>()

                    val principal = call.principal<JWTPrincipal>()
                    val userId = principal?.getClaim("userId", String::class)

                    if (userId != id){
                        call.respond(HttpStatusCode.Unauthorized, "User dont have permissions")
                    }
                    val user = id?.let { userService.updateUser(updatedUser.copy(userId = it)) }
                    if (user != null) {
                        call.respond(HttpStatusCode.Created, Json.encodeToString(user.toUserResponse()))
                    } else {
                        call.respond(HttpStatusCode.NotFound, "User not found")
                    }
                }

                // Delete User
                delete("/{id}") {
                    val id = call.parameters["id"]
                    val principal = call.principal<JWTPrincipal>()
                    val userId = principal?.getClaim("userId", String::class)
                    if (userId != id){
                        call.respond(HttpStatusCode.Unauthorized, "User dont have permissions")
                    }
                    val success = id?.let { userService.deleteUser(it) }
                    if (success == true) {
                        call.respond(HttpStatusCode.OK, "User deleted successfully")
                    } else {
                        call.respond(HttpStatusCode.NotFound, "User not found")
                    }
                }
            }
            // Sign in (login)
            post("/login") {
                val loginRequest = call.receive<LoginRequest>()
                val user = userService.getUserByEmail(loginRequest.email)
                if (user == null) {
                    call.respond(HttpStatusCode.Unauthorized, "Invalid email or password")
                    return@post
                }

                // Check if the password matches the hashed password
                if (!BCrypt.checkpw(loginRequest.password, user.password)) {
                    call.respond(HttpStatusCode.Unauthorized, "Invalid email or password")
                    return@post
                }

                if (user.userId != null) {
                    val token = generateToken(user.userId)
                    call.respondText(
                            Json.encodeToString(TokenResponse(token)),
                            contentType = ContentType.Application.Json
                    )
                }
            }
        }
    }
}
