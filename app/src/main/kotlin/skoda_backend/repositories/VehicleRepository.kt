import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import java.util.*
import org.jetbrains.exposed.sql.and

class VehicleRepository {
    fun getVehicleByUserAndVehicleId(userId: String, vehicleId: String): Vehicle? {
        return transaction {
            Vehicles.selectAll().where { (Vehicles.userId eq userId) and (Vehicles.id eq vehicleId) }
                    .map {
                        Vehicle(
                                vehicleId = it[Vehicles.id].value,
                                userId = it[Vehicles.userId].value,
                                make = it[Vehicles.make],
                                model = it[Vehicles.model],
                                year = it[Vehicles.year],
                                licensePlate = it[Vehicles.licensePlate],
                                vin = it[Vehicles.vin],
                                fuelLevel = it[Vehicles.fuelLevel],
                                latitude = it[Vehicles.latitude],
                                longitude = it[Vehicles.longitude],
                                mileage = it[Vehicles.mileage],
                                status = it[Vehicles.status],
                                lastServiceDate = it[Vehicles.lastServiceDate],
                                createdAt = it[Vehicles.createdAt],
                                updatedAt = it[Vehicles.updatedAt]
                        )
                    }.singleOrNull()
        }
    }
}
