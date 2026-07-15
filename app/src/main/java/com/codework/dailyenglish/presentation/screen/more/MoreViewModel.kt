package com.codework.dailyenglish.presentation.screen.more

import androidx.lifecycle.viewModelScope
import com.codework.dailyenglish.core.base.BaseViewModel
import com.codework.dailyenglish.domain.auth.usecase.LogoutUseCase
import com.codework.dailyenglish.domain.user.usecase.GetUserInfoUseCase
import com.codework.dailyenglish.presentation.screen.more.mvi.MoreEffect
import com.codework.dailyenglish.presentation.screen.more.mvi.MoreEvent
import com.codework.dailyenglish.presentation.screen.more.mvi.MoreState
import com.codework.dailyenglish.presentation.screen.practice.mvi.PracticeEffect
import com.codework.dailyenglish.presentation.screen.practice.mvi.PracticeEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.codework.dailyenglish.domain.user_statistic.usecase.StudyTimeManagerUseCase


@HiltViewModel
class MoreViewModel @Inject constructor(
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val studyTimeManager: StudyTimeManagerUseCase,
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
            is MoreEvent.OnClickLeaderboard -> sendEffect(MoreEffect.NavigateToLeaderBoard)
            is MoreEvent.OnRefreshData -> loadProfile()
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
            try {
                studyTimeManager.syncIfEnoughTime()
            } catch (e: Exception) {
            }
            logoutUseCase()
            sendEffect(MoreEffect.HandleLogout)
        }
    }
}