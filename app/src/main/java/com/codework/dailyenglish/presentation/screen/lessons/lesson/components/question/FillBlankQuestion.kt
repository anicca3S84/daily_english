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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// === BẢNG MÀU UI 3D ===
private val ColorText = Color(0xFF4B4B4B)
private val ColorGrayBorder = Color(0xFFE5E5E5)
private val ColorGrayBg = Color(0xFFF3F4F6)

private val ColorBlueMain = Color(0xFF1CB0F6)
private val ColorBlueBg = Color(0xFFDDF4FF)

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

    // Tổng số ô vuông
    val totalBoxes = hintMask.size

    var userInput by remember(hintMask) {
        mutableStateOf(initialAnswer?.take(totalBoxes) ?: "")
    }

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
            color = ColorText,
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
            // Mascot
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(ColorGrayBg)
                    .border(2.dp, ColorGrayBorder, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "🐻", fontSize = 36.sp)
            }

            // Speech Bubble
            Surface(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(
                    topStart = 4.dp, // Góc nhọn chỉ về phía con gấu
                    topEnd = 24.dp,
                    bottomEnd = 24.dp,
                    bottomStart = 24.dp
                ),
                color = Color.White,
                border = BorderStroke(2.dp, ColorGrayBorder)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Nút Loa (Icon)
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = ColorBlueBg,
                        modifier = Modifier.size(44.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Rounded.VolumeUp,
                                contentDescription = "Nghe",
                                tint = ColorBlueMain,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    // Nội dung hội thoại
                    Text(
                        text = prompt,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = ColorText,
                        lineHeight = 26.sp
                    )

                    if (sentenceWithBlank.isNotBlank()) {
                        Text(
                            text = sentenceWithBlank,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = ColorText,
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
            // Trường nhập liệu vô hình (Bắt bàn phím hệ thống)
            BasicTextField(
                value = userInput,
                onValueChange = { newValue ->
                    // Lọc chỉ lấy chữ cái và số
                    val filtered = newValue.replace(Regex("[^a-zA-Z0-9]"), "")
                    if (filtered.length <= totalBoxes) {
                        userInput = filtered
                        onAnswerSelected(filtered)
                    }
                },
                modifier = Modifier
                    .focusRequester(focusRequester)
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
                        // Ẩn view gõ thực tế
                        Box(modifier = Modifier.alpha(0f).size(0.dp)) {
                            innerTextField()
                        }

                        // Vẽ các ô 3D trực quan
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            for (index in 0 until totalBoxes) {
                                val isTyped = index < userInput.length
                                val isFocused = index == userInput.length

                                val charToShow = if (isTyped) {
                                    userInput[index].toString().uppercase()
                                } else {
                                    hintMask[index]?.uppercase() ?: ""
                                }

                                BlankBox3D(
                                    char = charToShow,
                                    isFocused = isFocused,
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
            color = ColorBlueMain.copy(alpha = 0.8f),
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
    // Logic đổi màu dựa trên trạng thái (Tập trung / Đã điền / Trống)
    val bgColor = when {
        isFocused -> ColorBlueBg
        isFilled -> Color.White
        else -> ColorGrayBg
    }

    val borderColor = when {
        isFocused -> ColorBlueMain
        else -> ColorGrayBorder
    }

    val textColor = when {
        isHint -> Color.Gray
        isFocused -> ColorBlueMain
        else -> ColorText
    }

    // Animation cho con trỏ nhấp nháy
    val infiniteTransition = rememberInfiniteTransition()
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
        ), label = "cursorBlink"
    )

    Box(
        modifier = Modifier.size(width = 52.dp, height = 64.dp)
    ) {
        // Lớp Đáy 3D (Đổ bóng / Viền đáy)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .align(Alignment.BottomCenter)
                .background(borderColor, RoundedCornerShape(12.dp))
        )

        // Lớp Nổi trên cùng
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
                    // Con trỏ nhấp nháy
                    Box(
                        modifier = Modifier
                            .width(2.dp)
                            .height(24.dp)
                            .alpha(cursorAlpha)
                            .background(ColorBlueMain)
                    )
                } else {
                    // Ký tự
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

@Preview(showBackground = true, name = "Trạng thái ban đầu (Chưa gõ)")
@Composable
fun PreviewFillBlankQuestion_Empty() {
    MaterialTheme {
        Box(
            modifier = Modifier
                .background(Color.White)
                .fillMaxSize()
        ) {
            FillBlankQuestion(
                prompt = "Mom: \"It's late. Go to sleep.\"",
                sentenceWithBlank = "Tommy: \"Good ________, Mom.\"",
                hintMask = listOf("n", null, null, null, null),
                initialAnswer = null,
                onAnswerSelected = {}
            )
        }
    }
}

@Preview(showBackground = true, name = "Đang gõ dở")
@Composable
fun PreviewFillBlankQuestion_Typing() {
    MaterialTheme {
        Box(
            modifier = Modifier
                .background(Color.White)
                .fillMaxSize()
        ) {
            FillBlankQuestion(
                prompt = "Mom: \"It's late. Go to sleep.\"",
                sentenceWithBlank = "Tommy: \"Good ________, Mom.\"",
                hintMask = listOf("n", null, null, null, null),
                initialAnswer = "nig", // User đã gõ 'nig'
                onAnswerSelected = {}
            )
        }
    }
}