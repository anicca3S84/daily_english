package com.codework.dailyenglish.presentation.screen.auth.login

import androidx.lifecycle.viewModelScope
import com.codework.dailyenglish.core.base.BaseEffect.ShowToast
import com.codework.dailyenglish.core.base.BaseViewModel
import com.codework.dailyenglish.core.network.onError
import com.codework.dailyenglish.core.network.onSuccess
import com.codework.dailyenglish.domain.auth.usecase.LoginWithGoogleUseCase
import com.codework.dailyenglish.presentation.screen.auth.login.mvi.LoginEffect
import com.codework.dailyenglish.presentation.screen.auth.login.mvi.LoginEvent
import com.codework.dailyenglish.presentation.screen.auth.login.mvi.LoginState
import com.codework.dailyenglish.presentation.utils.asUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginWithGoogleUseCase: LoginWithGoogleUseCase,
) : BaseViewModel<LoginState, LoginEvent, LoginEffect>(
    initialState = LoginState(data = Unit)
) {

    override fun onEvent(event: LoginEvent) {
        when (event) {

            is LoginEvent.LoginWithGoogle -> {
                performGoogleLogin(event.idToken)
            }

            LoginEvent.OnGoogleLoginClick -> {
            }

            is LoginEvent.OnEmailLoginClick -> {
                sendEffect(LoginEffect.NavigateToEmailLogin)
            }

            is LoginEvent.OnAppleLoginClick -> {
            }

            is LoginEvent.OnCancelClick -> {
                sendEffect(LoginEffect.NavigateBack)
            }

            is LoginEvent.OnSignUpClick -> {
                sendEffect(LoginEffect.NavigateToSignUp)
            }
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
                .onSuccess {
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

                    sendGlobalEffect(ShowToast(uiError))
                }
        }
    }
}