package com.codework.daily_english.presentation.screen.auth.login

import androidx.lifecycle.viewModelScope
import com.codework.daily_english.core.base.BaseEffect.ShowToast
import com.codework.daily_english.core.base.BaseViewModel
import com.codework.daily_english.core.result.onError
import com.codework.daily_english.core.result.onSuccess
import com.codework.daily_english.domain.auth.usecase.LoginWithGoogleUseCase
import com.codework.daily_english.presentation.screen.auth.login.mvi.LoginEffect
import com.codework.daily_english.presentation.screen.auth.login.mvi.LoginEvent
import com.codework.daily_english.presentation.screen.auth.login.mvi.LoginState
import com.codework.daily_english.presentation.utils.asUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginWithGoogleUseCase: LoginWithGoogleUseCase
) : BaseViewModel<LoginState, LoginEvent, LoginEffect>(
    initialState = LoginState()
) {

    override fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.LoginWithGoogle -> {
                performGoogleLogin(event.idToken)
            }
            is LoginEvent.OnEmailLoginClick -> {
                sendEffect(LoginEffect.NavigateToEmailLogin)
            }
            is LoginEvent.OnAppleLoginClick -> {
                // TODO: Xử lý Apple Login
            }
            is LoginEvent.OnCancelClick -> {
                sendEffect(LoginEffect.NavigateBack)
            }
            is LoginEvent.OnSignUpClick -> {
                sendEffect(LoginEffect.NavigateToSignUp)
            }

            LoginEvent.OnGoogleLoginClick -> TODO()
        }
    }

    private fun performGoogleLogin(idToken: String) {
        _state.update {
            it.copy(
                isLoading = true,
                error = null
            )
        }

        viewModelScope.launch {
            loginWithGoogleUseCase(idToken)
                .onSuccess { user ->
                    _state.update { it.copy(isLoading = false) }

                    sendEffect(LoginEffect.NavigateToHome)
                }
                .onError { appError ->
                    val uiError = appError.asUiText()
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = uiError
                        )
                    }

                    sendEffect(ShowToast(uiError))
                }
        }
    }
}