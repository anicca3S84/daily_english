package com.codework.daily_english.data.translate.remote

import com.codework.daily_english.data.translate.remote.dto.TranslateResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface TranslateApi {
    @GET("translate") // Thay bằng endpoint thực tế của bạn
    suspend fun translate(
        @Query("q") text: String,
        @Query("source") source: String,
        @Query("target") target: String
    ): TranslateResponseDto
}