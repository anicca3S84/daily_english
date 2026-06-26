package com.codework.dailyenglish.data.ai.remote.dto

data class ChatSessionDto(
    val id: Long,
    val title: String,
    val lastMessage: String?, // Dấu ? cho phép null từ BE
    val date: String?         // Dấu ? cho phép null từ BE
)