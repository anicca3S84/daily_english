package com.codework.dailyenglish.data.ai.remote.dto

import com.google.gson.annotations.SerializedName

data class ChatResponseDto(
    @SerializedName("sessionId") val sessionId: String,
    @SerializedName("aiReply") val aiReply: String,
    @SerializedName("timestamp") val timestamp: String,
    @SerializedName("suggestions") val suggestions: List<String>? = null // Nhận mảng chuỗi gợi ý từ BE
)