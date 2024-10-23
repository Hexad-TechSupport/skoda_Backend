import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.javatime.datetime
import skoda_backend.models.User
import skoda_backend.models.Users
import skoda_backend.models.Users.entityId

object Vehicles : IdTable<String>("Vehicles") {
    override val id = varchar("vehicleId", 255).entityId()
    val userId = reference("userId", Users.id)
    val make = varchar("make", 255).nullable()
    val model = varchar("model", 255).nullable()
    val year = integer("year").nullable()
    val licensePlate = varchar("licensePlate", 20).nullable()
    val vin = varchar("vin", 255).nullable()
    val fuelLevel = float("fuelLevel").nullable()
    val latitude = float("latitude").nullable()
    val longitude = float("longitude").nullable()
    val mileage = integer("mileage").nullable()
    val status = varchar("status", 50).nullable()
    val lastServiceDate = long("lastServiceDate").nullable()
    val createdAt = long("createdAt")
    val updatedAt = long("updatedAt").nullable()
}
