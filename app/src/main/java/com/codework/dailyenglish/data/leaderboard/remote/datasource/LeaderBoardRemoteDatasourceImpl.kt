package com.codework.dailyenglish.data.leaderboard.remote.datasource

import com.codework.dailyenglish.core.network.AppResult
import com.codework.dailyenglish.core.network.safeApiCall
import com.codework.dailyenglish.core.network.unwrap
import com.codework.dailyenglish.data.leaderboard.remote.LeaderBoardApi
import com.codework.dailyenglish.data.leaderboard.remote.dto.LeaderBoardWrapperDto
import javax.inject.Inject

class LeaderBoardRemoteDataSourceImpl @Inject constructor(
    private val api: LeaderBoardApi
) : LeaderBoardRemoteDataSource {
    override suspend fun getLeaderBoard(type: String, timeFilter: String, page: Int, size: Int): AppResult<LeaderBoardWrapperDto> {
        return safeApiCall { api.getLeaderBoard(type, timeFilter, page, size) }.unwrap()
    }
}