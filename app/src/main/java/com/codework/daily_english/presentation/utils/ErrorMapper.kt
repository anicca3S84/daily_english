package com.codework.daily_english.presentation.utils
import com.codework.daily_english.core.result.AppError

fun AppError.asUiText(): String {
    return when (this) {
        is AppError.Network -> "Không có kết nối mạng. Vui lòng kiểm tra Wifi/4G."
        is AppError.Timeout -> "Máy chủ phản hồi quá lâu. Vui lòng thử lại."
        is AppError.Http -> "Lỗi kết nối máy chủ (Mã: ${this.code})."
        is AppError.Parsing -> "Lỗi đọc dữ liệu từ hệ thống."
        is AppError.Unknown -> "Đã xảy ra lỗi không xác định."

        // 🔥 MỚI: Dịch mã lỗi Validation sang tiếng Việt
        is AppError.Validation -> {
            when (this.reason) {
                "EMPTY_TEXT" -> "Văn bản cần dịch không được để trống."
                else -> "Dữ liệu không hợp lệ."
            }
        }
    }
}