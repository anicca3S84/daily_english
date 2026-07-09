package com.codework.dailyenglish.domain.subscription.repository

import com.codework.dailyenglish.core.network.AppResult
import com.codework.dailyenglish.domain.subscription.model.PaymentDetails
import com.codework.dailyenglish.domain.subscription.model.Subscription
import kotlinx.coroutines.flow.Flow

interface SubscriptionRepository {
    suspend fun getSubscriptionPacks(): AppResult<List<Subscription>>

    suspend fun buyPremium(
        subscriptionPublicId: String,
        paymentMethod: String
    ): AppResult<PaymentDetails>

    suspend fun checkPaymentStatus(transactionRef: String): AppResult<String>

}