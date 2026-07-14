package com.codework.dailyenglish.data.leaderboard.remote.datasource

import com.codework.dailyenglish.core.network.AppResult
import com.codework.dailyenglish.data.leaderboard.remote.dto.LeaderBoardWrapperDto

interface LeaderBoardRemoteDataSource {
    suspend fun getLeaderBoard(type: String, timeFilter: String, page: Int, size: Int): AppResult<LeaderBoardWrapperDto>
}