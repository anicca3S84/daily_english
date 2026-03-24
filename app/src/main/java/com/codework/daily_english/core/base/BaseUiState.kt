package com.codework.daily_english.core.base

import com.codework.daily_english.presentation.utils.UiText

data class BaseUiState<out T>(
    val data: T? = null,
    val isLoading: Boolean = false,
    val error: UiText? = null
) {
    // Tự động suy luận trạng thái trống dựa trên kiểu dữ liệu
    val isEmpty: Boolean
        get() = data == null || (data is Collection<*> && data.isEmpty())

    // Trạng thái refresh (đang có data nhưng vẫn đang load mới)
    val isRefreshing: Boolean
        get() = isLoading && !isEmpty
}