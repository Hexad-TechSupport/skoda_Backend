package skoda_backend.repositories

import org.jetbrains.exposed.sql.Table
import skoda_backend.models.Users
import skoda_backend.models.Vehicles

object VehicleHistory : Table("VehicleHistory") {
    val historyId = integer("historyid").autoIncrement()
    val vehicleId = reference("vehicleid", Vehicles.id)
    val userId = reference("userid",Users.id)
    val action = varchar("action", 10)
    val timestamp = long("timestamp")
    val createdAt = long("createdat")
}

