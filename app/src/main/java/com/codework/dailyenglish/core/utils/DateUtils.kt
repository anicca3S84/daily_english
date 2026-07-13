package com.codework.dailyenglish.core.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale


fun String?.toFriendlyDate(): String {
    if (this.isNullOrBlank()) return ""
    return try {
        val datePart = this.substringBefore("T")
        val localDate = LocalDate.parse(datePart)
        val formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy", Locale.US)
        localDate.format(formatter)
    } catch (e: Exception) {
        this
    }
}