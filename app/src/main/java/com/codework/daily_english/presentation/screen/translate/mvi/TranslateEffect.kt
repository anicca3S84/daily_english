package com.codework.daily_english.presentation.screen.translate.mvi

sealed interface TranslateEffect {
    data class ShowToast(val message: String) : TranslateEffect
}