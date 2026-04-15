package com.codework.dailyenglish.presentation.screen.dictionary.mvi

import com.codework.dailyenglish.core.base.BaseEffect

sealed interface DictionaryEffect: BaseEffect {
    data class PlayAudio(val word: String, val accent: String) : DictionaryEffect // Ra lệnh cho UI phát âm thanh
    data class NavigateToFeature(val feature: String) : DictionaryEffect // Điều hướng từ Quick Action
    data class ShowToast(val message: String) : DictionaryEffect
}