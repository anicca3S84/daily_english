package com.codework.dailyenglish.presentation.screen.lessons.lesson.components.question.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FillBlankInputArea(
    hintMask: List<String?>,
    initialAnswer: String?,
    onAnswerSelected: (String) -> Unit,
    isCorrect: Boolean?,
    modifier: Modifier = Modifier
) {
    // 1. Nhóm các chỉ mục (index) thành các "từ" riêng biệt dựa trên khoảng trắng " "
    // Cách này giúp hiển thị FlowRow đẹp mắt, tự động xuống dòng theo nguyên từ.
    val words = remember(hintMask) {
        val result = mutableListOf<List<Int>>()
        var currentWord = mutableListOf<Int>()
        for (i in hintMask.indices) {
            if (hintMask[i] == " ") {
                if (currentWord.isNotEmpty()) {
                    result.add(currentWord)
                    currentWord = mutableListOf()
                }
            } else {
                currentWord.add(i)
            }
        }
        if (currentWord.isNotEmpty()) {
            result.add(currentWord)
        }
        result
    }

    // 2. Khởi tạo mảng ký tự người dùng nhập dựa trên initialAnswer và hintMask
    val userInputChars = remember(hintMask, initialAnswer) {
        mutableStateListOf<String>().apply {
            for (i in hintMask.indices) {
                val charFromAnswer = initialAnswer?.getOrNull(i)?.toString()
                val hint = hintMask[i]
                if (hint == " ") {
                    add(" ") // Giữ nguyên khoảng trắng cố định
                } else {
                    add(charFromAnswer ?: hint ?: "")
                }
            }
        }
    }

    val focusRequesters = remember(hintMask) {
        List(hintMask.size) { FocusRequester() }
    }

    val focusNext = { currentIndex: Int ->
        for (i in (currentIndex + 1) until hintMask.size) {
            if (hintMask[i] == null) {
                focusRequesters[i].requestFocus()
                break
            }
        }
    }

    val focusPrevious = { currentIndex: Int ->
        for (i in (currentIndex - 1) downTo 0) {
            if (hintMask[i] == null) {
                focusRequesters[i].requestFocus()
                break
            }
        }
    }

    val updateAnswer = {
        val combinedAnswer = userInputChars.mapIndexed { index, char ->
            if (hintMask[index] == " ") " " else char.ifEmpty { " " }
        }.joinToString("")
        onAnswerSelected(combinedAnswer)
    }

    FlowRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        words.forEachIndexed { wordIndex, wordIndices ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                wordIndices.forEach { charIndex ->
                    val isHint = hintMask[charIndex] != null
                    val charValue = userInputChars.getOrNull(charIndex) ?: ""

                    if (isHint) {
                        // Hiển thị ô Gợi ý (Chỉ đọc)
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .background(Color(0xFFEFF6FF), RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = hintMask[charIndex] ?: "",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1D4ED8)
                            )
                        }
                    } else {
                        // Hiển thị ô Nhập liệu (Có thể chỉnh sửa)
                        val borderColor = when (isCorrect) {
                            true -> Color(0xFF10B981)  // Xanh lá nếu đúng
                            false -> Color(0xFFEF4444) // Đỏ nếu sai
                            null -> Color(0xFFD1D5DB)  // Mặc định xám
                        }

                        BasicTextField(
                            value = charValue,
                            onValueChange = { newValue ->
                                // Chỉ nhận duy nhất 1 ký tự cuối cùng vừa gõ
                                val filtered = newValue.trim().takeLast(1)
                                userInputChars[charIndex] = filtered
                                updateAnswer()

                                if (filtered.isNotEmpty()) {
                                    focusNext(charIndex)
                                }
                            },
                            textStyle = TextStyle(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1F2937),
                                textAlign = TextAlign.Center
                            ),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next
                            ),
                            modifier = Modifier
                                .size(38.dp)
                                .focusRequester(focusRequesters[charIndex])
                                .border(2.dp, borderColor, RoundedCornerShape(8.dp))
                                .background(Color.White, RoundedCornerShape(8.dp))
                                .wrapContentHeight(Alignment.CenterVertically)
                                .onKeyEvent { keyEvent ->
                                    // Xử lý sự kiện nhấn nút Backspace (xóa ngược)
                                    if (keyEvent.type == KeyEventType.KeyDown && keyEvent.key == Key.Backspace) {
                                        if (charValue.isEmpty()) {
                                            focusPrevious(charIndex)
                                            true
                                        } else {
                                            userInputChars[charIndex] = ""
                                            updateAnswer()
                                            false
                                        }
                                    } else {
                                        false
                                    }
                                }
                        )
                    }
                }
            }
            // Tạo khoảng trống rõ ràng giữa các từ
            if (wordIndex < words.lastIndex) {
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }
}