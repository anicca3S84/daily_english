package com.codework.dailyenglish.data.ai.remote

import com.codework.dailyenglish.data.ai.remote.dto.ChatMessageDto
import com.codework.dailyenglish.data.ai.remote.dto.ChatRequestDto
import com.codework.dailyenglish.data.ai.remote.dto.ChatResponseDto
import com.codework.dailyenglish.data.ai.remote.dto.ChatSessionDto
import com.codework.dailyenglish.data.remote.response.BaseResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface AiApiService {
    @GET("chat/sessions")
    suspend fun getSessions(
        @Query("type") type: String
    ): BaseResponse<List<ChatSessionDto>>

    @GET("chat/sessions/agent")
    suspend fun getAgentSessions(
        @Query("type") type: String,
        @Query("topicId") topicId: String?
    ): BaseResponse<List<ChatSessionDto>>

    @POST("chat/message")
    suspend fun sendMessage(@Body request: ChatRequestDto): BaseResponse<ChatResponseDto>

    @GET("chat/sessions/{sessionId}/messages")
    suspend fun getSessionMessages(
        @Path("sessionId") sessionId: String
    ): BaseResponse<List<ChatMessageDto>>

    @POST("chat/agent/message")
    suspend fun sendAgentMessage(@Body request: ChatRequestDto): BaseResponse<ChatResponseDto>
}