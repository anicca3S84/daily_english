package com.codework.dailyenglish.presentation.screen.course_details.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.codework.dailyenglish.presentation.theme.CourseTheme
import com.codework.dailyenglish.presentation.theme.Spaces
import com.codework.dailyenglish.presentation.theme.TextSizes
@Composable
fun ModuleHeaderLabel(
    title: String,
    description: String,
    theme: CourseTheme
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Spaces.Space16, vertical = Spaces.Space8)
            .clip(RoundedCornerShape(Spaces.Space16))
            .background(Color(0xFFF5F5F5))
            .padding(Spaces.Space16),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = TextSizes.TextSizeMain,
                color = Color(0xFF333333)
            )

            Spacer(modifier = Modifier.height(Spaces.Space4))

            Text(
                text = description,
                color = Color(0xFF6B6B6B),
                fontSize = TextSizes.TextSize13
            )
        }

        Box(
            modifier = Modifier
                .size(Spaces.Space32)
                .background(
                    color = theme.primaryColor.copy(alpha = 0.15f),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Flag,
                contentDescription = null,
                tint = theme.primaryColor.copy(alpha = 0.9f),
                modifier = Modifier.size(Spaces.Space16)
            )
        }
    }
}