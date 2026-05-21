package com.codework.dailyenglish.presentation.utils

import kotlin.math.max

fun String.cleanForSpeaking(): String {
    return this.replace(Regex("[^a-zA-Z0-9 ]"), "").trim().lowercase()
}

fun String.levenshteinDistance(other: String): Int {
    val dp = Array(this.length + 1) { IntArray(other.length + 1) }

    for (i in 0..this.length) dp[i][0] = i
    for (j in 0..other.length) dp[0][j] = j

    for (i in 1..this.length) {
        for (j in 1..other.length) {
            val cost = if (this[i - 1] == other[j - 1]) 0 else 1
            dp[i][j] = minOf(
                dp[i - 1][j] + 1,      // Xoá
                dp[i][j - 1] + 1,      // Thêm
                dp[i - 1][j - 1] + cost // Thay thế
            )
        }
    }
    return dp[this.length][other.length]
}

/**
 * Trả về độ tương đồng giữa hai chuỗi theo tỷ lệ phần trăm (0.0 đến 1.0).
 */
fun String.calculateSimilarity(other: String): Float {
    val s1 = this.cleanForSpeaking()
    val s2 = other.cleanForSpeaking()

    if (s1 == s2) return 1.0f
    if (s1.isEmpty() || s2.isEmpty()) return 0.0f

    val maxLen = max(s1.length, s2.length)
    val distance = s1.levenshteinDistance(s2)

    return 1.0f - (distance.toFloat() / maxLen)
}