package com.codework.dailyenglish.core.network

sealed interface AppResult<out T> {

    data class Success<T>(
        val data: T
    ) : AppResult<T>

    data class Error(
        val error: AppError,
        val message: String? = null
    ) : AppResult<Nothing>
}