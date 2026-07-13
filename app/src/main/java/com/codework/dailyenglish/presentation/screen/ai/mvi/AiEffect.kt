package com.codework.dailyenglish.presentation.screen.ai.mvi

import com.codework.dailyenglish.core.base.BaseEffect

sealed interface AiEffect : BaseEffect{
    data object OpenGallery : AiEffect
}