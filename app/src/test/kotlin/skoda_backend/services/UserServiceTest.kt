//package skoda_backend.services
//
//import skoda_backend.models.User
//import skoda_backend.repositories.UserRepository
//import skoda_backend.services.UserService
//import io.mockk.*
//import org.junit.jupiter.api.BeforeEach
//import org.junit.jupiter.api.Test
//import java.util.*
//
//class UserServiceTest {
//
//    private lateinit var userRepository: UserRepository
//    private lateinit var userService: UserService
//
//    @BeforeEach
//    fun setup() {
//        userRepository = mockk()
//        userService = UserService(userRepository)
//    }
//
//    @Test
//    fun testCreateUser() {
//        val user = User(UUID.randomUUID(), "test@example.com", "hashedPassword", "John", "Doe", "1234567890", "123 Main St", "2024-10-22T10:00:00Z", null)
//
//        every { userRepository.create(user) } returns user
//
//        val createdUser = userService.createUser(user)
//
//        verify { userRepository.create(user) }
//        assert(createdUser.email == user.email)
//    }
//
//    @Test
//    fun testGetUser() {
//        val userId = UUID.randomUUID()
//        val user = User(userId, "test@example.com", "hashedPassword", "John", "Doe", "1234567890", "123 Main St", "2024-10-22T10:00:00Z", null)
//
//        every { userRepository.findById(userId) } returns user
//
//        val retrievedUser = userService.getUser(userId)
//
//        verify { userRepository.findById(userId) }
//        assert(retrievedUser?.email == user.email)
//    }
//
//    @Test
//    fun testUpdateUser() {
//        val userId = UUID.randomUUID()
//        val user = User(userId, "updated@example.com", "hashedPassword", "Jane", "Smith", "0987654321", "456 Main St", "2024-10-22T10:00:00Z", null)
//
//        every { userRepository.update(userId, user) } returns true
//
//        val updated = userService.updateUser(userId, user)
//
//        verify { userRepository.update(userId, user) }
//        assert(updated)
//    }
//
//    @Test
//    fun testDeleteUser() {
//        val userId = UUID.randomUUID()
//
//        every { userRepository.delete(userId) } returns true
//
//        val deleted = userService.deleteUser(userId)
//
//        verify { userRepository.delete(userId) }
//        assert(deleted)
//    }
//}
