package com.codework.daily_english.presentation.components

sealed interface UiState<out T> {
    data object Loading : UiState<Nothing>
    data class Success<T>(val data: T) : UiState<T>
    data class Error(
        val message: String,
        val code: Int? = null, // Thêm code để phân loại (404, 500, -1...)
        val throwable: Throwable? = null
    ) : UiState<Nothing>
}