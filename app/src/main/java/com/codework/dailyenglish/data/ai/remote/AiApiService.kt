package com.codework.dailyenglish.data.ai.remote

import com.codework.dailyenglish.data.ai.remote.dto.ChatRequestDto
import com.codework.dailyenglish.data.ai.remote.dto.ChatResponseDto
import com.codework.dailyenglish.data.ai.remote.dto.ChatSessionDto
import com.codework.dailyenglish.data.remote.response.BaseResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AiApiService {
    @GET("chat/sessions")
    suspend fun getSessions(): BaseResponse<List<ChatSessionDto>>

    @POST("chat/message")
    suspend fun sendMessage(@Body request: ChatRequestDto): BaseResponse<ChatResponseDto>
}