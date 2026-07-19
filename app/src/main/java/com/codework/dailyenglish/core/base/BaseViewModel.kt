package com.codework.dailyenglish.core.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codework.dailyenglish.presentation.utils.UiText
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

    private val _effectChannel = Channel<F>()
    val effectFlow = _effectChannel.receiveAsFlow()

    private val _globalEffectChannel = Channel<BaseEffect>()
    val globalEffectFlow = _globalEffectChannel.receiveAsFlow()


    abstract fun onEvent(event: E)

    protected fun sendEffect(effect: F) {
        viewModelScope.launch {
            _effectChannel.send(effect)
        }
    }

    protected fun sendGlobalEffect(effect: BaseEffect) {
        // Kiểm tra nếu hiệu ứng gửi đi là hiển thị Toast
        if (effect is BaseEffect.ShowToast) {

            // Lấy chuỗi text thông báo từ UiText (hoặc String tùy thuộc cấu trúc dự án của bạn)
            // Thông thường UiText sẽ có dạng DynamicString chứa chuỗi thô từ API trả về.
            val messageStr = when (val msg = effect.message) {
                is UiText.DynamicString -> msg.value
                // Trường hợp nếu dự án của bạn định nghĩa message là String thường, hãy dùng: msg
                else -> ""
            }

            // Nếu thông báo chứa các từ khóa liên quan đến hết hạn Token, ta chặn không cho gửi đi
            if (messageStr.contains("token", ignoreCase = true) ||
                messageStr.contains("expired", ignoreCase = true) ||
                messageStr.contains("unauthorized", ignoreCase = true) ||
                messageStr.contains("JWT", ignoreCase = true)
            ) {
                return // Thoát hàm ngay lập tức, chặn đứng không gửi Toast
            }
        }

        // Nếu không phải lỗi token thì cho phép gửi bình thường
        viewModelScope.launch {
            _globalEffectChannel.send(effect)
        }
    }
}