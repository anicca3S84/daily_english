package com.codework.dailyenglish.presentation.screen.leaderboard.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codework.dailyenglish.presentation.screen.leaderboard.TextGray
import com.codework.dailyenglish.presentation.theme.AppColors

@Composable
fun TabItem(text: String, isSelected: Boolean, modifier: Modifier, onClick: () -> Unit) {
    val backgroundBrush = if (isSelected) {
        AppColors.MainGradient
    } else {
        Brush.linearGradient(listOf(Color.Transparent, Color.Transparent))
    }

    Box(
        modifier = modifier
            .fillMaxHeight()
            .clip(RoundedCornerShape(20.dp))
            .background(brush = backgroundBrush)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (isSelected) Color.White else TextGray,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
    }
}