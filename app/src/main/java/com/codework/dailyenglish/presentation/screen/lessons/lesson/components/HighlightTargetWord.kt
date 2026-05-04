package com.codework.dailyenglish.presentation.screen.lessons.lesson.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import com.codework.dailyenglish.presentation.theme.AppColors
import com.codework.dailyenglish.presentation.theme.AppTextSizes
import com.codework.dailyenglish.presentation.theme.Spaces

@Composable
fun HighlightTargetWord(
    example: String,
    targetWord: String,
    modifier: Modifier = Modifier
) {
    val annotatedString = buildAnnotatedString {
        val lowerExample = example.lowercase()
        val lowerTarget = targetWord.lowercase()
        val startIndex = lowerExample.indexOf(lowerTarget)

        if (startIndex >= 0) {
            append(example.substring(0, startIndex))
            withStyle(
                style = SpanStyle(
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                )
            ) {
                append(example.substring(startIndex, startIndex + targetWord.length))
            }
            append(example.substring(startIndex + targetWord.length))
        } else {
            append(example)
        }
    }

    Text(
        text = annotatedString,
        fontSize = AppTextSizes.TextSizeMain,
        color = AppColors.GrayText,
        lineHeight = AppTextSizes.TextSize24,
        modifier = modifier
    )
}