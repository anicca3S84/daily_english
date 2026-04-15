package com.codework.dailyenglish.core.network


sealed interface AppError {
    data object Network : AppError
    data object Timeout : AppError

    data class Http(
        val code: Int,
        val message: String? = null
    ) : AppError

    data class Parsing(
        val throwable: Throwable? = null
    ) : AppError

    // 🔥 MỚI: Lỗi chuẩn hóa dữ liệu đầu vào (Không chứa text UI)
    data class Validation(
        val reason: String
    ) : AppError

    data class Unknown(
        val throwable: Throwable? = null
    ) : AppError
}


