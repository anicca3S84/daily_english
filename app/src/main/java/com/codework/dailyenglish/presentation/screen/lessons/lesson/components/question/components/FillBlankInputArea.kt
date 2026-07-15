package com.codework.dailyenglish.presentation.screen.lessons.lesson.components.question.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.input.TextFieldValue // Import thêm thư viện này
import androidx.compose.ui.text.TextRange       // Import thêm thư viện này

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FillBlankInputArea(
    hintMask: List<String?>,
    initialAnswer: String?,
    onAnswerSelected: (String) -> Unit,
    isCorrect: Boolean?,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current

    val lastEditableIndex = remember(hintMask) {
        hintMask.indices.lastOrNull { hintMask[it] == null } ?: -1
    }

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

    // Khởi tạo mảng dưới dạng TextFieldValue để kiểm soát vị trí con trỏ (luôn ở sau ký tự)
    val userInputChars = remember(hintMask, initialAnswer) {
        mutableStateListOf<TextFieldValue>().apply {
            for (i in hintMask.indices) {
                val charFromAnswer = initialAnswer?.getOrNull(i)?.toString()
                val hint = hintMask[i]
                if (hint == " ") {
                    add(TextFieldValue(" ", TextRange(1)))
                } else {
                    val initialVal = charFromAnswer ?: hint ?: " "
                    val finalVal = if (initialVal.isEmpty()) " " else initialVal
                    add(TextFieldValue(finalVal, TextRange(finalVal.length)))
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

    val updateAnswer = {
        val combinedAnswer = userInputChars.mapIndexed { index, tfv ->
            if (hintMask[index] == " ") " " else if (tfv.text == " ") " " else tfv.text
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
                    val textFieldValue = userInputChars.getOrNull(charIndex) ?: TextFieldValue(" ", TextRange(1))

                    if (isHint) {
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
                        val borderColor = when (isCorrect) {
                            true -> Color(0xFF10B981)
                            false -> Color(0xFFEF4444)
                            null -> Color(0xFFD1D5DB)
                        }

                        val isLastEditable = charIndex == lastEditableIndex

                        BasicTextField(
                            value = textFieldValue,
                            onValueChange = { newValue ->
                                val text = newValue.text
                                if (text.isEmpty()) {
                                    // Người dùng nhấn nút xóa (Backspace)
                                    if (textFieldValue.text == " ") {
                                        // Nếu ô hiện tại đã trống sẵn (" "), tìm ô nhập gần nhất phía trước để xóa và chuyển focus
                                        var prevEditableIndex = -1
                                        for (i in (charIndex - 1) downTo 0) {
                                            if (hintMask[i] == null) {
                                                prevEditableIndex = i
                                                break
                                            }
                                        }
                                        if (prevEditableIndex != -1) {
                                            // Xóa ký tự ở ô trước đó và ép con trỏ nằm sau khoảng trắng
                                            userInputChars[prevEditableIndex] = TextFieldValue(" ", TextRange(1))
                                            updateAnswer()
                                            focusRequesters[prevEditableIndex].requestFocus()
                                        }
                                    } else {
                                        // Nếu ô hiện tại đang có chữ, đưa nó về trạng thái trống " "
                                        userInputChars[charIndex] = TextFieldValue(" ", TextRange(1))
                                        updateAnswer()
                                    }
                                } else {
                                    // Người dùng nhập ký tự mới
                                    val filtered = text.replace(" ", "").takeLast(1)
                                    if (filtered.isNotEmpty()) {
                                        userInputChars[charIndex] = TextFieldValue(filtered, TextRange(filtered.length))
                                        updateAnswer()

                                        if (isLastEditable) {
                                            focusManager.clearFocus()
                                        } else {
                                            focusNext(charIndex)
                                        }
                                    } else {
                                        // Phòng trường hợp người dùng chỉ nhập khoảng trắng
                                        userInputChars[charIndex] = TextFieldValue(" ", TextRange(1))
                                        updateAnswer()
                                    }
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
                                imeAction = if (isLastEditable) ImeAction.Done else ImeAction.Next
                            ),
                            keyboardActions = KeyboardActions(
                                onNext = {
                                    focusNext(charIndex)
                                },
                                onDone = {
                                    focusManager.clearFocus()
                                }
                            ),
                            modifier = Modifier
                                .size(38.dp)
                                .focusRequester(focusRequesters[charIndex])
                                .border(2.dp, borderColor, RoundedCornerShape(8.dp))
                                .background(Color.White, RoundedCornerShape(8.dp))
                                .wrapContentHeight(Alignment.CenterVertically)
                        )
                    }
                }
            }
            if (wordIndex < words.lastIndex) {
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }
}