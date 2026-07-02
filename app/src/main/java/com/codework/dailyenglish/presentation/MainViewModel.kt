package com.codework.dailyenglish.presentation.main

import androidx.lifecycle.viewModelScope
import com.codework.dailyenglish.core.base.BaseViewModel
import com.codework.dailyenglish.core.dispatchers.Dispatcher
import com.codework.dailyenglish.core.navigation.Route
import com.codework.dailyenglish.domain.auth.usecase.CheckSessionUseCase
import com.codework.dailyenglish.presentation.MainState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update // BẮT BUỘC IMPORT DÒNG NÀY
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val checkSessionUseCase: CheckSessionUseCase,
    private val dispatchers: Dispatcher
) : BaseViewModel<MainState, Unit, Unit>(MainState()) {

    init {
        checkSession()
    }

    private fun checkSession() {
        viewModelScope.launch {
            val isLoggedIn = withContext(dispatchers.io) {
                checkSessionUseCase()
            }

            // Dùng trực tiếp _state.update, không cần hàm ở Base
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