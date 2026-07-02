package com.codework.dailyenglish.presentation

import com.codework.dailyenglish.core.base.BaseUiState
import com.codework.dailyenglish.core.navigation.Route

data class MainState(
    val isLoading: Boolean = true,
    val startDestination: Any = Route.LoginScreen
)