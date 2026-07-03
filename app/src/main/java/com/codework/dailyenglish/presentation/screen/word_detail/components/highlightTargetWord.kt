package com.codework.dailyenglish.presentation.screen.word_detail.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle

@Composable
fun highlightTargetWord(fullSentence: String, targetWord: String): androidx.compose.ui.text.AnnotatedString {
    return buildAnnotatedString {
        val lowerSentence = fullSentence.lowercase()
        val lowerTarget = targetWord.lowercase()
        val startIndex = lowerSentence.indexOf(lowerTarget)

        if (startIndex != -1) {
            val endIndex = startIndex + lowerTarget.length
            append(fullSentence.substring(0, startIndex))
            withStyle(style = SpanStyle(fontWeight = FontWeight.Black, color = Color(0xFF0F172A))) {
                append(fullSentence.substring(startIndex, endIndex))
            }
            append(fullSentence.substring(endIndex))
        } else {
            append(fullSentence)
        }
    }
}