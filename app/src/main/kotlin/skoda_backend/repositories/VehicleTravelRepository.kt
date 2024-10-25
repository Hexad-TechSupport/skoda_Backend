package skoda_backend.repositories

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import skoda_backend.models.TravelHistory
import skoda_backend.models.TravelHistoryResponse

class VehicleTravelRepository {

    fun addEntry(request: TravelHistoryResponse): TravelHistoryResponse {
        return transaction {
            val id = TravelHistory.insertAndGetId {
                it[vehicleId] = request.vehicleId
                it[userId] = request.userId
                it[startLatitude] = request.startLatitude
                it[startLongitude] = request.startLongitude
                it[endLatitude] = request.endLatitude
                it[endLongitude] = request.endLongitude
                it[travelTime] = request.travelTime
                it[distanceTravelled] = request.distanceTravelled
                it[createdAt] = System.currentTimeMillis()
            }
            request.copy(historyId = id.value)
        }
    }

    fun getAllTrips(vehicleId: String, userId: String): List<TravelHistoryResponse> {
        return transaction {
            TravelHistory
                    .selectAll().where { (TravelHistory.vehicleId eq vehicleId) and (TravelHistory.userId eq userId) }
                    .map {
                        TravelHistoryResponse(
                                historyId = it[TravelHistory.id].value,
                                vehicleId = it[TravelHistory.vehicleId],
                                userId = it[TravelHistory.userId],
                                startLatitude = it[TravelHistory.startLatitude],
                                startLongitude = it[TravelHistory.startLongitude],
                                endLatitude = it[TravelHistory.endLatitude],
                                endLongitude = it[TravelHistory.endLongitude],
                                travelTime = it[TravelHistory.travelTime],
                                distanceTravelled = it[TravelHistory.distanceTravelled],
                                createdAt = it[TravelHistory.createdAt]
                        )
                    }
        }
    }

    fun deleteTravelHistory(historyId: Int): Boolean {
        return transaction {
            TravelHistory.deleteWhere { TravelHistory.id eq historyId } > 0
        }
    }

}
