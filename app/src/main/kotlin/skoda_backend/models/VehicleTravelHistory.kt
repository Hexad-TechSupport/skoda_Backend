package skoda_backend.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IdTable

object TravelHistory : IdTable<Int>("travelhistory") {
    override val id = integer("historyid").autoIncrement().entityId()
    val vehicleId = varchar("vehicleid", 255)
    val userId = varchar("userid", 255)
    val startLatitude = double("startlatitude")
    val startLongitude = double("startlongitude")
    val endLatitude = double("endlatitude")
    val endLongitude = double("endlongitude")
    val travelTime = long("traveltime")
    val distanceTravelled = double("distancetravelled")
    val createdAt = long("createdat")
}

@Serializable
data class TravelHistoryResponse(
        val historyId: Int? = null,           // Auto-incrementing ID for each trip (nullable for creation)
        val vehicleId: String,                // ID of the vehicle
        val userId: String,                   // ID of the user
        val startLatitude: Double,             // Starting latitude
        val startLongitude: Double,            // Starting longitude
        val endLatitude: Double,               // Ending latitude
        val endLongitude: Double,              // Ending longitude
        val travelTime: Long,                  // Travel time in seconds or milliseconds
        val distanceTravelled: Double,         // Distance traveled in kilometers/miles
        val createdAt: Long? = null,                    // Timestamp when the trip record was created
)

