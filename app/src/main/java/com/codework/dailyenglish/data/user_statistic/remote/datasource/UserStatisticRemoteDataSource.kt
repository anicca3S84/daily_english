package com.codework.dailyenglish.data.user_statistic.remote.datasource

import com.codework.dailyenglish.core.network.AppResult
import com.codework.dailyenglish.data.user_statistic.remote.dto.SyncTimeRequestDto
import com.codework.dailyenglish.data.user_statistic.remote.dto.WeeklyStudyStatsDto

interface UserStatisticRemoteDataSource {
    suspend fun syncStudyTime(
        request: SyncTimeRequestDto
    ): AppResult<String>

    suspend fun getWeeklyStats(): AppResult<List<WeeklyStudyStatsDto>>
}