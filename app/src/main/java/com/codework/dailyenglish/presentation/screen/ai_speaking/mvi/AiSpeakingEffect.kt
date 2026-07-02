package com.codework.dailyenglish.presentation.screen.ai_speaking.mvi

import com.codework.dailyenglish.core.base.BaseEffect

sealed interface AiSpeakingEffect : BaseEffect {
    object StartSpeechRecognition : AiSpeakingEffect
}