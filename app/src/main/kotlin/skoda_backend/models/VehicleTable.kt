package skoda_backend.models

import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Table

object Vehicles : IdTable<String>("Vehicles") {
    override val id = varchar("vehicleId", 255).entityId()
    val userId = reference("userId", Users.id)
    val make = varchar("make", 255).nullable()
    val model = varchar("model", 255).nullable()
    val year = integer("year").nullable()
    val licensePlate = varchar("licensePlate", 20).nullable()
    val vin = varchar("vin", 255).nullable()
    val fuelLevel = float("fuelLevel").nullable()
    val speed = float("speed").nullable()
    val topSpeed = float("topspeed").nullable()
    val latitude = float("latitude").nullable()
    val longitude = float("longitude").nullable()
    val mileage = integer("mileage").nullable()
    val status = varchar("status", 50).nullable()
    val lastServiceDate = long("lastServiceDate").nullable()
    val createdAt = long("createdAt")
    val updatedAt = long("updatedAt").nullable()
    val lockStatus = varchar("lockstatus", 50).nullable()
    var isEngineOn = bool("isEngineOn").nullable()
}

object CarTopSpeeds : Table("car_top_speeds") {
    val id = integer("id").autoIncrement()
    val make = varchar("make", 255)
    val model = varchar("model", 255)
    val country = varchar("country", 255)
    val topSpeedKmh = integer("top_speed_kmh")
    val topSpeedMph = integer("top_speed_mph")
}