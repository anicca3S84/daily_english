package com.codework.daily_english.presentation.screen.auth.login.mvi

import com.codework.daily_english.core.base.BaseEffect

sealed interface LoginEffect: BaseEffect {
    object NavigateBack : LoginEffect
    object NavigateToSignUp : LoginEffect
    object NavigateToEmailLogin : LoginEffect
    object NavigateToHome : LoginEffect
}