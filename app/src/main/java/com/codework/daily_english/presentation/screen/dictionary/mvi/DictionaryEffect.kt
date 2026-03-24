package com.codework.daily_english.presentation.screen.dictionary.mvi

import com.codework.daily_english.core.base.BaseEffect

sealed interface DictionaryEffect: BaseEffect {
    data class PlayAudio(val word: String, val accent: String) : DictionaryEffect // Ra lệnh cho UI phát âm thanh
    data class NavigateToFeature(val feature: String) : DictionaryEffect // Điều hướng từ Quick Action
    data class ShowToast(val message: String) : DictionaryEffect
}