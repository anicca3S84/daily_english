package com.codework.dailyenglish.data.user_statistic.remote.datasource

import com.codework.dailyenglish.core.network.AppResult
import com.codework.dailyenglish.core.network.safeApiCall
import com.codework.dailyenglish.core.network.unwrap
import com.codework.dailyenglish.data.user_statistic.remote.StatisticApi
import com.codework.dailyenglish.data.user_statistic.remote.dto.SyncTimeRequestDto
import com.codework.dailyenglish.data.user_statistic.remote.dto.WeeklyStudyStatsDto
import javax.inject.Inject

class UserStatisticRemoteDataSourceImpl @Inject constructor(
    private val api: StatisticApi
) : UserStatisticRemoteDataSource {

    override suspend fun syncStudyTime(request: SyncTimeRequestDto): AppResult<String> {
        return safeApiCall { api.syncStudyTime(request) }.unwrap()
    }

    override suspend fun getWeeklyStats(): AppResult<List<WeeklyStudyStatsDto>> {
        return safeApiCall { api.getWeeklyStats() }.unwrap()
    }
}