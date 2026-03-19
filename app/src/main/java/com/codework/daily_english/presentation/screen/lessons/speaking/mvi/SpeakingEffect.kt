package com.codework.daily_english.presentation.screen.lessons.speaking.mvi

sealed interface SpeakingEffect {
    data object NavigateBack : SpeakingEffect
    data object NavigateToFinish : SpeakingEffect
    data class PlayTtsAudio(val text: String) : SpeakingEffect // Hiệu ứng đọc văn bản
    data class ShowToast(val message: String) : SpeakingEffect
}