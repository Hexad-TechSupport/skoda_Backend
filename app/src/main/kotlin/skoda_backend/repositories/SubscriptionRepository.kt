package skoda_backend.repositories

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import skoda_backend.models.Subscription
import skoda_backend.models.Subscriptions

class SubscriptionRepository {

    fun createSubscription(subscription: Subscription): Subscription? {
        return transaction {
            val insertedId = Subscriptions.insertAndGetId {
                it[userId] = subscription.userId
                it[vehicleId] = subscription.vehicleId
                it[serviceType] = subscription.serviceType
                it[startDate] = subscription.startDate
                it[endDate] = subscription.endDate
                it[status] = subscription.status
                it[paymentStatus] = subscription.paymentStatus
                it[renewalDate] = subscription.renewalDate
                it[createdAt] = System.currentTimeMillis()
                it[updatedAt] = System.currentTimeMillis()
            }

            Subscription(
                    subscriptionId = insertedId.value,
                    userId = subscription.userId,
                    vehicleId = subscription.vehicleId,
                    serviceType = subscription.serviceType,
                    startDate = subscription.startDate,
                    endDate = subscription.endDate,
                    status = subscription.status,
                    paymentStatus = subscription.paymentStatus,
                    renewalDate = subscription.renewalDate,
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis()
            )
        }
    }

    fun getSubscriptionsByUserAndVehicle(userId: String, vehicleId: String): List<Subscription> {
        return transaction {
            Subscriptions.selectAll().where {
                (Subscriptions.userId eq userId) and (Subscriptions.vehicleId eq vehicleId)
            }.map {
                Subscription(
                        subscriptionId = it[Subscriptions.subscriptionId],
                        userId = it[Subscriptions.userId],
                        vehicleId = it[Subscriptions.vehicleId],
                        serviceType = it[Subscriptions.serviceType],
                        startDate = it[Subscriptions.startDate],
                        endDate = it[Subscriptions.endDate],
                        status = it[Subscriptions.status],
                        paymentStatus = it[Subscriptions.paymentStatus],
                        renewalDate = it[Subscriptions.renewalDate],
                        createdAt = it[Subscriptions.createdAt],
                        updatedAt = it[Subscriptions.updatedAt] ?: 0
                )
            }
        }
    }

    fun getSubscriptionById(subscriptionId: Int): Subscription? {
        return transaction {
            Subscriptions.selectAll().where { Subscriptions.subscriptionId eq subscriptionId }
                    .mapNotNull {
                        Subscription(
                                subscriptionId = it[Subscriptions.subscriptionId],
                                userId = it[Subscriptions.userId],
                                vehicleId = it[Subscriptions.vehicleId],
                                serviceType = it[Subscriptions.serviceType],
                                startDate = it[Subscriptions.startDate],
                                endDate = it[Subscriptions.endDate],
                                status = it[Subscriptions.status],
                                paymentStatus = it[Subscriptions.paymentStatus],
                                renewalDate = it[Subscriptions.renewalDate],
                                createdAt = it[Subscriptions.createdAt],
                                updatedAt = it[Subscriptions.updatedAt] ?: 0
                        )
                    }.singleOrNull()
        }
    }

    fun updateSubscription(subscriptionId: Int, updatedFields: Subscription): Boolean {
        return transaction {
            Subscriptions.update({ Subscriptions.subscriptionId eq subscriptionId }) {
                it[serviceType] = updatedFields.serviceType
                it[endDate] = updatedFields.endDate
                it[paymentStatus] = updatedFields.paymentStatus
                it[status] = updatedFields.status
                it[renewalDate] = updatedFields.renewalDate
                it[updatedAt] = System.currentTimeMillis()
            } > 0
        }
    }

    fun deleteSubscription(subscriptionId: Int): Boolean {
        return transaction {
            val rowDeleted =Subscriptions.deleteWhere { Subscriptions.subscriptionId eq subscriptionId }
            rowDeleted > 0
        }
    }
}
