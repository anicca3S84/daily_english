package com.codework.daily_english.core.network

import com.codework.daily_english.core.result.AppError
import com.codework.daily_english.core.result.AppResult
import com.google.gson.JsonSyntaxException
import retrofit2.HttpException // ✅ đúng lib
import java.io.IOException
import java.net.SocketTimeoutException

suspend inline fun <T> safeApiCall(
    crossinline apiCall: suspend () -> T
): AppResult<T> {
    return try {
        AppResult.Success(apiCall())
    } catch (e: Exception) {
        e.toAppError()
    }
}

/**
 * 🔥 Map tất cả exception -> AppError (clean + reusable)
 */
fun Exception.toAppError(): AppResult.Error {
    val error = when (this) {
        is IOException -> AppError.Network

        is SocketTimeoutException -> AppError.Timeout

        is HttpException -> AppError.Http(
            code = code(),
            message = message()
        )

        is JsonSyntaxException -> AppError.Parsing(this)

        else -> AppError.Unknown(this)
    }

    return AppResult.Error(error, message = this.message)
}