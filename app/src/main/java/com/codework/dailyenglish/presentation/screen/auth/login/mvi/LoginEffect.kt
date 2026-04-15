package com.codework.dailyenglish.presentation.screen.auth.login.mvi

import com.codework.dailyenglish.core.base.BaseEffect

sealed interface LoginEffect: BaseEffect {
    object NavigateBack : LoginEffect
    object NavigateToSignUp : LoginEffect
    object NavigateToEmailLogin : LoginEffect
    object NavigateToHome : LoginEffect
}