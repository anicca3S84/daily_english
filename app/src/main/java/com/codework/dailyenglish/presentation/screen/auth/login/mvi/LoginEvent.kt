package com.codework.dailyenglish.presentation.screen.auth.login.mvi

sealed interface LoginEvent {
    object OnCancelClick : LoginEvent
    object OnEmailLoginClick : LoginEvent
    object OnGoogleLoginClick : LoginEvent
    object OnAppleLoginClick : LoginEvent
    object OnSignUpClick : LoginEvent

    data class LoginWithGoogle(val idToken: String) : LoginEvent
}