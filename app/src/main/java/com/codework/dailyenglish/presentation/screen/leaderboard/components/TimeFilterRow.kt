package com.codework.dailyenglish.presentation.screen.leaderboard.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codework.dailyenglish.presentation.screen.leaderboard.TextGray
import com.codework.dailyenglish.presentation.theme.AppColors

@Composable
fun TimeFilterRow(selectedFilter: String, onFilterSelected: (String) -> Unit) {
    val filters = listOf("All", "This Week", "This Month")

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        filters.forEach { filter ->
            val isSelected = selectedFilter.equals(filter, ignoreCase = true)

            val backgroundBrush = if (isSelected) {
                Brush.linearGradient(
                    colors = listOf(
                        AppColors.BluePrimary.copy(alpha = 0.12f),
                        AppColors.IndigoPurple.copy(alpha = 0.12f)
                    )
                )
            } else {
                Brush.linearGradient(listOf(Color.Transparent, Color.Transparent))
            }

            val textStyle = if (isSelected) {
                TextStyle(
                    brush = AppColors.MainGradient,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            } else {
                TextStyle(
                    color = TextGray,
                    fontWeight = FontWeight.Medium,
                    fontSize = 13.sp
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(brush = backgroundBrush)
                    .clickable { onFilterSelected(filter) }
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = filter,
                    style = textStyle
                )
            }
            if (filter != filters.last()) {
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }
}