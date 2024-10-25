package skoda_backend.models

import kotlinx.serialization.Serializable

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
        val lockStatus: String?,
        val isEngineOn: Boolean?
)

@Serializable
data class VehicleHistoryRecord(
        var vehicleId: String,
        var userId : String,
        var action : String,
        var timestamp: Long,
        var createdAt: Long,
)

@Serializable
data class EngineStatusRequest(
        val userId: String,
        val engineStatus: Boolean,
        val vehicleId: String
)

@Serializable
data class CarTopSpeed(
        val id: Int,
        val make: String,
        val model: String,
        val country: String,
        val topSpeedKmh: Int,
        val topSpeedMph: Int
)
