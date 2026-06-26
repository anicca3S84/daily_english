package com.codework.dailyenglish.domain.user_statistic.usecase

import com.codework.dailyenglish.core.network.AppResult
import com.codework.dailyenglish.domain.user_statistic.repository.UserStatisticRepository
import javax.inject.Inject

class SyncStudyTimeUseCase @Inject constructor(
    private val repository: UserStatisticRepository
) {
    suspend operator fun invoke(minutes: Int): AppResult<Unit> {
        return repository.syncStudyTime(minutes)
    }
}

