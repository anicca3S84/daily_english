package com.codework.dailyenglish.presentation.utils

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.codework.dailyenglish.R
import com.codework.dailyenglish.core.network.AppError
import com.codework.dailyenglish.core.utils.Constants

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

fun AppError.asUiText(): UiText {
    return when (this) {
        is AppError.Network -> UiText.StringResource(R.string.error_network)
        is AppError.Timeout -> UiText.StringResource(R.string.error_timeout)
        is AppError.Http -> {
            if (!this.message.isNullOrBlank()) {
                UiText.DynamicString(this.message)
            } else {
                UiText.StringResource(R.string.error_http_unknown, this.code)
            }
        }
        is AppError.Validation -> {
            val resId = when (this.reason) {
                Constants.EMPTY_TEXT -> R.string.error_empty_text
                else -> R.string.error_invalid_data
            }
            UiText.StringResource(resId)
        }
        is AppError.Parsing -> UiText.StringResource(R.string.error_parsing)
        else -> UiText.StringResource(R.string.error_unknown)
    }
}