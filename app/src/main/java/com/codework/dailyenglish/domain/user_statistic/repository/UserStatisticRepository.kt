package com.codework.dailyenglish.domain.user_statistic.repository

import com.codework.dailyenglish.core.network.AppResult
import com.codework.dailyenglish.presentation.screen.home.model.DailyStudyTime

interface UserStatisticRepository {
    suspend fun syncStudyTime(seconds: Int): AppResult<Unit>
    suspend fun getWeeklyStats(): AppResult<List<DailyStudyTime>>
}