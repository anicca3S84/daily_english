package com.codework.daily_english.core.base

sealed interface BaseState<out T> {
    data object Loading : BaseState<Nothing>
    data class Success<T>(val data: T) : BaseState<T>
    data class Error(
        val message: String,
        val code: Int? = null, // Thêm code để phân loại (404, 500, -1...)
        val throwable: Throwable? = null
    ) : BaseState<Nothing>
}