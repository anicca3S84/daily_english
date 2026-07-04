package com.codework.dailyenglish.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Translate
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
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
    // 🔥 CHIÊU THỨ 1: KHÓA VUỐT VÀ KHÓA BACK PRESS
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { newValue ->
            newValue != SheetValue.Hidden
        }
    )

    val sheetBgColor = if (isCorrect) Color(0xFFD7FFB8) else Color(0xFFFFE0E0)
    val mainColor = if (isCorrect) Color(0xFF58CC02) else Color(0xFFFF4B4B)
    val shadowColor = if (isCorrect) Color(0xFF58A700) else Color(0xFFEA2B2B)
    val textDarkColor = Color(0xFF4B4B4B)

    val titleText = message ?: if (isCorrect) "Great!" else "Not quite!"

    val btnText = if (isCorrect) {
        if (isLastStep) "Finish" else "Continue"
    } else {
        "Got it"
    }

    ModalBottomSheet(
        onDismissRequest = {  },
        sheetState = sheetState,
        scrimColor = Color.Transparent,
        containerColor = Color.Transparent,
        dragHandle = null,
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Surface(
                color = sheetBgColor,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 24.dp)
                ) {
                    // ====== HEADER ROW ======
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color.White),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isCorrect) {
                                Text(text = "🐻", fontSize = 24.sp)
                            } else {
                                Icon(
                                    imageVector = Icons.Rounded.Close,
                                    contentDescription = "Wrong",
                                    tint = mainColor
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Text(
                            text = titleText,
                            color = mainColor,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }

                    // ====== TRƯỜNG HỢP ĐÚNG (HIỆN TỪ VỰNG NHƯ BẠN THIẾT KẾ) ======
                    if (isCorrect && word != null && meaning != null) {
                        Spacer(modifier = Modifier.height(20.dp))

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                                .padding(16.dp)
                        ) {
                            Text(
                                text = word,
                                color = shadowColor,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold,
                                modifier = Modifier.padding(bottom = 5.dp)
                            )
                            Text(
                                text = meaning,
                                color = textDarkColor,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(bottom = if (example != null) 12.dp else 0.dp)
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
                    // ====== TRƯỜNG HỢP SAI (HIỆN ĐÁP ÁN ĐÚNG) ======
                    else if (!isCorrect && correctAnswer != null) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Đáp án đúng:",
                            color = mainColor,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = correctAnswer,
                            color = shadowColor,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    } else {
                        Spacer(modifier = Modifier.height(32.dp))
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // ====== NÚT BẤM DÙNG MÀU ĐỘNG ======
                    DuolingoStyleButton(
                        text = btnText,
                        mainColor = mainColor,
                        shadowColor = shadowColor,
                        onClick = onNextClicked // Đã bỏ onDismissRequest vì ta không cho phép đóng bằng UI sheet
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
private fun DuolingoStyleButton(
    text: String,
    mainColor: Color,
    shadowColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = shadowColor,
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
    ) {
        Surface(
            color = mainColor,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 5.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = text,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }
        }
    }
}