package skoda_backend.repositories

import skoda_backend.models.User
import skoda_backend.repositories.UserRepository
import io.mockk.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class UserRepositoryTest {

    private lateinit var userRepository: UserRepository

    @BeforeEach
    fun setup() {
        Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;", driver = "org.h2.Driver")
        transaction {
            SchemaUtils.create(Users) // Assuming you have a Users table created
        }
        userRepository = UserRepository()
    }

    @Test
    fun `create should add user to the database`() {
        val user = User(
                userId = UUID.randomUUID(),
                email = "test@example.com",
                passwordHash = "hashedPassword",
                firstName = "John",
                lastName = "Doe",
                phoneNumber = "1234567890",
                address = "123 Main St",
                createdAt = "2024-10-22T10:00:00Z",
                updatedAt = null
        )

        userRepository.create(user)

        val createdUser = transaction {
            Users.select { Users.email eq user.email }.singleOrNull()?.let {
                toUser(it)
            }
        }

        assert(createdUser != null)
        assert(createdUser?.email == user.email)
    }

    @Test
    fun `findById should return user when exists`() {
        val user = User(
                userId = UUID.randomUUID(),
                email = "test@example.com",
                passwordHash = "hashedPassword",
                firstName = "John",
                lastName = "Doe",
                phoneNumber = "1234567890",
                address = "123 Main St",
                createdAt = "2024-10-22T10:00:00Z",
                updatedAt = null
        )

        userRepository.create(user)

        val foundUser = userRepository.findById(user.userId)

        assert(foundUser != null)
        assert(foundUser?.email == user.email)
    }

    @Test
    fun `update should modify existing user`() {
        val user = User(
                userId = UUID.randomUUID(),
                email = "test@example.com",
                passwordHash = "hashedPassword",
                firstName = "John",
                lastName = "Doe",
                phoneNumber = "1234567890",
                address = "123 Main St",
                createdAt = "2024-10-22T10:00:00Z",
                updatedAt = null
        )

        userRepository.create(user)

        val updatedUser = user.copy(email = "updated@example.com")
        userRepository.update(user.userId, updatedUser)

        val foundUser = userRepository.findById(user.userId)

        assert(foundUser != null)
        assert(foundUser?.email == updatedUser.email)
    }

    @Test
    fun `delete should remove user from the database`() {
        val user = User(
                userId = UUID.randomUUID(),
                email = "test@example.com",
                passwordHash = "hashedPassword",
                firstName = "John",
                lastName = "Doe",
                phoneNumber = "1234567890",
                address = "123 Main St",
                createdAt = "2024-10-22T10:00:00Z",
                updatedAt = null
        )

        userRepository.create(user)

        userRepository.delete(user.userId)

        val foundUser = userRepository.findById(user.userId)

        assert(foundUser == null)
    }
}
