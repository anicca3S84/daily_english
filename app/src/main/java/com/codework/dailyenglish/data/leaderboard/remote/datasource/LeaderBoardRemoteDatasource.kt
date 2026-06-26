package com.codework.dailyenglish.data.leaderboard.remote.datasource

import com.codework.dailyenglish.core.network.AppResult
import com.codework.dailyenglish.data.leaderboard.remote.dto.LeaderBoardWrapperDto

interface LeaderBoardRemoteDataSource {
    suspend fun getLeaderBoard(page: Int, size: Int): AppResult<LeaderBoardWrapperDto>
}