package com.codework.dailyenglish.data.leaderboard.remote

import com.codework.dailyenglish.data.leaderboard.remote.dto.LeaderBoardWrapperDto
import com.codework.dailyenglish.data.remote.response.BaseResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface LeaderBoardApi {
    @GET("leaderboard")
    suspend fun getLeaderBoard(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): BaseResponse<LeaderBoardWrapperDto>
}