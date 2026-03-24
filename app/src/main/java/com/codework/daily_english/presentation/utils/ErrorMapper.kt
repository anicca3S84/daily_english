package com.codework.daily_english.presentation.utils
import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.codework.daily_english.R
import com.codework.daily_english.core.result.AppError

fun AppError.asUiText(): UiText {
    return when (this) {
        is AppError.Network -> {
            UiText.DynamicString("Không có kết nối mạng")
        }

        is AppError.Timeout -> {
            UiText.DynamicString("Kết nối quá hạn (Timeout)")
        }

        is AppError.Http -> {
            // Hiển thị message từ server nếu có, không thì hiện mã lỗi
            val msg = if (this is AppError.Http && !this.message.isNullOrBlank()) {
                this.message
            } else if (this is AppError.Http) {
                "Lỗi hệ thống (${this.code})"
            } else "Lỗi kết nối máy chủ"

            UiText.DynamicString(msg)
        }

        is AppError.Validation -> {
            val reasonText = when (this.reason) {
                "EMPTY_TEXT" -> "Vui lòng nhập văn bản cần dịch"
                else -> "Dữ liệu không hợp lệ"
            }
            UiText.DynamicString(reasonText)
        }

        is AppError.Parsing -> {
            UiText.DynamicString("Lỗi xử lý dữ liệu")
        }

        // Nhánh else hoặc Unknown
        else -> {
            UiText.DynamicString("Đã xảy ra lỗi không xác định")
        }
    }
}

// Giữ nguyên class UiText bên dưới của bạn
sealed class UiText {
    data class DynamicString(val value: String) : UiText()

    class StringResource(
        @StringRes val resId: Int,
        vararg val args: Any
    ) : UiText()

    @Composable
    fun asString(): String {
        return when (this) {
            is DynamicString -> value
            is StringResource -> stringResource(resId, *args)
        }
    }

    fun asString(context: Context): String {
        return when (this) {
            is DynamicString -> value
            is StringResource -> context.getString(resId, *args)
        }
    }
}