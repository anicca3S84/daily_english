package com.codework.dailyenglish.data.user_statistic.repository


import com.codework.dailyenglish.core.dispatchers.Dispatcher
import com.codework.dailyenglish.core.network.AppResult
import com.codework.dailyenglish.core.network.map
import com.codework.dailyenglish.data.user_statistic.mapper.toDomain
import com.codework.dailyenglish.data.user_statistic.mapper.toSyncTimeDto
import com.codework.dailyenglish.data.user_statistic.remote.datasource.UserStatisticRemoteDataSource
import com.codework.dailyenglish.domain.user_statistic.repository.UserStatisticRepository
import com.codework.dailyenglish.presentation.screen.home.model.DailyStudyTime
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserStatisticRepositoryImpl @Inject constructor(
    private val remoteDataSource: UserStatisticRemoteDataSource,
    private val dispatchers: Dispatcher
) : UserStatisticRepository {

    override suspend fun syncStudyTime(
        seconds: Int
    ): AppResult<Unit> = withContext(dispatchers.io) {
        val request = seconds.toSyncTimeDto()
        val responseResult = remoteDataSource.syncStudyTime(request)
        responseResult.map {
        }
    }

    override suspend fun getWeeklyStats(): AppResult<List<DailyStudyTime>> = withContext(dispatchers.io) {
        remoteDataSource.getWeeklyStats().map { dtos ->
            dtos.map { it.toDomain() }
        }
    }
}