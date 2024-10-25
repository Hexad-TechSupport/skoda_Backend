package skoda_backend.repositories

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import skoda_backend.models.EngineStatusRequest
import skoda_backend.models.Vehicle
import skoda_backend.models.VehicleHistoryRecord
import skoda_backend.models.Vehicles

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
                                lockStatus = it[Vehicles.lockStatus],
                                isEngineOn = it[Vehicles.isEngineOn],
                                updatedAt = it[Vehicles.updatedAt],
                                speed = it[Vehicles.speed],
                                topSpeed = it[Vehicles.topSpeed]
                        )
                    }.singleOrNull()
        }
    }

    fun getVehicleByVehicleId(vehicleId: String): Vehicle? {
        return transaction {
            Vehicles.selectAll().where {  Vehicles.id eq vehicleId }
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
                                lockStatus = it[Vehicles.lockStatus],
                                isEngineOn = it[Vehicles.isEngineOn],
                                updatedAt = it[Vehicles.updatedAt],
                                speed = it[Vehicles.speed],
                                topSpeed = it[Vehicles.topSpeed]
                        )
                    }.singleOrNull()
        }
    }


    fun lockVehicle(vehicleId: String, userId: String): Boolean {
        return transaction {
            Vehicles.update({ Vehicles.id eq vehicleId }) {
                it[lockStatus] = "locked"
            }

            VehicleHistory.insert {
                it[VehicleHistory.vehicleId] = vehicleId
                it[VehicleHistory.userId] = userId
                it[action] = "lock"
                it[timestamp] = System.currentTimeMillis()
                it[createdAt] = System.currentTimeMillis()
            }
            true
        }
    }

    fun unlockVehicle(vehicleId: String, userId: String): Boolean {
        return transaction {
            Vehicles.update({ Vehicles.id eq vehicleId }) {
                it[lockStatus] = "unlocked"
            }

            VehicleHistory.insert {
                it[VehicleHistory.vehicleId] = vehicleId
                it[VehicleHistory.userId] = userId
                it[action] = "unlock"
                it[timestamp] = System.currentTimeMillis()
                it[createdAt] = System.currentTimeMillis()
            }
            true
        }
    }

    fun updateEngineStatus(engineStatusRequest: EngineStatusRequest): Boolean {
        return transaction {
            Vehicles.update({ Vehicles.id eq engineStatusRequest.vehicleId }) {
                it[Vehicles.isEngineOn] = engineStatusRequest.engineStatus
            }
            true
        }
    }

    fun updateFuelLevel(vehicleId: String,fuelLevel: Float): Boolean {
        return transaction {
            Vehicles.update({ Vehicles.id eq vehicleId }) {
                it[Vehicles.fuelLevel] = fuelLevel
            }
            true
        }
    }

    fun updateSpeed(vehicleId: String,speed: Float): Boolean {
        return transaction {
            Vehicles.update({ Vehicles.id eq vehicleId }) {
                it[Vehicles.speed] = speed
            }
            true
        }
    }

    fun updateMileage(vehicleId: String,mileage: Int): Boolean {
        return transaction {
            Vehicles.update({ Vehicles.id eq vehicleId }) {
                it[Vehicles.mileage] = mileage
            }
            true
        }
    }

    fun updateLockStatus(vehicleId: String, status: Boolean): Boolean {

        var lockStatus = "locked"
        if (!status){
            lockStatus = "unlocked"
        }
        return transaction {
            Vehicles.update({ Vehicles.id eq vehicleId }) {
                it[Vehicles.lockStatus] = lockStatus
            }

            VehicleHistory.insert {
                it[VehicleHistory.vehicleId] = vehicleId
                it[action] = "lock"
                it[timestamp] = System.currentTimeMillis()
                it[createdAt] = System.currentTimeMillis()
            }
            true
        }
    }


    fun getVehicleHistory(vehicleId: String): List<VehicleHistoryRecord> {
        return transaction {
            VehicleHistory.selectAll().where { VehicleHistory.vehicleId eq vehicleId }
                    .map {
                        VehicleHistoryRecord(
                                vehicleId = it[VehicleHistory.vehicleId].value,
                                userId = it[VehicleHistory.userId].value,
                                action = it[VehicleHistory.action],
                                timestamp = it[VehicleHistory.timestamp],
                                createdAt = it[VehicleHistory.createdAt]
                        )
                    }
        }
    }
}
