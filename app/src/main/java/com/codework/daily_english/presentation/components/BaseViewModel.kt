package com.codework.daily_english.presentation.components
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codework.daily_english.presentation.navigation.Route
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<T> : ViewModel() {

    protected val _uiState = MutableStateFlow<UiState<T>>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    protected val _navigationChannel = Channel<Route>()
    val navigationChannel = _navigationChannel.receiveAsFlow()

    protected fun navigateTo(route: Route) {
        viewModelScope.launch {
            _navigationChannel.send(route)
        }
    }
}