package com.codework.dailyenglish.core.base

import com.codework.dailyenglish.presentation.utils.UiText

interface BaseEffect {
    data class ShowToast(val message: UiText) : BaseEffect
    data class ShowSnackbar(val message: UiText) : BaseEffect

}