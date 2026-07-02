package com.codework.dailyenglish.presentation.screen.practice.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

@Composable
fun SectionTitle(title: String, subtitle: String) {
    Column {
        Text(text = title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold)
        Text(text = subtitle, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
    }
}