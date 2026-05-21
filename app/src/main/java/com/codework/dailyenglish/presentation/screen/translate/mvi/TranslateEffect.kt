package com.codework.dailyenglish.presentation.screen.translate.mvi

import com.codework.dailyenglish.core.base.BaseEffect

sealed interface TranslateEffect : BaseEffect {
    data object NavigateToHistory : TranslateEffect
    data object NavigateToCameraScanner : TranslateEffect // Điều hướng sang màn quét camera
    data object NavigateToGalleryPicker : TranslateEffect // Điều hướng sang thư viện ảnh
}