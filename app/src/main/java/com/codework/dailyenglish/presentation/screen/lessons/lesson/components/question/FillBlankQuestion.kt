// FILE: app/src/main/java/com/codework.daily_english/presentation/screen/lesson/components/FillBlankQuestion.kt

package com.codework.dailyenglish.presentation.screen.lessons.lesson.components.question

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Màn hình điền vào chỗ trống với các ô nhập liệu 3D.
 * Tuân thủ MVI: Dữ liệu luôn lấy từ [initialAnswer] của ViewModel.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FillBlankQuestion(
    prompt: String,
    sentenceWithBlank: String,
    hintMask: List<String?>,
    initialAnswer: String? = null,
    onAnswerSelected: (String) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    val totalBoxes = hintMask.size

    // Nguồn chân lý duy nhất (Single Source of Truth)
    val currentText = initialAnswer ?: ""

    // Trạng thái để kiểm soát con trỏ nhấp nháy chỉ khi bàn phím đang mở
    var isKeyboardFocused by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 30.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { focusManager.clearFocus() }
    ) {
        Text(
            text = "Điền vào chỗ trống",
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF1F2937), // Màu chữ chính
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // 1. KHỐI MASCOT & BONG BÓNG CHAT
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 40.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Mascot (Ví dụ: Gấu trúc/Gấu nâu)
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFF3F4F6))
                    .border(2.dp, Color(0xFFE5E7EB), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "🐻", fontSize = 36.sp)
            }

            // Speech Bubble
            Surface(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(
                    topStart = 4.dp,
                    topEnd = 24.dp,
                    bottomEnd = 24.dp,
                    bottomStart = 24.dp
                ),
                color = Color.White,
                border = BorderStroke(2.dp, Color(0xFFE5E7EB))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Nút Loa
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFE0F2FE),
                        modifier = Modifier.size(44.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Rounded.VolumeUp,
                                contentDescription = "Nghe",
                                tint = Color(0xFF3B82F6),
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    // Nội dung văn bản
                    Text(
                        text = prompt,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F2937),
                        lineHeight = 26.sp
                    )

                    if (sentenceWithBlank.isNotBlank()) {
                        Text(
                            text = sentenceWithBlank,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1F2937),
                            lineHeight = 26.sp
                        )
                    }
                }
            }
        }

        // 2. VÙNG Ô TRỐNG (Blank Boxes) & INPUT ẨN
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    focusRequester.requestFocus()
                },
            contentAlignment = Alignment.Center
        ) {
            // TextField vô hình để hứng sự kiện bàn phím
            BasicTextField(
                value = currentText,
                onValueChange = { newValue ->
                    // Lọc chỉ cho phép chữ và số, giới hạn độ dài
                    val filtered = newValue.replace(Regex("[^a-zA-Z0-9]"), "")
                    if (filtered.length <= totalBoxes) {
                        onAnswerSelected(filtered)
                    }
                },
                modifier = Modifier
                    .focusRequester(focusRequester)
                    .onFocusChanged { isKeyboardFocused = it.isFocused }
                    .fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                ),
                decorationBox = { innerTextField ->
                    Box(contentAlignment = Alignment.Center) {
                        // Ẩn nội dung TextField thực tế nhưng vẫn giữ nó hoạt động
                        Box(modifier = Modifier.alpha(0f).size(0.dp)) {
                            innerTextField()
                        }

                        // Hiển thị các ô 3D trực quan
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            for (index in 0 until totalBoxes) {
                                val isTyped = index < currentText.length
                                // Chỉ hiện cursor ở ô trống tiếp theo khi đang tập trung gõ
                                val isBoxFocused = isKeyboardFocused && index == currentText.length

                                val charToShow = if (isTyped) {
                                    currentText[index].toString().uppercase()
                                } else {
                                    hintMask.getOrNull(index)?.uppercase() ?: ""
                                }

                                BlankBox3D(
                                    char = charToShow,
                                    isFocused = isBoxFocused,
                                    isFilled = isTyped,
                                    isHint = !isTyped && charToShow.isNotEmpty()
                                )
                            }
                        }
                    }
                }
            )
        }

        Text(
            text = "Chạm vào ô để gõ bàn phím",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF3B82F6).copy(alpha = 0.8f),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun BlankBox3D(
    char: String,
    isFocused: Boolean,
    isFilled: Boolean,
    isHint: Boolean
) {
    // Định nghĩa palette màu cục bộ
    val colorBlueMain = Color(0xFF3B82F6)
    val colorBlueBg = Color(0xFFE0F2FE)
    val colorGrayBg = Color(0xFFF3F4F6)
    val colorGrayBorder = Color(0xFFE5E7EB)
    val colorText = Color(0xFF1F2937)

    val bgColor = when {
        isFocused -> colorBlueBg
        isFilled -> Color.White
        else -> colorGrayBg
    }

    val borderColor = when {
        isFocused -> colorBlueMain
        else -> colorGrayBorder
    }

    val textColor = when {
        isHint -> Color.Gray
        isFocused -> colorBlueMain
        else -> colorText
    }

    // Hiệu ứng nhấp nháy cho con trỏ
    val infiniteTransition = rememberInfiniteTransition(label = "cursor")
    val cursorAlpha by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 1000
                0f at 0
                1f at 500
                0f at 1000
            }
        ), label = "alpha"
    )

    Box(
        modifier = Modifier.size(width = 52.dp, height = 64.dp)
    ) {
        // Lớp Đáy 3D (Viền dưới tạo độ sâu)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .align(Alignment.BottomCenter)
                .background(borderColor, RoundedCornerShape(12.dp))
        )

        // Lớp Bề mặt (Phần nút bấm nổi lên)
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .align(Alignment.TopCenter),
            shape = RoundedCornerShape(12.dp),
            color = bgColor,
            border = BorderStroke(2.dp, borderColor)
        ) {
            Box(contentAlignment = Alignment.Center) {
                if (isFocused && char.isEmpty()) {
                    // Thanh Cursor nhấp nháy
                    Box(
                        modifier = Modifier
                            .width(2.dp)
                            .height(24.dp)
                            .alpha(cursorAlpha)
                            .background(colorBlueMain)
                    )
                } else {
                    Text(
                        text = char,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = textColor
                    )
                }
            }
        }
    }
}