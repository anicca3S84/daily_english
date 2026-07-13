package com.codework.dailyenglish.data.ai.remote.dto

import com.google.gson.annotations.SerializedName

data class ChatResponseDto(
    @SerializedName("sessionId") val sessionId: String,
    @SerializedName("aiReply") val aiReply: String,
    @SerializedName("timestamp") val timestamp: String,
    @SerializedName("suggestions") val suggestions: List<String>? = null,

    @SerializedName("isPremium") val isPremium: Boolean? = null,
    @SerializedName("remainingMessages") val remainingMessages: Int? = null,
    @SerializedName("activeCharLimit") val activeCharLimit: Int? = null
)