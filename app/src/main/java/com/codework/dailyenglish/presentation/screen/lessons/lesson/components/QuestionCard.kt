package com.codework.dailyenglish.presentation.screen.lessons.lesson.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.codework.dailyenglish.domain.lesson.model.StepData
import com.codework.dailyenglish.presentation.theme.GrayText

@Composable
fun QuestionCard(
    data: StepData.Question,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val isAlreadyAnswered = data.selectedOptionId != null

    Card(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp), // Đổi thành 0dp nếu muốn phẳng hoặc giữ 2dp
        border = BorderStroke(1.dp, Color(0xFFF1F5F9)), // Viền nhẹ cho Card
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            // 1. QUESTION HEADER
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = data.content,
                    style = MaterialTheme.typography.headlineSmall, // Dùng headlineSmall cho đỡ bị quá to
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF1E293B),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = if (isAlreadyAnswered) "Kết quả của bạn:" else "Chọn đáp án đúng:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = GrayText
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // 2. OPTIONS LIST
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(data.options) { option ->
                    val isSelected = data.selectedOptionId == option.id

                    // Logic màu sắc: Ưu tiên hiển thị đúng/sai nếu đã Answered
                    val animatedBgColor by animateColorAsState(
                        targetValue = when {
                            isAlreadyAnswered && option.isCorrect -> Color(0xFFDCFCE7) // Xanh lục nhạt
                            isAlreadyAnswered && isSelected && !option.isCorrect -> Color(0xFFFEE2E2) // Đỏ nhạt
                            !isAlreadyAnswered && isSelected -> Color(0xFFEFF6FF) // Xanh dương nhạt (đang chọn)
                            else -> Color.White
                        }, label = "bgColor"
                    )

                    val animatedBorderColor by animateColorAsState(
                        targetValue = when {
                            isAlreadyAnswered && option.isCorrect -> Color(0xFF22C55E) // Xanh lục
                            isAlreadyAnswered && isSelected && !option.isCorrect -> Color(0xFFEF4444) // Đỏ
                            !isAlreadyAnswered && isSelected -> Color(0xFF3B82F6) // Xanh dương
                            else -> Color(0xFFE2E8F0)
                        }, label = "borderColor"
                    )

                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        color = animatedBgColor,
                        border = BorderStroke(2.dp, animatedBorderColor),
                        tonalElevation = if (isSelected) 2.dp else 0.dp
                    ) {
                        Row(
                            modifier = Modifier
                                .clickable(enabled = !isAlreadyAnswered) {
                                    onOptionSelected(option.id)
                                }
                                .padding(20.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = option.content,
                                modifier = Modifier.weight(1f),
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = if (isSelected || (isAlreadyAnswered && option.isCorrect))
                                    FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected || (isAlreadyAnswered && option.isCorrect))
                                    Color(0xFF1E293B) else Color(0xFF64748B)
                            )

                            if (isAlreadyAnswered) {
                                when {
                                    option.isCorrect -> Text("✅")
                                    isSelected && !option.isCorrect -> Text("❌")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}