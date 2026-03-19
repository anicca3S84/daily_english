package com.codework.daily_english.presentation.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    private val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())

    fun formatTime(timestamp: Long): String {
        return formatter.format(Date(timestamp))
    }
}