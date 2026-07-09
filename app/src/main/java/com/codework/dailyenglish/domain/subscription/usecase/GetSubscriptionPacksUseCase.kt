package com.codework.dailyenglish.domain.subscription.usecase

import com.codework.dailyenglish.core.network.AppResult
import com.codework.dailyenglish.domain.lesson_preview.model.LessonPreview
import com.codework.dailyenglish.domain.subscription.model.Subscription
import com.codework.dailyenglish.domain.subscription.repository.SubscriptionRepository
import javax.inject.Inject

class GetSubscriptionPacksUseCase @Inject constructor(
    private val repository: SubscriptionRepository
) {
    suspend operator fun invoke(): AppResult<List<Subscription>> {
        return repository.getSubscriptionPacks()
    }
}