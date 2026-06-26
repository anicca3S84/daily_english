package com.codework.dailyenglish.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource // Import cái này để dùng R.drawable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.codework.dailyenglish.presentation.screen.home.model.QuickActionItem // Import Data Class của bạn

@Composable
fun QuickActionCard(
    iconResId: Int,
    label: String,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.aspectRatio(1f), // Ép thành hình vuông
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, color.copy(alpha = 0.1f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                shape = CircleShape,
                color = color.copy(alpha = 0.1f),
                modifier = Modifier.size(56.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    // SỬA LẠI: Dùng painterResource cho R.drawable
                    Icon(
                        painter = painterResource(id = iconResId),
                        contentDescription = null,
                        tint = color,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = label,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
fun QuickActionSection(
    title: String = "Quick Access",
    actions: List<QuickActionItem>
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))

        val rows = actions.chunked(2)

        rows.forEachIndexed { index, rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Item 1
                QuickActionCard( // Gọi Composable tên mới
                    iconResId = rowItems[0].icon,
                    label = rowItems[0].label,
                    color = rowItems[0].color,
                    onClick = rowItems[0].onClick,
                    modifier = Modifier.weight(1f) // Truyền modifier vào đây
                )

                // Item 2 (Nếu có)
                if (rowItems.size > 1) {
                    QuickActionCard(
                        iconResId = rowItems[1].icon,
                        label = rowItems[1].label,
                        color = rowItems[1].color,
                        onClick = rowItems[1].onClick,
                        modifier = Modifier.weight(1f)
                    )
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }

            if (index < rows.lastIndex) {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}