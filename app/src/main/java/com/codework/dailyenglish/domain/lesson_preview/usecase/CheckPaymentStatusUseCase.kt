package com.codework.dailyenglish.domain.lesson_preview.usecase

import com.codework.dailyenglish.domain.subscription.repository.SubscriptionRepository
import jakarta.inject.Inject

class CheckPaymentStatusUseCase @Inject constructor(
    private val repository: SubscriptionRepository
) {
    suspend operator fun invoke(transactionRef: String) =
        repository.checkPaymentStatus(transactionRef)
}