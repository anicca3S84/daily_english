package com.codework.dailyenglish.data.ai.remote.dto

import com.google.gson.annotations.SerializedName

data class ChatMessageDto(
    @SerializedName("senderType") val senderType: String,
    @SerializedName("content") val content: String,
    @SerializedName("createdAt") val createdAt: String
)