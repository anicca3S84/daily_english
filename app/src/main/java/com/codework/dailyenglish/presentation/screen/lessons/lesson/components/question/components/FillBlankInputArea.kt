package com.codework.dailyenglish.presentation.screen.lessons.lesson.components.question.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.animation.animateColorAsState


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FillBlankInputArea(
    hintMask: List<String?>,
    initialAnswer: String? = null,
    onAnswerSelected: (String) -> Unit,
    isCorrect: Boolean? = null // Nhận trạng thái chấm điểm
) {
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    val totalBoxes = hintMask.size
    val currentText = initialAnswer?.lowercase() ?: ""

    val isEvaluated = isCorrect != null
    val isActive = currentText.isNotEmpty()

    // 1. Màu nền động
    val surfaceBgColor by animateColorAsState(
        targetValue = when {
            isEvaluated && isCorrect == true -> Color(0xFFD7FFB8)  // Xanh lá dịu
            isEvaluated && isCorrect == false -> Color(0xFFFFE0E0) // Đỏ hồng dịu
            isActive -> Color(0xFFEFF6FF)                         // Xanh dương khi đang soạn thảo
            else -> Color.White
        },
        label = "bg"
    )

    // 2. Màu viền động
    val surfaceBorderColor by animateColorAsState(
        targetValue = when {
            isEvaluated && isCorrect == true -> Color(0xFF58CC02)  // Viền xanh lá
            isEvaluated && isCorrect == false -> Color(0xFFFF4B4B) // Viền đỏ
            isActive -> Color(0xFF93C5FD)                         // Viền xanh dương khi đang soạn thảo
            else -> Color(0xFFF3F4F6)
        },
        label = "border"
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                enabled = !isEvaluated, // Vô hiệu hóa tính năng bấm khi đã chấm điểm xong
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                focusRequester.requestFocus()
            },
        shape = RoundedCornerShape(16.dp),
        color = surfaceBgColor,
        border = BorderStroke(1.dp, surfaceBorderColor)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 32.dp, horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            BasicTextField(
                value = currentText,
                onValueChange = { newValue ->
                    if (!isEvaluated) { // Chặn chỉnh sửa nội dung sau khi chấm điểm
                        val filtered = newValue.replace(Regex("[^a-zA-Z0-9]"), "").lowercase()
                        if (filtered.length <= totalBoxes) {
                            onAnswerSelected(filtered)
                        }
                    }
                },
                modifier = Modifier
                    .focusRequester(focusRequester)
                    .fillMaxWidth(),
                enabled = !isEvaluated, // Ngăn việc gọi bàn phím ảo lên
                readOnly = isEvaluated, // Cấu hình chỉ đọc
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                ),
                decorationBox = { innerTextField ->
                    Box(contentAlignment = Alignment.Center) {
                        Box(modifier = Modifier.alpha(0f).size(0.dp)) {
                            innerTextField()
                        }

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(24.dp)
                        ) {
                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                for (index in 0 until totalBoxes) {
                                    val isTyped = index < currentText.length

                                    val charToShow = if (isTyped) {
                                        currentText[index].toString()
                                    } else {
                                        hintMask.getOrNull(index)?.lowercase() ?: ""
                                    }

                                    CharacterSlot(
                                        char = charToShow,
                                        isHint = !isTyped && charToShow.isNotEmpty()
                                    )
                                }
                            }

                            // Chỉ hiển thị dòng chữ hướng dẫn "Tap here to type" khi chưa đánh giá kết quả
                            if (!isEvaluated) {
                                Text(
                                    text = "Tap here to type",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Medium,
                                    fontStyle = FontStyle.Italic,
                                    color = Color(0xFF9CA3AF)
                                )
                            }
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun CharacterSlot(
    char: String,
    isHint: Boolean
) {
    val displayText = char.ifEmpty { "_" }
    val textColor = when {
        char.isEmpty() -> Color(0xFF9CA3AF) // Gray underscore
        isHint -> Color(0xFF9CA3AF)         // Green hint
        else -> Color(0xFF374151)           // Dark gray typed text
    }

    Text(
        text = displayText,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        color = textColor,
        modifier = Modifier.padding(horizontal = 2.dp)
    )
}