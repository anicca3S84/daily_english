package com.codework.daily_english.presentation.components

data class UiState<T>(
    val isLoading: Boolean = false,
    val error: String? = null,
    val data: T? = null
){
    val isSuccess: Boolean get() = data != null && error == null && !isLoading
}
