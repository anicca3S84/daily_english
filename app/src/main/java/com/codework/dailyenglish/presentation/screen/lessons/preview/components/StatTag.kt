package com.codework.dailyenglish.presentation.screen.lessons.preview.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import com.codework.dailyenglish.presentation.theme.AppIcons
import com.codework.dailyenglish.presentation.theme.AppRadius
import com.codework.dailyenglish.presentation.theme.Spaces

@Composable
fun StatTag(icon: ImageVector, text: String, containerColor: androidx.compose.ui.graphics.Color, contentColor: androidx.compose.ui.graphics.Color) {
    Row(
        modifier = Modifier
            .background(containerColor, RoundedCornerShape(AppRadius.RadiusMedium))
            .padding(horizontal = Spaces.Space12, vertical = Spaces.Space6),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null, modifier = Modifier.size(AppIcons.IconSizeExtraSmall), tint = contentColor)
        Spacer(Modifier.width(Spaces.Space6))
        Text(text = text, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = contentColor)
    }
}