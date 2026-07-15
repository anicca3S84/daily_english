package com.codework.dailyenglish.presentation.screen.leaderboard.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codework.dailyenglish.presentation.screen.leaderboard.TextGray
import com.codework.dailyenglish.presentation.theme.AppColors

@Composable
fun CurrentPositionCard(rank: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(16.dp))
            .border(1.dp, Color(0xFFF1F5F9), RoundedCornerShape(16.dp))
            .padding(vertical = 14.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Your current position",
            color = TextGray,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.width(16.dp))
        VerticalDivider(modifier = Modifier.height(20.dp), color = Color(0xFFE2E8F0))
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = if (rank > 0) rank.toString() else "-",
            style = TextStyle(
                brush = AppColors.MainGradient,
                fontSize = 20.sp,
                fontWeight = FontWeight.Black
            )
        )
    }
}