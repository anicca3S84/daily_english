package com.codework.daily_english.presentation.screen.login

import androidx.lifecycle.viewModelScope
import com.codework.daily_english.presentation.components.BaseViewModel
import com.codework.daily_english.presentation.components.UiState
import com.codework.daily_english.presentation.screen.auth.login.LoginEffect
import com.codework.daily_english.presentation.screen.auth.login.LoginEvent
import com.codework.daily_english.presentation.screen.auth.login.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() :
    BaseViewModel<LoginState, LoginEvent, LoginEffect>() {

    init {
        _uiState.update { UiState.Success(LoginState()) }
    }

    override fun onEvent(event: LoginEvent) {
        val currentState = (_uiState.value as? UiState.Success)?.data ?: return

        when (event) {
            is LoginEvent.OnContinueWithGoogle -> simulateSocialLogin(currentState, "Google")
            is LoginEvent.OnContinueWithApple -> simulateSocialLogin(currentState, "Apple")
            is LoginEvent.OnContinueWithEmail -> sendEffect(LoginEffect.NavigateToEmailLogin)
            is LoginEvent.OnTermsClick -> { /* Xử lý mở Browser */ }
            is LoginEvent.OnPrivacyClick -> { /* Xử lý mở Browser */ }
            is LoginEvent.OnRetry -> _uiState.update { UiState.Success(LoginState()) }
        }
    }

    private fun simulateSocialLogin(currentState: LoginState, provider: String) {
        viewModelScope.launch {
            _uiState.update {
                UiState.Success(currentState.copy(isLoading = true, loadingProvider = provider))
            }

            delay(1500) // Giả lập gọi Firebase Auth hoặc API

            _uiState.update {
                UiState.Success(currentState.copy(isLoading = false, loadingProvider = null))
            }

            // Đăng nhập thành công -> Chuyển sang màn hình chính
            sendEffect(LoginEffect.NavigateToHome)
        }
    }
}