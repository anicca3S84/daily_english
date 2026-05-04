package com.codework.dailyenglish.presentation.screen.lessons.lesson.components.question

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material.icons.rounded.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codework.dailyenglish.domain.lesson.model.QuestionContent
import com.codework.dailyenglish.domain.lesson.model.StepData

// === BẢNG MÀU UI MỚI (Dựa theo thiết kế HTML) ===
private val ColorText = Color(0xFF4B4B4B)
private val ColorGrayBorder = Color(0xFFE5E5E5)
private val ColorGrayBg = Color(0xFFF3F4F6)

private val ColorBlueMain = Color(0xFF1CB0F6)
private val ColorBlueBg = Color(0xFFDDF4FF)
private val ColorBlueBorder = Color(0xFF84D8FF)

private val ColorGreenMain = Color(0xFF58CC02)
private val ColorGreenBg = Color(0xFFD7FFB8)

private val ColorRedMain = Color(0xFFEA2B2B)
private val ColorRedBg = Color(0xFFFFDFE0)

@Composable
fun MultipleChoiceQuestion(
    data: StepData.Question,
    onOptionSelected: (String) -> Unit,
    onPlayAudio: (String?, String, Float) -> Unit,
    modifier: Modifier = Modifier
) {
    val content = data.content as? QuestionContent.MultipleChoice ?: return
    val isEvaluated = data.isAnswerEvaluated

    val hasAudio = content.audioUrl != null
    val textToRead = content.audioUrl ?: content.prompt

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 30.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Chọn nghĩa đúng",
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold,
            color = ColorText,
            modifier = Modifier.padding(bottom = 30.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 40.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier.size(80.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "🐻", fontSize = 64.sp)
            }

            Surface(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(
                    topStart = 24.dp,
                    topEnd = 24.dp,
                    bottomEnd = 24.dp,
                    bottomStart = 0.dp
                ),
                color = Color.White,
                border = BorderStroke(2.dp, ColorGrayBorder),
                shadowElevation = 0.dp
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (hasAudio) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = ColorBlueBg,
                            modifier = Modifier
                                .size(48.dp)
                                .clickable { onPlayAudio(null, textToRead, 1f) }
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Rounded.VolumeUp,
                                    contentDescription = "Nghe",
                                    tint = ColorBlueMain,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        }
                    }

                    Text(
                        text = content.prompt,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = ColorText,
                        lineHeight = 28.sp
                    )
                }
            }
        }

        if (content.translation != null && hasAudio) {
            var isTranscriptExpanded by remember { mutableStateOf(false) }

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { isTranscriptExpanded = !isTranscriptExpanded },
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, ColorGrayBorder),
                color = Color.White
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Xem bản dịch & văn bản",
                            color = Color.Gray,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Icon(
                            imageVector = if (isTranscriptExpanded) Icons.Rounded.KeyboardArrowUp else Icons.Rounded.KeyboardArrowDown,
                            contentDescription = null,
                            tint = Color.Gray
                        )
                    }

                    AnimatedVisibility(visible = isTranscriptExpanded) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(ColorGrayBg)
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "\"${content.audioUrl}\"",
                                color = ColorText,
                                fontSize = 16.sp,
                                fontStyle = FontStyle.Italic,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            Text(
                                text = content.translation,
                                color = Color.Gray,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
        }

        // 3. KHỐI ĐÁP ÁN (Grid 2 cột)
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            val chunkedOptions = content.options.chunked(2)
            var optionNumber = 1

            chunkedOptions.forEach { rowOptions ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    rowOptions.forEach { option ->
                        QuizOption3D(
                            modifier = Modifier.weight(1f),
                            text = option.text,
                            number = optionNumber++,
                            optionId = option.id,
                            selectedOptionId = data.selectedOptionId,
                            isCorrect = option.isCorrect,
                            isEvaluated = isEvaluated,
                            onClick = onOptionSelected
                        )
                    }
                    if (rowOptions.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
fun QuizOption3D(
    text: String,
    number: Int,
    optionId: String,
    selectedOptionId: String?,
    isCorrect: Boolean,
    isEvaluated: Boolean,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val isSelected = selectedOptionId == optionId

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val isSinking = isPressed || (isEvaluated && isSelected)

    val topOffset by animateDpAsState(
        targetValue = if (isSinking) 4.dp else 0.dp,
        animationSpec = tween(100), label = "3D_Offset"
    )

    val bgColor by animateColorAsState(
        targetValue = when {
            isEvaluated && isCorrect -> ColorGreenBg
            isEvaluated && isSelected && !isCorrect -> ColorRedBg
            !isEvaluated && isSelected -> ColorBlueBg
            else -> Color.White
        }, label = "BgColor"
    )

    val borderColor by animateColorAsState(
        targetValue = when {
            isEvaluated && isCorrect -> ColorGreenMain
            isEvaluated && isSelected && !isCorrect -> ColorRedMain
            !isEvaluated && isSelected -> ColorBlueBorder
            else -> ColorGrayBorder
        }, label = "BorderColor"
    )

    val shadowColor by animateColorAsState(
        targetValue = when {
            isEvaluated && isCorrect -> ColorGreenMain
            isEvaluated && isSelected && !isCorrect -> ColorRedMain
            !isEvaluated && isSelected -> ColorBlueBorder
            else -> ColorGrayBorder
        }, label = "ShadowColor"
    )

    val textColor by animateColorAsState(
        targetValue = when {
            isEvaluated && isCorrect -> ColorGreenMain
            isEvaluated && isSelected && !isCorrect -> ColorRedMain
            !isEvaluated && isSelected -> ColorBlueMain
            else -> ColorText
        }, label = "TextColor"
    )

    Box(
        modifier = modifier.height(110.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(106.dp)
                .align(Alignment.BottomCenter)
                .background(shadowColor, RoundedCornerShape(16.dp))
        )

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(106.dp)
                .offset(y = topOffset)
                .clip(RoundedCornerShape(16.dp))
                .clickable(
                    interactionSource = interactionSource,
                    indication = null, // Bỏ hiệu ứng Ripple mặc định để tôn lên hiệu ứng 3D lún
                    enabled = !isEvaluated
                ) { onClick(optionId) },
            shape = RoundedCornerShape(16.dp),
            color = bgColor,
            border = BorderStroke(2.dp, borderColor)
        ) {
            Box(
                modifier = Modifier.padding(12.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = if (!isEvaluated && isSelected) Color.White else ColorGrayBg,
                    border = BorderStroke(2.dp, if (!isEvaluated && isSelected) ColorBlueBorder else ColorGrayBorder),
                    modifier = Modifier.size(24.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = number.toString(),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (!isEvaluated && isSelected) ColorBlueMain else Color.Gray
                        )
                    }
                }

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = text,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor,
                        textAlign = TextAlign.Center,
                        maxLines = 2
                    )
                }
            }
        }
    }
}