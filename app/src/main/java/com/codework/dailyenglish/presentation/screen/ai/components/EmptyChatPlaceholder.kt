package com.codework.dailyenglish.presentation.screen.ai.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codework.dailyenglish.presentation.theme.AppColors

@Composable
fun EmptyChatPlaceholder() {
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center, modifier = Modifier.padding(32.dp)) {
        Box(modifier = Modifier.size(120.dp).background(brush = AppColors.MainGradient, shape = CircleShape, alpha = 0.1f), contentAlignment = Alignment.Center) {
            Icon(Icons.Rounded.AutoAwesome, null, modifier = Modifier.size(70.dp), tint = AppColors.BluePrimary)
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(text = "Anicca English Tutor", style = MaterialTheme.typography.headlineSmall.copy(brush = AppColors.MainGradient, fontWeight = FontWeight.ExtraBold, fontSize = 24.sp, textAlign = TextAlign.Center), modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = "Your intelligent assistant for everything.\nAsk me anything, from learning languages to solving complex problems!", style = MaterialTheme.typography.bodyLarge, color = Color.Gray, textAlign = TextAlign.Center, lineHeight = 22.sp)
    }
}