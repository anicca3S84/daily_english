package com.codework.dailyenglish.presentation.screen.lessons.lesson.components.question

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.drawBehind

@Composable
fun FillBlankQuestion(
    prompt: String,
    sentenceWithBlank: String,
    options: List<String>,
    initialAnswer: String? = null,
    onAnswerSelected: (String) -> Unit
) {
    var currentAnswer by remember { mutableStateOf(initialAnswer ?: "") }
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        Text(
            text = prompt,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1F2937),
            modifier = Modifier.padding(bottom = 32.dp)
        )

        SentenceDisplay(
            sentenceWithBlank = sentenceWithBlank,
            currentAnswer = currentAnswer
        )

        Spacer(modifier = Modifier.height(48.dp))

        if (options.isNotEmpty()) {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalArrangement = Arrangement.spacedBy(12.dp),
                maxItemsInEachRow = 3
            ) {
                options.forEach { option ->
                    val isSelected = option == currentAnswer
                    OptionChip(
                        text = option,
                        isSelected = isSelected,
                        onClick = {
                            currentAnswer = if (isSelected) "" else option
                            onAnswerSelected(currentAnswer)
                        }
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                }
            }
        } else {
            OutlinedTextField(
                value = currentAnswer,
                onValueChange = { newValue ->
                    currentAnswer = newValue
                    onAnswerSelected(newValue)
                },
                placeholder = { Text("Gõ câu trả lời của bạn...") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF3B82F6),
                    unfocusedBorderColor = Color(0xFFE5E7EB),
                    focusedContainerColor = Color(0xFFEFF6FF),
                    unfocusedContainerColor = Color(0xFFF9FAFB)
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                ),
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            )
        }
    }
}

@Composable
private fun SentenceDisplay(
    sentenceWithBlank: String,
    currentAnswer: String
) {
    val parts = sentenceWithBlank.split("____")

    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalArrangement = Arrangement.Start
    ) {
        parts.forEachIndexed { index, textPart ->
            Text(
                text = textPart,
                fontSize = 22.sp,
                lineHeight = 34.sp,
                color = Color(0xFF374151),
                modifier = Modifier.align(Alignment.CenterVertically)
            )

            if (index < parts.size - 1) {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .align(Alignment.CenterVertically)
                ) {
                    if (currentAnswer.isNotEmpty()) {
                        Text(
                            text = currentAnswer,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2563EB),
                            modifier = Modifier
                                .padding(horizontal = 2.dp)
                                .drawBottomLine(Color(0xFF2563EB))
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .width(60.dp)
                                .height(2.dp)
                                .offset(y = 12.dp)
                                .background(Color(0xFF9CA3AF))
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun OptionChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) Color(0xFFDBEAFE) else Color.White
    val borderColor = if (isSelected) Color(0xFF3B82F6) else Color(0xFFE5E7EB)
    val textColor = if (isSelected) Color(0xFF1D4ED8) else Color(0xFF4B5563)
    val elevation = if (isSelected) 0.dp else 2.dp
    val shadowOffset = if (isSelected) 0.dp else 4.dp

    Box(
        modifier = Modifier
            .padding(vertical = 4.dp)
            .height(50.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
    ) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = backgroundColor,
            border = BorderStroke(2.dp, borderColor),
            shadowElevation = elevation,
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.padding(horizontal = 20.dp)
            ) {
                Text(
                    text = text,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

private fun Modifier.drawBottomLine(color: Color) = this.run {
    drawBehind {
        val strokeWidth = 2.dp.toPx()
        val y = size.height - strokeWidth / 2
        drawLine(
            color = color,
            start = Offset(0f, y),
            end = Offset(size.width, y),
            strokeWidth = strokeWidth
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, name = "1. Có Options (Dạng Chọn)")
@Composable
fun FillBlankQuestionPreview_WithOptions() {
    MaterialTheme {
        Box(modifier = Modifier.padding(vertical = 24.dp)) {
            FillBlankQuestion(
                prompt = "Điền từ thích hợp vào chỗ trống:",
                sentenceWithBlank = "I eat an ____ every morning.",
                options = listOf("apple", "banana", "orange", "grape"),
                initialAnswer = "",
                onAnswerSelected = {}
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, name = "2. Không có Options (Dạng Gõ)")
@Composable
fun FillBlankQuestionPreview_Typing() {
    MaterialTheme {
        Box(modifier = Modifier.padding(vertical = 24.dp)) {
            FillBlankQuestion(
                prompt = "Nghe và viết lại từ bạn nghe được:",
                sentenceWithBlank = "Nice to ____ you.",
                options = emptyList(),
                initialAnswer = "meet",
                onAnswerSelected = {}
            )
        }
    }
}