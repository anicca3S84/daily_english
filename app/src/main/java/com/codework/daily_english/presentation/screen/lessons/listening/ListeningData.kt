package com.codework.daily_english.presentation.screen.lessons.listening

import com.codework.daily_english.presentation.model.DialogueLine

data class ListeningData(
    val title: String,
    val subtitle: String,
    val imageUrl: String,
    val duration: Long,         // Tổng thời lượng bài nghe
    val currentTime: Long = 0L, // Thời gian hiện tại đang phát
    val isPlaying: Boolean = false,
    val isSlowMode: Boolean = false,
    val dialogues: List<DialogueLine>,
    val progressCurrent: Int = 1,
    val progressTotal: Int = 5
)