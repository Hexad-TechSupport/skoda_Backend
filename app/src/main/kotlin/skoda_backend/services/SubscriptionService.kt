package skoda_backend.services

import skoda_backend.models.Subscription
import skoda_backend.repositories.SubscriptionRepository

class SubscriptionService(private val repository: SubscriptionRepository) {

    fun createSubscription(subscription: Subscription): Subscription? {
        // Business logic can be added here
        return repository.createSubscription(subscription)
    }

    fun getSubscriptionsByUserAndVehicle(userId: String, vehicleId: String): List<Subscription> {
        return repository.getSubscriptionsByUserAndVehicle(userId, vehicleId)
    }

    fun getSubscriptionById(subscriptionId: Int): Subscription? {
        return repository.getSubscriptionById(subscriptionId)
    }

    fun updateSubscription(subscriptionId: Int, updatedFields: Subscription): Boolean {
        return repository.updateSubscription(subscriptionId, updatedFields)
    }

    fun deleteSubscription(subscriptionId: Int): Boolean {
        return repository.deleteSubscription(subscriptionId)
    }
}
