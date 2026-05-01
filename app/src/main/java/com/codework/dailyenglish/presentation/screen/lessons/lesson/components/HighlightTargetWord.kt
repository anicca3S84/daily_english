package com.codework.dailyenglish.presentation.screen.lessons.lesson.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import com.codework.dailyenglish.presentation.theme.AppColors

@Composable
fun HighlightTargetWord(example: String, targetWord: String): androidx.compose.ui.text.AnnotatedString {
    return buildAnnotatedString {
        val lowerExample = example.lowercase()
        val lowerTarget = targetWord.lowercase()
        val startIndex = lowerExample.indexOf(lowerTarget)

        if (startIndex >= 0) {
            append(example.substring(0, startIndex))
            withStyle(
                style = SpanStyle(
                    fontWeight = FontWeight.Bold,
                    textDecoration = TextDecoration.Underline,
                    color = AppColors.Slate900
                )
            ) {
                append(example.substring(startIndex, startIndex + targetWord.length))
            }
            append(example.substring(startIndex + targetWord.length))
        } else {
            append(example)
        }
    }
}