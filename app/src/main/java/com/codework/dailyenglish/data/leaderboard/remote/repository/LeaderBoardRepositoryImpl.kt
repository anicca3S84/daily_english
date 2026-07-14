package com.codework.dailyenglish.data.leaderboard.remote.repository
import com.codework.dailyenglish.core.dispatchers.Dispatcher
import com.codework.dailyenglish.core.network.AppResult
import com.codework.dailyenglish.core.network.map
import com.codework.dailyenglish.data.leaderboard.mapper.toDomain
import com.codework.dailyenglish.data.leaderboard.remote.datasource.LeaderBoardRemoteDataSource
import com.codework.dailyenglish.domain.leaderboard.model.LeaderBoardWrapper
import com.codework.dailyenglish.domain.leaderboard.repository.LeaderBoardRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LeaderBoardRepositoryImpl @Inject constructor(
    private val remoteDataSource: LeaderBoardRemoteDataSource,
    private val dispatchers: Dispatcher
) : LeaderBoardRepository {
    override suspend fun getLeaderBoard(type: String, timeFilter: String, page: Int, size: Int): AppResult<LeaderBoardWrapper> =
        withContext(dispatchers.io) {
            remoteDataSource.getLeaderBoard(type, timeFilter, page, size).map { dto ->
                dto.toDomain()
            }
        }
}