package com.codework.dailyenglish.core.base

import com.codework.dailyenglish.presentation.utils.UiText

data class BaseUiState<out T>(
    val data: T? = null,
    val isLoading: Boolean = false,
    val error: UiText? = null
) {
    val isEmpty: Boolean
        get() = data == null || (data is Collection<*> && data.isEmpty())

    val isRefreshing: Boolean
        get() = isLoading && !isEmpty
}