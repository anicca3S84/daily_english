package com.codework.dailyenglish.presentation.screen.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.codework.dailyenglish.presentation.screen.home.model.QuickActionItem

@Composable
fun QuickActionSection(
    actions: List<QuickActionItem>,
    onSeeAllClick: () -> Unit = {}
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Quick Access",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E293B)
                )
            )
            TextButton(
                onClick = onSeeAllClick,
                contentPadding = PaddingValues(0.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "See All",
                        color = Color(0xFF6366F1),
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = null,
                        tint = Color(0xFF6366F1),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        // Grid 2x2
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (actions.isNotEmpty()) {
                    QuickAccessCard(
                        item = actions[0],
                        backgroundColor = Color(0xFFFFF2F5),
                        circleColor = Color(0xFFFFE3E8),
                        iconBgColor = Color(0xFFFFD1DC),
                        iconColor = Color(0xFFE91E63),
                        arrowBgColor = Color(0xFFFFE2E8),
                        modifier = Modifier.weight(1f)
                    )
                }
                if (actions.size > 1) {
                    QuickAccessCard(
                        item = actions[1],
                        backgroundColor = Color(0xFFF0F7FF),
                        circleColor = Color(0xFFE0EFFE),
                        iconBgColor = Color(0xFFD4E6FC),
                        iconColor = Color(0xFF2196F3),
                        arrowBgColor = Color(0xFFE3F2FD),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (actions.size > 2) {
                    QuickAccessCard(
                        item = actions[2],
                        backgroundColor = Color(0xFFFFF9EE),
                        circleColor = Color(0xFFFFF2D5),
                        iconBgColor = Color(0xFFFFE8C3),
                        iconColor = Color(0xFFFF9800),
                        arrowBgColor = Color(0xFFFFF3E0),
                        modifier = Modifier.weight(1f)
                    )
                }
                if (actions.size > 3) {
                    QuickAccessCard(
                        item = actions[3],
                        backgroundColor = Color(0xFFF1FBF3),
                        circleColor = Color(0xFFE2F6E7),
                        iconBgColor = Color(0xFFD0F1D8),
                        iconColor = Color(0xFF4CAF50),
                        arrowBgColor = Color(0xFFE8F5E9),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}