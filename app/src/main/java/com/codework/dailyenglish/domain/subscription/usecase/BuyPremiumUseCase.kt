package com.codework.dailyenglish.domain.subscription.usecase

import com.codework.dailyenglish.core.network.AppResult
import com.codework.dailyenglish.domain.subscription.model.PaymentDetails
import com.codework.dailyenglish.domain.subscription.repository.SubscriptionRepository
import javax.inject.Inject


class BuyPremiumUseCase @Inject constructor(
    private val repository: SubscriptionRepository
) {
    suspend operator fun invoke(
        subscriptionPublicId: String,
        paymentMethod: String
    ): AppResult<PaymentDetails> {
        return repository.buyPremium(subscriptionPublicId, paymentMethod)
    }
}