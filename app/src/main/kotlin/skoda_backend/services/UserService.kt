package skoda_backend.services

import skoda_backend.models.User
import skoda_backend.repositories.UserRepository

class UserService(private val userRepository: UserRepository) {
    fun createUser(user: User): User = userRepository.createUser(user)

    fun getUserById(id: String): User? = userRepository.getUserById(id)

    fun updateUser(user: User): User? = userRepository.updateUser(user)

    fun deleteUser(id: String): Boolean = userRepository.deleteUser(id)

    fun getAllUsers(): List<User> = userRepository.getAllUsers()

    fun getUserByEmail(email: String): User? {
        return userRepository.findUserByEmail(email)
    }
}
