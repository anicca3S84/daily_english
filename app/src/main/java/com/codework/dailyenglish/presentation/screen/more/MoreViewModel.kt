package com.codework.dailyenglish.presentation.screen.more

import androidx.lifecycle.viewModelScope
import com.codework.dailyenglish.core.base.BaseViewModel
import com.codework.dailyenglish.domain.auth.usecase.LogoutUseCase
import com.codework.dailyenglish.domain.user.usecase.GetUserInfoUseCase
import com.codework.dailyenglish.presentation.screen.more.mvi.MoreEffect
import com.codework.dailyenglish.presentation.screen.more.mvi.MoreEvent
import com.codework.dailyenglish.presentation.screen.more.mvi.MoreState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoreViewModel @Inject constructor(
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val logoutUseCase: LogoutUseCase
) : BaseViewModel<MoreState, MoreEvent, MoreEffect>(
    initialState = MoreState()
) {

    init {
        loadProfile()
    }

    override fun onEvent(event: MoreEvent) {
        when(event) {
            is MoreEvent.OnEditProfile -> sendEffect(MoreEffect.NavigateToEditProfile)
            is MoreEvent.OnMenuClick -> sendEffect(MoreEffect.NavigateToMenu(event.label))
            is MoreEvent.OnRetry -> loadProfile()
            is MoreEvent.OnLogout -> handleLogout()
        }
    }

    private fun loadProfile() {
        viewModelScope.launch {
            val user = getUserInfoUseCase()

            _state.update { currentState ->
                currentState.copy(
                    userName = user?.fullName ?: "Guest",
                    email = user?.email ?: "",
                    avatarUrl = user?.avatarUrl ?: "",
                )
            }
        }
    }

    private fun handleLogout() {
        viewModelScope.launch {
            logoutUseCase()
            sendEffect(MoreEffect.HandleLogout)
        }
    }
}