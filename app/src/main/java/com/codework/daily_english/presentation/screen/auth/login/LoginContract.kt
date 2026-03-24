package com.codework.daily_english.presentation.screen.auth.login

import com.codework.daily_english.core.base.BaseEffect

// 1. STATE (Đổi LoginData thành LoginState)
data class LoginState(
    val isLoading: Boolean = false,
    val loadingProvider: String? = null
)

// 2. EVENT
sealed interface LoginEvent {
    data object OnContinueWithGoogle : LoginEvent
    data object OnContinueWithApple : LoginEvent
    data object OnContinueWithEmail : LoginEvent
    data object OnTermsClick : LoginEvent
    data object OnPrivacyClick : LoginEvent
    data object OnRetry : LoginEvent
}

// 3. EFFECT
sealed interface LoginEffect: BaseEffect {
    data object NavigateToHome : LoginEffect
    data object NavigateToEmailLogin : LoginEffect
    data class ShowError(val message: String) : LoginEffect
}