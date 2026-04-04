package com.codework.daily_english.data.translate.remote
import com.codework.daily_english.data.remote.response.BaseResponse
import com.codework.daily_english.data.translate.remote.dto.TranslateDto
import retrofit2.http.GET
import retrofit2.http.Query

interface TranslateApi {
    @GET("translate")
    suspend fun translate(
        @Query("q") text: String,
        @Query("source") source: String,
        @Query("target") target: String
    ): BaseResponse<TranslateDto> // Bọc BaseResponse ở đây
}
