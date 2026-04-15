package com.codework.dailyenglish.presentation.screen.ai.model

data class ChatSession(
    val id: String,
    val title: String, // Ví dụ: "Học thì quá khứ", "Sửa lỗi ngữ pháp"...
    val lastMessage: String,
    val date: String
)