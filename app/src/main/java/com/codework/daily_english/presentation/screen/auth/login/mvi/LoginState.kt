package com.codework.daily_english.presentation.screen.auth.login.mvi

import com.codework.daily_english.presentation.utils.UiText

data class LoginState(
    val isLoading: Boolean = false,
    val error: UiText? = null
)