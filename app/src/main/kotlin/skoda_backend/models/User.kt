package skoda_backend.models

import kotlinx.serialization.Contextual
import java.time.LocalDateTime
import java.util.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import skoda_backend.utils.LocalDateTimeSerializer
import skoda_backend.utils.UUIDSerializer

@Serializable// Apply the custom serializers
data class User(
        val userId: String?=null,
        val email: String,
        val passwordHash: String,
        val firstName: String?,
        val lastName: String?,
        val phoneNumber: String?,
        val address: String?,
        val createdAt: Long?=null,
        val updatedAt: Long?= null
)
