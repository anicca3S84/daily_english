package com.codework.daily_english.presentation.screen.translate.mvi

import com.codework.daily_english.core.base.BaseEffect

sealed interface TranslateEffect : BaseEffect {
    data object NavigateToHistory : TranslateEffect
}