package skoda_backend.models

import kotlinx.serialization.Contextual
import java.time.LocalDateTime
import java.util.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers

@Serializable
data class User(
        val userId: String?=null,
        val email: String,
        val password: String,
        val firstName: String? = null,
        val lastName: String? = null,
        val phoneNumber: String? = null,
        val address: String? = null,
        val createdAt: Long?=null,
        val updatedAt: Long?= null
)

// User model used in the response (without passwordHash)
@Serializable
data class UserResponse(
        val userId: String? = null,
        val email: String,
        val firstName: String? = null,
        val lastName: String? = null,
        val phoneNumber: String? = null,
        val address: String? = null,
        val createdAt: Long?=null,
        val updatedAt: Long?= null
)

// Utility function to convert a User to UserResponse
fun User.toUserResponse(): UserResponse {
    return UserResponse(
            userId = this.userId,
            email = this.email,
            firstName = this.firstName,
            lastName = this.lastName,
            phoneNumber = this.phoneNumber,
            address = this.address,
            createdAt = this.createdAt,
            updatedAt = this.updatedAt
    )
}

@Serializable
data class LoginRequest(
        val email: String,
        val password: String
)

@Serializable
data class TokenResponse(val token: String)
