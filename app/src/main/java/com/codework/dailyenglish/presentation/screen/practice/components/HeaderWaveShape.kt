package com.codework.dailyenglish.presentation.screen.practice.components

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

class HeaderWaveShape : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path().apply {
            moveTo(0f, 0f)
            lineTo(0f, size.height * 0.72f)

            cubicTo(
                x1 = size.width * 0.28f,
                y1 = size.height * 0.84f,
                x2 = size.width * 0.72f,
                y2 = size.height * 0.60f,
                x3 = size.width,
                y3 = size.height * 0.72f
            )

            lineTo(size.width, 0f)
            close()
        }
        return Outline.Generic(path)
    }
}