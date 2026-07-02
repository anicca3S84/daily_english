package com.codework.dailyenglish.presentation.screen.ai_speaking.components

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SpeakingInputBar(
    isRecording: Boolean,
    onToggleRecording: () -> Unit,
    onShowSuggestions: () -> Unit, // Callback mới
    onSendText: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 1. Nút Bóng đèn (Gợi ý)
        IconButton(
            onClick = onShowSuggestions,
            modifier = Modifier.size(48.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Lightbulb, // Icon bóng đèn
                contentDescription = "Suggestions",
                tint = Color(0xFFF59E0B) // Màu vàng cam
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        // 2. Nút Micro (Chính)
        IconButton(
            onClick = onToggleRecording,
            modifier = Modifier
                .size(56.dp)
                .background(if (isRecording) Color.Red else Color(0xFF4F46E5), CircleShape)
        ) {
            Icon(
                imageVector = if (isRecording) Icons.Default.Stop else Icons.Default.Mic,
                contentDescription = "Mic",
                tint = Color.White
            )
        }

        // ... (phần input text nếu có)
    }
}