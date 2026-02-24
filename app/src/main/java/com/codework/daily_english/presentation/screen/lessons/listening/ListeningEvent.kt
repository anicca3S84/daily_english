package com.codework.daily_english.presentation.screen.lessons.listening

sealed class ListeningEvent {
    object TogglePlayPause : ListeningEvent()
    object ToggleSpeed : ListeningEvent()
    data class OnSeek(val position: Float) : ListeningEvent() // Khi kéo thanh tiến trình audio
    object OnLetsPracticeClick : ListeningEvent()
}