package com.codework.daily_english.presentation.components
import android.R.attr.alpha
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Spellcheck
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.codework.daily_english.presentation.screen.home.HomeEvent


@Composable
fun QuickActionSection(onEvent: (HomeEvent) -> Unit) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = "Quick Tools",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween // Chia đều khoảng cách
        ) {
            QuickActionItem(
                icon = Icons.Default.SmartToy, // Icon Robot cho AI
                label = "Ask AI",
                color = Color(0xFFE91E63), // Màu hồng
                onClick = { onEvent(HomeEvent.OnQuickActionGemini) }
            )

            QuickActionItem(
                icon = Icons.Default.Book,
                label = "Dictionary",
                color = Color(0xFF2196F3), // Màu xanh
                onClick = { onEvent(HomeEvent.OnQuickActionDictionary) }
            )

            QuickActionItem(
                icon = Icons.Default.Spellcheck,
                label = "Grammar",
                color = Color(0xFFFF9800), // Màu cam
                onClick = { onEvent(HomeEvent.OnQuickActionGrammar) }
            )

            QuickActionItem(
                icon = Icons.Default.Translate,
                label = "Translate",
                color = Color(0xFF4CAF50), // Màu lục
                onClick = { onEvent(HomeEvent.OnQuickActionTranslate) }
            )
        }
    }
}

@Composable
fun QuickActionItem(
    icon: ImageVector,
    label: String,
    color: Color,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        // Vòng tròn nền Icon
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = color.copy(alpha = 0.1f),
            modifier = Modifier.size(56.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color, // Màu icon đậm
                    modifier = Modifier.size(28.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Label
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Medium,
            color = Color.Gray
        )
    }
}