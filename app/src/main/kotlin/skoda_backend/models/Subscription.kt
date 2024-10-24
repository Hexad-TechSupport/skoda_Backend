package skoda_backend.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.Table.Dual.long
import org.jetbrains.exposed.sql.Table.Dual.varchar
import skoda_backend.repositories.VehicleHistory
import skoda_backend.repositories.VehicleHistory.autoIncrement

@Serializable
data class SubscriptionRequest(
        val userId: String,
        val vehicleId: String,
        val serviceType: String,
        val validity: Int,
        val renewalDate: Long?,
        val createdAt: Long?,
        val updatedAt: Long?
)
@Serializable
data class Subscription(
        val subscriptionId: Int? = null,
        val userId: String,
        val vehicleId: String,
        val serviceType: String,
        val startDate: Long,
        val endDate: Long,
        val status: String,
        val paymentStatus: String,
        val renewalDate: Long,
        val createdAt: Long,
        val updatedAt: Long
)
object Subscriptions : IdTable<Int>("subscriptions") {
    override val id = integer("id").entityId().autoIncrement() // Use autoIncrement() for SERIAL
    val subscriptionId = integer("subscriptionid").autoIncrement()
    val userId = varchar("userid", 255)
    val vehicleId = varchar("vehicleid", 255)
    val serviceType = varchar("servicetype", 255)
    val startDate = long("startdate")
    val endDate = long("enddate")
    val status = varchar("status", 50)
    val paymentStatus = varchar("paymentstatus", 50)
    val renewalDate = long("renewaldate")
    val createdAt = long("createdat")
    val updatedAt = long("updatedat").nullable()
}
