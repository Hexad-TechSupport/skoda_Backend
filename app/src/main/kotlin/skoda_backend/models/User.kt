package skoda_backend.models
import java.util.*

data class User(
        val userId: UUID,
        val email: String,
        val passwordHash: String,
        val firstName: String?,
        val lastName: String?,
        val phoneNumber: String?,
        val address: String?,
        val createdAt: String,
        val updatedAt: String?
)
