package com.codework.dailyenglish.presentation.screen.lessons.lesson_result

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.withTransform

@Composable
fun ConfettiBackground(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val colors = listOf(
            Color(0xFFFCD34D),
            Color(0xFFFCA5A5),
            Color(0xFF86EFAC),
            Color(0xFF93C5FD),
            Color(0xFFF97316)
        )

        val drawConfetti = { xRatio: Float, yRatio: Float, rotation: Float, colorIdx: Int, isCircle: Boolean ->
            withTransform({
                translate(left = size.width * xRatio, top = size.height * yRatio)
                rotate(rotation)
            }) {
                if (isCircle) {
                    drawRoundRect(
                        color = colors[colorIdx],
                        size = Size(16f, 16f),
                        cornerRadius = CornerRadius(8f, 8f)
                    )
                } else {
                    drawRect(
                        color = colors[colorIdx],
                        size = Size(12f, 28f)
                    )
                }
            }
        }
    }
}