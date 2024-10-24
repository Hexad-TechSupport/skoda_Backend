package skoda_backend.services

import skoda_backend.models.TravelHistoryResponse
import skoda_backend.repositories.VehicleTravelRepository

class VehicleTravelService (private val vehicleTravelRepo: VehicleTravelRepository){

    fun addTravel(request: TravelHistoryResponse): TravelHistoryResponse {
        return vehicleTravelRepo.addEntry(request)
    }

    fun getTrips(vehicelId: String, userId: String): List<TravelHistoryResponse> {
        return vehicleTravelRepo.getAllTrips(vehicelId, userId)
    }

    fun deleteTrip(tripId: Int): Boolean {
        return vehicleTravelRepo.deleteTravelHistory(tripId)
    }
}