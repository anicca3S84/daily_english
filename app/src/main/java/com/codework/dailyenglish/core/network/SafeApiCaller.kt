package com.codework.dailyenglish.core.network

import com.codework.dailyenglish.data.lesson.remote.dto.LessonPlayResponseDto
import com.codework.dailyenglish.data.remote.response.BaseResponse
import com.google.gson.Gson // Thêm import này để parse JSON từ errorBody
import com.google.gson.JsonObject
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
    var serverMessage: String? = null

    val error = when (this) {
        is IOException -> AppError.Network
        is SocketTimeoutException -> AppError.Timeout

        is HttpException -> {
            val errorBodyString = response()?.errorBody()?.string()
            serverMessage = try {
                val jsonObject = Gson().fromJson(errorBodyString, JsonObject::class.java)
                jsonObject.get("message")?.asString
            } catch (e: Exception) {
                null
            }
            AppError.Http(
                code = code(),
                message = serverMessage ?: message()
            )
        }

        is JsonSyntaxException -> AppError.Parsing(this)
        else -> AppError.Unknown(this)
    }

    return AppResult.Error(
        error = error,
        message = serverMessage ?: this.message ?: ""
    )
}

fun <T> AppResult<BaseResponse<T>>.unwrap(): AppResult<T> {
    return when (this) {
        is AppResult.Success -> {
            val body = this.data
            if (body.isSuccess && body.data != null) {
                AppResult.Success(body.data)
            } else {
                AppResult.Error(
                    error = AppError.Validation(body.message ?: "Authentication error from the Server"),
                    message = body.message ?: "Server data error"
                )
            }
        }
        is AppResult.Error -> this
    }
}