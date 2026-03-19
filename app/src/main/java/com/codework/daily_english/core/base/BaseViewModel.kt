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

    // 1. STATE: Dùng protected để các ViewModel con có thể update state
    protected val _state = MutableStateFlow(initialState)
    val state: StateFlow<S> = _state.asStateFlow()

    // 2. EFFECT: Channel xử lý các sự kiện 1 lần (Toast, Navigation)
    private val _effectChannel = Channel<F>()
    val effectFlow = _effectChannel.receiveAsFlow()

    // 3. EVENT: Hàm bắt buộc các ViewModel con phải implement để hứng sự kiện từ UI
    abstract fun onEvent(event: E)

    // Hàm tiện ích để ViewModel con bắn Effect dễ dàng hơn
    protected fun sendEffect(effect: F) {
        viewModelScope.launch {
            _effectChannel.send(effect)
        }
    }
}