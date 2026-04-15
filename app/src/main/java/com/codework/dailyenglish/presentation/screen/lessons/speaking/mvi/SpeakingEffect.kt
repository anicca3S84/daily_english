package com.codework.dailyenglish.presentation.screen.lessons.speaking.mvi

import com.codework.dailyenglish.core.base.BaseEffect

sealed interface SpeakingEffect: BaseEffect {
    data object NavigateBack : SpeakingEffect
    data object NavigateToFinish : SpeakingEffect
    data class PlayTtsAudio(val text: String) : SpeakingEffect // Hiệu ứng đọc văn bản
    data class ShowToast(val message: String) : SpeakingEffect
}