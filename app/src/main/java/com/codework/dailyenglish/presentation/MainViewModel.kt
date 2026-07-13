package com.codework.dailyenglish.presentation

import androidx.lifecycle.viewModelScope
import com.codework.dailyenglish.core.base.BaseViewModel
import com.codework.dailyenglish.core.dispatchers.Dispatcher
import com.codework.dailyenglish.core.navigation.Route
import com.codework.dailyenglish.domain.auth.usecase.CheckSessionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import com.codework.dailyenglish.core.session.SessionEvent
import com.codework.dailyenglish.core.session.SessionEventBus
import kotlinx.coroutines.flow.SharedFlow


@HiltViewModel
class MainViewModel @Inject constructor(
    private val checkSessionUseCase: CheckSessionUseCase,
    private val dispatchers: Dispatcher,
    private val sessionEventBus: SessionEventBus 
) : BaseViewModel<MainState, Unit, Unit>(MainState()) {
    val sessionEvents: SharedFlow<SessionEvent> = sessionEventBus.events

    init {
        checkSession()
    }

    private fun checkSession() {
        viewModelScope.launch {
            val isLoggedIn = withContext(dispatchers.io) {
                checkSessionUseCase()
            }

            _state.update { currentState ->
                currentState.copy(
                    isLoading = false,
                    startDestination = if (isLoggedIn) Route.HomeScreen else Route.LoginScreen
                )
            }
        }
    }

    override fun onEvent(event: Unit) {}
}