package com.codework.dailyenglish.core.network

inline fun <T> AppResult<T>.onSuccess(action: (T) -> Unit): AppResult<T> {
    if (this is AppResult.Success) action(data)
    return this
}

inline fun <T> AppResult<T>.onError(action: (AppError) -> Unit): AppResult<T> {
    if (this is AppResult.Error) action(error)
    return this
}

inline fun <T, R> AppResult<T>.map(transform: (T) -> R): AppResult<R> {
    return when (this) {
        is AppResult.Success -> AppResult.Success(transform(data))
        is AppResult.Error -> this
    }
}

inline fun <T> AppResult<T>.onErrorDetails(action: (error: AppError, message: String?) -> Unit): AppResult<T> {
    if (this is AppResult.Error) action(error, message)
    return this
}