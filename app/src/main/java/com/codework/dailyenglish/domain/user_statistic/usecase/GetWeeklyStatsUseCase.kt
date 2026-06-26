package com.codework.dailyenglish.domain.user_statistic.usecase

import com.codework.dailyenglish.core.network.AppResult
import com.codework.dailyenglish.domain.user_statistic.repository.UserStatisticRepository
import com.codework.dailyenglish.presentation.screen.home.model.DailyStudyTime
import javax.inject.Inject

class GetWeeklyStatsUseCase @Inject constructor(
    private val repository: UserStatisticRepository
) {
    suspend operator fun invoke(): AppResult<List<DailyStudyTime>> {
        return repository.getWeeklyStats()
    }
}