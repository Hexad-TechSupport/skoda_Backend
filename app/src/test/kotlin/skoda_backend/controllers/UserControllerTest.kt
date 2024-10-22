package skoda_backend.controllers

import skoda_backend.models.User
import skoda_backend.services.UserService
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.testing.*
import io.mockk.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class UserControllerTest {

    private lateinit var userService: UserService
    private lateinit var userController: UserController

    @BeforeEach
    fun setup() {
        userService = mockk(relaxed = true)
        userController = UserController(userService)
    }

    @Test
    fun testCreateUser() {
        val user = User(UUID.randomUUID(), "test@example.com", "hashedPassword", "John", "Doe", "1234567890", "123 Main St", "2024-10-22T10:00:00Z", null)

        withTestApplication(Application::module) {
            val response = handleRequest(HttpMethod.Post, "/users") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody(Json.encodeToString(user))
            }

            verify { userService.createUser(user) }
            assertEquals(HttpStatusCode.Created, response.response.status())
        }
    }

    @Test
    fun testGetUser() {
        val userId = UUID.randomUUID()
        val user = User(userId, "test@example.com", "hashedPassword", "John", "Doe", "1234567890", "123 Main St", "2024-10-22T10:00:00Z", null)

        every { userService.getUser(userId) } returns user

        withTestApplication(Application::module) {
            val response = handleRequest(HttpMethod.Get, "/users/$userId") {}

            verify { userService.getUser(userId) }
            assertEquals(HttpStatusCode.OK, response.response.status())
            // Further assertions can be added to verify the user details
        }
    }

    @Test
    fun testUpdateUser() {
        val userId = UUID.randomUUID()
        val user = User(userId, "updated@example.com", "hashedPassword", "Jane", "Smith", "0987654321", "456 Main St", "2024-10-22T10:00:00Z", null)

        every { userService.updateUser(userId, user) } returns true

        withTestApplication(Application::module) {
            val response = handleRequest(HttpMethod.Put, "/users/$userId") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody(Json.encodeToString(user))
            }

            verify { userService.updateUser(userId, user) }
            assertEquals(HttpStatusCode.OK, response.response.status())
        }
    }

    @Test
    fun testDeleteUser() {
        val userId = UUID.randomUUID()

        every { userService.deleteUser(userId) } returns true

        withTestApplication(Application::module) {
            val response = handleRequest(HttpMethod.Delete, "/users/$userId") {}

            verify { userService.deleteUser(userId) }
            assertEquals(HttpStatusCode.NoContent, response.response.status())
        }
    }
}

class users {
}