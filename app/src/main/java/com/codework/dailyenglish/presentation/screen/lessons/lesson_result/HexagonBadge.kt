package com.codework.dailyenglish.presentation.screen.lessons.lesson_result

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun HexagonBadge(modifier: Modifier = Modifier) {
    val hexagonShape = GenericShape { size, _ ->
        val w = size.width
        val h = size.height
        moveTo(w * 0.5f, 0f)
        lineTo(w, h * 0.25f)
        lineTo(w, h * 0.75f)
        lineTo(w * 0.5f, h)
        lineTo(0f, h * 0.75f)
        lineTo(0f, h * 0.25f)
        close()
    }

    Box(
        modifier = modifier
            .background(Color(0xFFFBBF24), shape = hexagonShape)
            .padding(8.dp)
            .background(Color(0xFFFEF08A), shape = hexagonShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Rounded.Star,
            contentDescription = null,
            tint = Color(0xFFF59E0B),
            modifier = Modifier.size(68.dp)
        )
    }
}