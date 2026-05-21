package com.codework.dailyenglish.data.translate.remote
import com.codework.dailyenglish.data.remote.response.BaseResponse
import com.codework.dailyenglish.data.translate.remote.dto.TranslateDto
import com.codework.dailyenglish.data.translate.remote.dto.TranslateRequestDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface TranslateApi {
    @POST("translate")
    suspend fun translate(
        @Body request: TranslateRequestDto
    ): BaseResponse<TranslateDto>
}