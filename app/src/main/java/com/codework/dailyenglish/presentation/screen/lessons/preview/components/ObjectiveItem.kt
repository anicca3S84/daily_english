package com.codework.dailyenglish.presentation.screen.lessons.preview.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.codework.dailyenglish.presentation.theme.AppTextSizes
import com.codework.dailyenglish.presentation.theme.Spaces

@Composable
fun ObjectiveItem(text: String) {
    Row(
        verticalAlignment = Alignment.Top,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = Icons.Rounded.CheckCircle,
            contentDescription = null,
            tint = Color(0xFF22C55E),
            modifier = Modifier
                .size(Spaces.Space22)
                .padding(top = Spaces.Space2)
        )

        Spacer(modifier = Modifier.width(Spaces.Space12))

        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = AppTextSizes.TextSize15
            ),
            color = Color(0xFF374151),
            lineHeight = Spaces.Space22.value.sp,
            fontWeight = FontWeight.Medium
        )
    }
}