package com.codework.dailyenglish.presentation.screen.translate.mvi

import com.codework.dailyenglish.core.base.BaseEffect

sealed interface TranslateEffect : BaseEffect {
    data object NavigateToHistory : TranslateEffect
}