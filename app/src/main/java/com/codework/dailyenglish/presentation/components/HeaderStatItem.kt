package com.codework.dailyenglish.presentation.components
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import com.codework.dailyenglish.presentation.theme.AppIcons
import com.codework.dailyenglish.presentation.theme.Spaces
import com.codework.dailyenglish.presentation.theme.AppTextSizes

@Composable
fun HeaderStatItem(
    value: String,
    label: String,
    icon: ImageVector? = null,
    iconColor: Color? = null,
    textColor: Color = Color(0xFF263238)
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        if (icon != null && iconColor != null) {
            Surface(
                color = iconColor.copy(alpha = 0.1f),
                shape = CircleShape,
                modifier = Modifier.size(Spaces.Space40)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconColor,
                        modifier = Modifier.size(AppIcons.IconSizeSmall)
                    )
                }
            }

            Spacer(modifier = Modifier.width(Spaces.Space12))
        }
        Column {
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = AppTextSizes.TextSizeMain,
                    fontWeight = FontWeight.Bold
                ),
                color = textColor
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = AppTextSizes.TextSizeSmall
                ),
                color = Color.Gray
            )
        }
    }
}