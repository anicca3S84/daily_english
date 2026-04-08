package com.codework.daily_english.core.network

import com.codework.daily_english.data.remote.response.BaseResponse
import com.google.gson.JsonSyntaxException
import retrofit2.HttpException
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

fun <T> AppResult<BaseResponse<T>>.unwrap(): AppResult<T> {
    return when (this) {
        is AppResult.Success -> {
            val body = this.data
            // Nếu API HTTP 200 OK và Logic Backend cũng báo thành công
            if (body.isSuccess && body.data != null) {
                AppResult.Success(body.data)
            } else {
                // Nếu HTTP 200 OK, Backend error
                AppResult.Error(
                    error = AppError.Validation(body.message ?: "Lỗi xác thực từ Server"),
                    message = body.message ?: "Lỗi dữ liệu Server"
                )
            }
        }
        // other error
        is AppResult.Error -> this
    }
}