package com.codework.daily_english.presentation.screen.auth.login.mvi

sealed interface LoginEvent {
    object OnCancelClick : LoginEvent
    object OnEmailLoginClick : LoginEvent
    object OnGoogleLoginClick : LoginEvent
    object OnAppleLoginClick : LoginEvent
    object OnSignUpClick : LoginEvent

    //already exists token
    data class LoginWithGoogle(val idToken: String) : LoginEvent
}