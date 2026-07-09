package com.codework.dailyenglish.core.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

    fun String?.toFriendlyDate(fallback: String = "December 12, 2026"): String {
        if (this.isNullOrBlank()) return fallback
        return try {
            // Parse chuỗi "2026-12-12" từ API
            val localDate = LocalDate.parse(this)
            // Định dạng sang "December 12, 2026"
            val formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy", Locale.US)
            localDate.format(formatter)
        } catch (e: Exception) {
            // Phòng hờ lỗi parse thì trả về fallback mặc định
            fallback
        }
    }
