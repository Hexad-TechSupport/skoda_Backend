package skoda_backend.services

import skoda_backend.models.Subscription
import skoda_backend.models.SubscriptionRequest
import skoda_backend.repositories.SubscriptionRepository
import java.time.ZoneId
import java.time.LocalDate

class SubscriptionService(private val repository: SubscriptionRepository) {

    fun createSubscription(subscriptionReq: SubscriptionRequest): Subscription? {

        val currentDate = LocalDate.now()
        val validThrough = currentDate.plusMonths(subscriptionReq.validity.toLong())

        // Convert the date to milliseconds since epoch
        val milliseconds = validThrough.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()

        val subscription = Subscription(
                userId = subscriptionReq.userId,
                vehicleId = subscriptionReq.vehicleId,
                serviceType = subscriptionReq.serviceType,
                startDate = System.currentTimeMillis(),
                endDate = milliseconds,
                status = "Active",
                renewalDate = subscriptionReq.renewalDate ?: System.currentTimeMillis(),
                createdAt = subscriptionReq.createdAt ?: System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis(),
                paymentStatus = "Success"
        )

        return repository.createSubscription(subscription)
    }

    fun getSubscriptionsByUserAndVehicle(userId: String, vehicleId: String): List<Subscription> {
        return repository.getSubscriptionsByUserAndVehicle(userId, vehicleId)
    }

    fun getSubscriptionById(subscriptionId: Int): Subscription? {
        return repository.getSubscriptionById(subscriptionId)
    }

    fun updateSubscription(subscriptionId: Int, subscriptionReq: SubscriptionRequest): Boolean {
        val currentDate = LocalDate.now()
        val validThrough = currentDate.plusMonths(subscriptionReq.validity.toLong())

        // Convert the date to milliseconds since epoch
        val milliseconds = validThrough.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()

        val subscription = Subscription(
                userId = subscriptionReq.userId,
                vehicleId = subscriptionReq.vehicleId,
                serviceType = subscriptionReq.serviceType,
                startDate = System.currentTimeMillis(),
                endDate = milliseconds,
                status = "Active",
                renewalDate = subscriptionReq.renewalDate ?: System.currentTimeMillis(),
                createdAt = subscriptionReq.createdAt ?: System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis(),
                paymentStatus = "Success"
        )

        return repository.updateSubscription(subscriptionId, subscription)
    }

    fun deleteSubscription(subscriptionId: Int): Boolean {
        return repository.deleteSubscription(subscriptionId)
    }
}
