package com.codework.dailyenglish.data.ai.remote.dto

import com.google.gson.annotations.SerializedName

data class ChatRequestDto(
    @SerializedName("sessionId") val sessionId: String,
    @SerializedName("message") val message: String,
    @SerializedName("topicId") val topicId: String? = null,
    @SerializedName("topicName") val topicName: String? = null,
    @SerializedName("isNeedSuggestions") val isNeedSuggestions: Boolean // Đồng bộ cờ điều kiện với BE
)