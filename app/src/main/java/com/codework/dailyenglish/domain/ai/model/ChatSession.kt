package com.codework.dailyenglish.domain.ai.model

data class ChatSession(
    val id: String,
    val title: String,
    val lastMessage: String? = "",
    val date: String? = ""
)