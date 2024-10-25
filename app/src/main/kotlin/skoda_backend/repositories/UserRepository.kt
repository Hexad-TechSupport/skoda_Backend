package skoda_backend.repositories

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import skoda_backend.models.User
import skoda_backend.models.Users
import org.mindrot.jbcrypt.BCrypt
import java.util.UUID

class UserRepository {

    init {
        // Create the Users table if it doesn't exist
        transaction {
            SchemaUtils.create(Users)
        }
    }

    // Create a new user
    fun createUser(user: User): User {
        return transaction {
            val id = Users.insertAndGetId {
                it[id] = UUID.randomUUID().toString()
                it[email] = user.email
                it[passwordHash] = BCrypt.hashpw(user.password, BCrypt.gensalt())
                it[firstName] = user.firstName
                it[lastName] = user.lastName
                it[phoneNumber] = user.phoneNumber
                it[address] = user.address
                it[createdAt] = System.currentTimeMillis()
                it[updatedAt] = null
            }
            user.copy(userId = id.value, updatedAt = null)
        }
    }

    // Get a user by ID
    fun getUserById(id: String): User? {
        return transaction {
            Users.selectAll().where { Users.id eq id }
                    .mapNotNull {
                        User(
                                userId = it[Users.id].value,
                                email = it[Users.email],
                                password = it[Users.passwordHash],
                                firstName = it[Users.firstName],
                                lastName = it[Users.lastName],
                                phoneNumber = it[Users.phoneNumber],
                                address = it[Users.address],
                                createdAt = it[Users.createdAt],
                                updatedAt = it[Users.updatedAt]
                        )
                    }.singleOrNull()
        }
    }

    fun findUserByEmail(email: String): User? {
        return transaction {
            Users.selectAll().where { Users.email eq email }.map {
                User(
                        userId = it[Users.id].value,
                        email = it[Users.email],
                        password = it[Users.passwordHash],
                        firstName = it[Users.firstName],
                        lastName = it[Users.lastName],
                        phoneNumber = it[Users.phoneNumber],
                        address = it[Users.address]
                )
            }.singleOrNull()
        }
    }
    // Get all users
    fun getAllUsers(): List<User> {
        return transaction {
            Users.selectAll().map {
                User(
                        userId = it[Users.id].value,
                        email = it[Users.email],
                        password = it[Users.passwordHash],
                        firstName = it[Users.firstName],
                        lastName = it[Users.lastName],
                        phoneNumber = it[Users.phoneNumber],
                        address = it[Users.address],
                        createdAt = it[Users.createdAt],
                        updatedAt = it[Users.updatedAt]
                )
            }
        }
    }

    // Update an existing user
    fun updateUser(updatedUser: User): User? {
        return transaction {
            val updatedRowsCount = Users.update({ Users.id eq updatedUser.userId }) {
                it[email] = updatedUser.email
                if (!updatedUser.password.isNullOrEmpty()) {
                    it[passwordHash] = BCrypt.hashpw(updatedUser.password, BCrypt.gensalt())
                }
                it[firstName] = updatedUser.firstName
                it[lastName] = updatedUser.lastName
                it[phoneNumber] = updatedUser.phoneNumber
                it[address] = updatedUser.address
                it[updatedAt] = System.currentTimeMillis()
            }
            if (updatedRowsCount > 0) updatedUser else null
        }
    }

    // Delete a user by ID
    fun deleteUser(id: String): Boolean {
        return transaction {
            // Check if any row was deleted and return true if so
            val rowsDeleted = Users.deleteWhere { Users.id eq id }
            rowsDeleted > 0
        }
    }

}
