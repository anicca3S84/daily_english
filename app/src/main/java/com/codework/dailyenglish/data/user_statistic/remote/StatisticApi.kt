package com.codework.dailyenglish.data.user_statistic.remote

import com.codework.dailyenglish.data.remote.response.BaseResponse
import com.codework.dailyenglish.data.user_statistic.remote.dto.SyncTimeRequestDto
import com.codework.dailyenglish.data.user_statistic.remote.dto.WeeklyStudyStatsDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


interface StatisticApi {
    @POST("statistics/sync-time")
    suspend fun syncStudyTime(
        @Body request: SyncTimeRequestDto
    ): BaseResponse<String>

    @GET("statistics/weekly")
    suspend fun getWeeklyStats(): BaseResponse<List<WeeklyStudyStatsDto>>
}