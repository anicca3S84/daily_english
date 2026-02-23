package com.codework.daily_english.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.codework.daily_english.presentation.theme.AppSurface
import com.codework.daily_english.presentation.theme.GrayText
import com.codework.daily_english.presentation.theme.SuccessGreen
import com.codework.daily_english.presentation.theme.TextPrimary

@Composable
fun QuizOption(
    text: String,
    index: Int,
    selectedIndex: Int?,
    correctIndex: Int,
    onClick: (Int) -> Unit
) {
    val isAnswered = selectedIndex != null
    val isSelected = selectedIndex == index

    val bgColor = when {
        !isAnswered -> AppSurface
        index == correctIndex -> Color(0xFFECFDF5)
        isSelected && index != correctIndex -> Color(0xFFFEF2F2)
        else -> AppSurface
    }
    val borderColor = when {
        !isAnswered -> Color(0xFFE5E7EB)
        index == correctIndex -> SuccessGreen
        isSelected && index != correctIndex -> Color(0xFFEF4444)
        else -> Color(0xFFE5E7EB)
    }
    val textColor = when {
        !isAnswered -> TextPrimary
        index == correctIndex -> SuccessGreen
        isSelected && index != correctIndex -> Color(0xFFEF4444)
        else -> GrayText
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .clickable(enabled = !isAnswered) { onClick(index) },
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, borderColor),
        color = bgColor
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(8.dp)) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = textColor,
                textAlign = TextAlign.Center,
                maxLines = 2
            )
            if (isAnswered) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopEnd) {
                    if (index == correctIndex) Icon(Icons.Default.CheckCircle, null, tint = SuccessGreen, modifier = Modifier.size(16.dp))
                    else if (isSelected) Icon(Icons.Default.Cancel, null, tint = Color(0xFFEF4444), modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}