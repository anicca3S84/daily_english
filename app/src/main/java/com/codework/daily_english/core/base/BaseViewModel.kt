package com.codework.daily_english.core.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 * @param S: State (Trạng thái UI - Thường là Data Class)
 * @param E: Event (Hành động từ UI gửi lên - Thường là Sealed Interface)
 * @param F: Effect (Hiệu ứng bắn từ ViewModel xuống UI dùng 1 lần - Thường là Sealed Interface)
 */
abstract class BaseViewModel<S, E, F>(
    initialState: S
) : ViewModel() {

    protected val _state = MutableStateFlow(initialState)
    val state: StateFlow<S> = _state.asStateFlow()

    private val _effectChannel = Channel<BaseEffect>()
    val effectFlow = _effectChannel.receiveAsFlow()

    abstract fun onEvent(event: E)

    protected fun sendEffect(effect: BaseEffect) {
        viewModelScope.launch {
            _effectChannel.send(effect)
        }
    }
}