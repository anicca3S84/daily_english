package com.codework.dailyenglish.presentation.screen.ai_speaking.components

import androidx.compose.animation.AnimatedVisibility // Đảm bảo import đúng em này
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.codework.dailyenglish.presentation.screen.lessons.lesson.components.question.components.MicRippleAnimation
import com.codework.dailyenglish.presentation.theme.AppColors

@Composable
fun SpeakingInputBar(
    isRecording: Boolean,
    onToggleRecording: () -> Unit,
    onShowSuggestions: () -> Unit,
    onSendText: (String) -> Unit
) {
    val mainGradientBrush = Brush.linearGradient(
        colors = listOf(Color(0xFF4F46E5), Color(0xFF3B82F6))
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .padding( horizontal = 16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onShowSuggestions,
            modifier = Modifier.size(48.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Lightbulb,
                contentDescription = "Suggestions",
                tint = Color(0xFFF59E0B)
            )
        }


        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(120.dp)
        ) {
            androidx.compose.animation.AnimatedVisibility(
                visible = isRecording,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                MicRippleAnimation(micSize = 56.dp)
            }

            // Nút bấm Mic chính
            IconButton(
                onClick = onToggleRecording,
                modifier = Modifier
                    .size(56.dp)
                    .background(
                        brush = if (isRecording) mainGradientBrush else Brush.linearGradient(listOf(Color(0xFFE2E8F0), Color(0xFFE2E8F0))),
                        shape = CircleShape
                    )
            ) {
                Icon(
                    imageVector = if (isRecording) Icons.Default.Stop else Icons.Default.Mic,
                    contentDescription = "Mic",
                    tint = if (isRecording) Color.White else AppColors.BluePrimary
                )
            }
        }
    }
}