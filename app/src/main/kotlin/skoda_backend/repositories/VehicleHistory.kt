package skoda_backend.repositories

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import skoda_backend.models.Users
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import skoda_backend.models.Vehicles

object VehicleHistory : Table("VehicleHistory") {
    val historyId = integer("historyid").autoIncrement()
    val vehicleId = reference("vehicleid", Vehicles.id)
    val userId = reference("userid",Users.id)
    val action = varchar("action", 10)
    val timestamp = long("timestamp")
    val createdAt = long("createdat")
}

