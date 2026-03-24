package com.codework.daily_english.presentation.screen.auth.login

import com.codework.daily_english.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() :
    BaseViewModel<LoginState, LoginEvent, LoginEffect>(
        initialState = LoginState()
    ) {

    init {
//        _state.update { BaseState.Success(LoginState()) }
    }

    override fun onEvent(event: LoginEvent) {
//        val currentState = (_uiState.value as? BaseState.Success)?.data ?: return

        when (event) {
            is LoginEvent.OnContinueWithGoogle -> {}
//                simulateSocialLogin(currentState, "Google")
            is LoginEvent.OnContinueWithApple -> {}
//                simulateSocialLogin(currentState, "Apple")
            is LoginEvent.OnContinueWithEmail -> sendEffect(LoginEffect.NavigateToEmailLogin)
            is LoginEvent.OnTermsClick -> { /* Xử lý mở Browser */ }
            is LoginEvent.OnPrivacyClick -> { /* Xử lý mở Browser */ }
            is LoginEvent.OnRetry -> {}
//                _uiState.update { BaseState.Success(LoginState()) }
        }
    }

    private fun simulateSocialLogin(currentState: LoginState, provider: String) {
//        viewModelScope.launch {
//            _uiState.update {
//                BaseState.Success(currentState.copy(isLoading = true, loadingProvider = provider))
//            }
//
//            delay(1500) // Giả lập gọi Firebase Auth hoặc API
//
//            _uiState.update {
//                BaseState.Success(currentState.copy(isLoading = false, loadingProvider = null))
//            }
//
//            // Đăng nhập thành công -> Chuyển sang màn hình chính
//            sendEffect(LoginEffect.NavigateToHome)
//        }
    }
}