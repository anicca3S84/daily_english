package com.codework.daily_english.presentation.model

data class DialogueLine(
    val speakerName: String,
    val speakerEmoji: String,
    val text: String,
    val startTime: Long, // Thời gian bắt đầu (millisecond)
    val endTime: Long    // Thời gian kết thúc (millisecond)
)