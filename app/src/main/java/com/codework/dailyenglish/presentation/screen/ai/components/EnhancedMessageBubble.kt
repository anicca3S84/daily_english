package com.codework.dailyenglish.presentation.screen.ai.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.codework.dailyenglish.domain.ai.model.ChatMessage
import com.codework.dailyenglish.presentation.theme.AppColors

@Composable
fun EnhancedMessageBubble(message: ChatMessage) {
    val isUser = message.isUser
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = if (isUser) Alignment.End else Alignment.Start) {
        Surface(shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp, bottomStart = if (isUser) 20.dp else 4.dp, bottomEnd = if (isUser) 4.dp else 20.dp), color = Color.Transparent, shadowElevation = if (isUser) 4.dp else 1.dp) {
            Box(modifier = Modifier.background(brush = if (isUser) AppColors.MainGradient else Brush.linearGradient(listOf(Color.White, Color.White)))) {
                Text(text = message.text, modifier = Modifier.padding(12.dp), color = if (isUser) Color.White else Color(0xFF333333), style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}