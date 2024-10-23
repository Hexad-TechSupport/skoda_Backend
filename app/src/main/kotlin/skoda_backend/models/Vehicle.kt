package skoda_backend.models

import kotlinx.serialization.Serializable
import skoda_backend.repositories.VehicleHistory
import java.util.*
import java.time.LocalDateTime

@Serializable
data class Vehicle(
        val vehicleId: String,
        val userId: String,
        val make: String?,
        val model: String?,
        val year: Int?,
        val licensePlate: String?,
        val vin: String?,
        val fuelLevel: Float?,
        val latitude: Float?,
        val longitude: Float?,
        val mileage: Int?,
        val status: String?,
        val lastServiceDate: Long?,
        val createdAt: Long,
        val updatedAt: Long?,
        val lockStatus: String?
)

@Serializable
data class VehicleHistoryRecord(
        var vehicleId: String,
        var userId : String,
        var action : String,
        var timestamp: Long,
        var createdAt: Long,
)
