package skoda_backend.services

import skoda_backend.models.VehicleHistoryRecord
import skoda_backend.repositories.VehicleRepository


class VehicleService(private val vehicleRepo: VehicleRepository) {

    fun lockVehicle(vehicleId: String, userId: String): Boolean {
        return vehicleRepo.lockVehicle(vehicleId, userId)
    }

    fun unlockVehicle(vehicleId: String, userId: String): Boolean {
        return vehicleRepo.unlockVehicle(vehicleId, userId)
    }

    fun getVehicleHistory(vehicleId: String): List<VehicleHistoryRecord> {
        return vehicleRepo.getVehicleHistory(vehicleId)
    }
}
