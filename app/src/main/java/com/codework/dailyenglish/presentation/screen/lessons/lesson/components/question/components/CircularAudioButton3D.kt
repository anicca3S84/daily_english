package com.codework.dailyenglish.presentation.screen.lessons.lesson.components.question.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CircularAudioButton3D(onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val yOffset = if (isPressed) 2.dp else 0.dp
    val bottomPadding = if (isPressed) 0.dp else 2.dp

    Box(
        modifier = Modifier
            .size(48.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
    ) {
        // Lớp đế 3D màu xanh đậm phía dưới
        Box(
            modifier = Modifier
                .fillMaxSize()
                .offset(y = 2.dp)
                .background(Color(0xFF84D8FF), CircleShape)
        )
        // Lớp bề mặt màu xanh nhạt phía trên có viền mảnh mềm mại
        Box(
            modifier = Modifier
                .fillMaxSize()
                .offset(y = yOffset)
                .padding(bottom = bottomPadding)
                .background(Color(0xFFDDF4FF), CircleShape)
                .border(1.5.dp, Color(0xFFB3E5FC), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.VolumeUp,
                contentDescription = "Play Audio",
                tint = Color(0xFF1CB0F6),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}