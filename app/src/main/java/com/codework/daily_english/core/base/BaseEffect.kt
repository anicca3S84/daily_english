package com.codework.daily_english.core.base

import com.codework.daily_english.presentation.utils.UiText

interface BaseEffect {
    data class ShowToast(val message: UiText) : BaseEffect
    data class ShowSnackbar(val message: UiText) : BaseEffect

}