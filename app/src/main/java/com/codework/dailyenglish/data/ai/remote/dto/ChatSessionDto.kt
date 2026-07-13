package com.codework.dailyenglish.data.ai.remote.dto

import kotlinx.serialization.SerialName

data class ChatSessionDto(
    @SerialName("id") val id: String,
    @SerialName("title") val title: String? = "New Chat",
    @SerialName("lastMessage") val lastMessage: String? = null,
    @SerialName("date") val date: String? = null
)