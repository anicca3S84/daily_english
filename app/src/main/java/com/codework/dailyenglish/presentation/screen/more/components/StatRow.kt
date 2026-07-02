package com.codework.dailyenglish.presentation.screen.more.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.codework.dailyenglish.presentation.screen.more.mvi.MoreState

@Composable
fun StatsRow(data: MoreState) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatCard(
            modifier = Modifier.weight(1f),
            value = data.streak.toString(),
            label = "Streak",
            icon = Icons.Default.LocalFireDepartment,
            color = Color(0xFFFF5722)
        )
        StatCard(
            modifier = Modifier.weight(1f),
            value = data.totalXp.toString(),
            label = "Total XP",
            icon = Icons.Default.Star,
            color = Color(0xFFFFC107)
        )
        StatCard(
            modifier = Modifier.weight(1f),
            value = data.wordsLearned.toString(),
            label = "Words",
            icon = Icons.Default.MenuBook,
            color = Color(0xFF2196F3)
        )
    }
}