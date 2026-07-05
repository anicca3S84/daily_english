package com.codework.dailyenglish.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Translate
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnswerBottomSheet(
    isCorrect: Boolean,
    message: String? = null,
    correctAnswer: String? = null,
    word: String? = null,
    meaning: String? = null,
    isLastStep: Boolean,
    example: String? = null,
    exampleTranslation: String? = null,
    onNextClicked: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { newValue ->
            newValue != SheetValue.Hidden
        }
    )

    val sheetBgColor = if (isCorrect) Color(0xFFF1FAF3) else Color(0xFFFEF8ED)
    val mainColor = if (isCorrect) Color(0xFF42B059) else Color(0xFFF08F00)
    val textDarkColor = Color(0xFF3F3F3F)

    val titleText = if (isCorrect) "Correct" else "Incorrect"
    val descText = message ?: if (isCorrect) "Great job!" else null

    val btnText = if (isCorrect) {
        if (isLastStep) "Finish" else "Continue"
    } else {
        "Continue"
    }

    ModalBottomSheet(
        onDismissRequest = {  },
        sheetState = sheetState,
        scrimColor = Color.Transparent,
        containerColor = sheetBgColor,
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
        dragHandle = null,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                // Giảm bớt padding phía trên và dưới để giao diện gọn gàng hơn
                .padding(start = 24.dp, end = 24.dp, top = 20.dp, bottom = 12.dp)
        ) {
            // ====== TIÊU ĐỀ ======
            Text(
                text = titleText,
                color = mainColor,
                fontSize = 20.sp, // Điều chỉnh nhỏ lại một chút để cân đối
                fontWeight = FontWeight.ExtraBold
            )

            // ====== MÔ TẢ (Chỉ hiển thị khi trả lời đúng) ======
            if (descText != null) {
                Spacer(modifier = Modifier.height(4.dp)) // Giảm khoảng cách từ 8.dp xuống 4.dp
                Text(
                    text = descText,
                    color = textDarkColor,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            // ====== TRƯỜNG HỢP ĐÚNG ======
            if (isCorrect && word != null && meaning != null) {
                Spacer(modifier = Modifier.height(12.dp)) // Giảm khoảng cách từ 20.dp xuống 12.dp

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White.copy(alpha = 0.6f), RoundedCornerShape(16.dp))
                        .padding(12.dp) // Tiết kiệm không gian đệm trong hộp thoại
                ) {
                    Text(
                        text = word,
                        color = mainColor,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Text(
                        text = meaning,
                        color = textDarkColor,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(bottom = if (example != null) 8.dp else 0.dp)
                    )

                    if (example != null) {
                        var showTranslation by remember { mutableStateOf(false) }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = example,
                                color = textDarkColor,
                                fontSize = 15.sp,
                                fontStyle = FontStyle.Italic
                            )

                            if (exampleTranslation != null) {
                                Icon(
                                    imageVector = Icons.Rounded.Translate,
                                    contentDescription = "Translate",
                                    tint = if (showTranslation) Color.Blue else Color.Gray,
                                    modifier = Modifier
                                        .padding(start = 6.dp)
                                        .size(18.dp)
                                        .clickable { showTranslation = !showTranslation }
                                )
                            }
                        }

                        if (exampleTranslation != null) {
                            AnimatedVisibility(
                                visible = showTranslation,
                                enter = expandVertically(animationSpec = tween(300)) + fadeIn()
                            ) {
                                Text(
                                    text = exampleTranslation,
                                    color = Color.Gray,
                                    fontSize = 14.sp,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
            // ====== TRƯỜNG HỢP SAI (ĐÃ GỘP DÒNG) ======
            else if (!isCorrect && correctAnswer != null) {
                // Gộp nhãn hiển thị và giá trị đáp án trên cùng 1 dòng để tránh xuống dòng trống trải
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("Correct answer: ")
                        }
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Medium)) {
                            append(correctAnswer)
                        }
                    },
                    color = mainColor,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            } else {
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Giảm khoảng cách đệm trước khi tới nút bấm chính
            Spacer(modifier = Modifier.height(16.dp))

            // ====== NÚT BẤM DẠNG PHẲNG ======
            FlatStyleButton(
                text = btnText,
                backgroundColor = mainColor,
                onClick = onNextClicked
            )
            Spacer(modifier = Modifier.height(8.dp)) // Thu nhỏ khoảng cách dưới cùng
        }
    }
}

@Composable
private fun FlatStyleButton(
    text: String,
    backgroundColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(backgroundColor)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}