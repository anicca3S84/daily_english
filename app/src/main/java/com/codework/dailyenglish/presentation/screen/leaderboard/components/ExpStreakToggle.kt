package com.codework.dailyenglish.presentation.screen.leaderboard.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ExpStreakToggle(selectedTab: String, onTabSelected: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(Color(0xFFF1F5F9), RoundedCornerShape(24.dp))
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TabItem(
            text = "Exp",
            isSelected = selectedTab == "EXP",
            modifier = Modifier.weight(1f),
            onClick = { onTabSelected("EXP") }
        )
        TabItem(
            text = "Streak",
            isSelected = selectedTab == "STREAK",
            modifier = Modifier.weight(1f),
            onClick = { onTabSelected("STREAK") }
        )
    }
}