package com.codework.daily_english.presentation.components

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

// Truyền kiểu State vào, BaseViewModel sẽ tự bọc nó bằng UiState
abstract class BaseViewModel<State, Event, Effect> : ViewModel() {

    // Trạng thái mặc định luôn là Loading
    protected val _uiState = MutableStateFlow<UiState<State>>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _effectChannel = Channel<Effect>()
    val effectFlow = _effectChannel.receiveAsFlow()

    abstract fun onEvent(event: Event)

    protected fun sendEffect(effect: Effect) {
        viewModelScope.launch {
            _effectChannel.send(effect)
        }
    }
}