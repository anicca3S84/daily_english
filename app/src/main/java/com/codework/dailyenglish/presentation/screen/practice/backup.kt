package com.codework.dailyenglish.presentation.screen.practice

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.codework.dailyenglish.presentation.screen.practice.mvi.PracticeEvent

//item {
//    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
//        SectionTitle(title = "Smart Review 🧠", subtitle = "Based on your forgetting curve")
//        Spacer(modifier = Modifier.height(12.dp))
//        SpacedRepetitionCard(
//            topicName = "Family & Friends",
//            dueWords = 15,
//            onClick = { onEvent(PracticeEvent.OnFlashcardClick) } // Đã nối sự kiện
//        )
//    }
//}
//
//@Composable
//fun SpacedRepetitionCard(topicName: String, dueWords: Int, onClick: () -> Unit) {
//    Card(
//        onClick = onClick,
//        modifier = Modifier.fillMaxWidth(),
//        shape = RoundedCornerShape(24.dp),
//        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f))
//    ) {
//        Row(
//            modifier = Modifier.padding(20.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Box(contentAlignment = Alignment.Center) {
//                CircularProgressIndicator(
//                    progress = { 0.7f },
//                    modifier = Modifier.size(60.dp),
//                    color = MaterialTheme.colorScheme.primary,
//                    trackColor = Color.White
//                )
//                Text(text = "70%", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
//            }
//
//            Spacer(modifier = Modifier.width(16.dp))
//
//            Column(modifier = Modifier.weight(1f)) {
//                Text(text = "Flashcard Review", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
//                Text(text = topicName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
//                Text(text = "$dueWords words due today", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
//            }
//
//            Surface(shape = CircleShape, color = MaterialTheme.colorScheme.primary) {
//                Icon(Icons.AutoMirrored.Filled.ArrowForward, null, tint = Color.White, modifier = Modifier.padding(8.dp))
//            }
//        }
//    }
//}