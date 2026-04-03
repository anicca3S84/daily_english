package com.codework.daily_english.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.codework.daily_english.presentation.theme.Spaces


@Composable
fun GridBackground() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val gridSize = Spaces.Space24.toPx()
        val gridColor = Color.Gray.copy(alpha = 0.1f)
        val strokeWidth = Spaces.Space1.toPx()
        var x = 0f
        while (x < canvasWidth) {
            drawLine(color = gridColor, start = Offset(x, 0f), end = Offset(x, canvasHeight), strokeWidth = strokeWidth)
            x += gridSize
        }
        var y = 0f
        while (y < canvasHeight) {
            drawLine(color = gridColor, start = Offset(0f, y), end = Offset(canvasWidth, y), strokeWidth = strokeWidth)
            y += gridSize
        }
    }
}