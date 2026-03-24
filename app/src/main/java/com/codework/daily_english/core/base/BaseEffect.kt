package com.codework.daily_english.core.base

interface BaseEffect {
    data class ShowToast(val message: String) : BaseEffect
    data class ShowSnackbar(val message: String) : BaseEffect
}