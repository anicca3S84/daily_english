package com.codework.dailyenglish.presentation.screen.lessons.lesson.components.question

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Star
import com.codework.dailyenglish.presentation.screen.lessons.lesson.components.question.components.AudioWaveform
import com.codework.dailyenglish.presentation.screen.lessons.lesson.components.question.components.CircularAudioButton3D
import com.codework.dailyenglish.presentation.screen.lessons.lesson.components.question.components.PlayButton3D
import kotlin.math.roundToInt

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SentenceBuilderQuestion(
    prompt: String,
    words: List<String>,
    correctSentence: String,
    selectedIndices: List<Int> = emptyList(),
    isEvaluated: Boolean = false,
    isCorrect: Boolean? = null,
    onAnswerSelected: (List<Int>, String) -> Unit,
    onPlayAudio: (String?, String, Float) -> Unit,
    showSpeedControl: Boolean = true
) {
    var ttsSpeed by remember { mutableFloatStateOf(1.0f) }
    val selectedSet = remember(selectedIndices) { selectedIndices.toSet() }

    // Tách câu đúng thành danh sách các từ để tính toán chính xác số lượng ô cần hiển thị
    val correctWordsCount = remember(correctSentence) {
        if (correctSentence.isBlank()) 0
        else correctSentence.trim().split("\\s+".toRegex()).size
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        // CỤM 1: HEADER
        Text(
            text = "Put the words in order",
            fontSize = 22.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF1F2937),
            modifier = Modifier.padding(bottom = 20.dp)
        )

        // CỤM 2: KHU VỰC PHÁT ÂM THANH & CÂU HỎI
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        ) {
            // Lớp bóng đổ 3D mềm mại phía dưới khung chứa
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .offset(y = 4.dp)
                    .background(Color(0xFFF3F4F6), RoundedCornerShape(20.dp))
            )
            // Lớp thẻ chính màu trắng với viền tinh tế
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                color = Color.White,
                border = BorderStroke(2.dp, Color(0xFFEFF1F4))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(18.dp)
                ) {
                    // Nút Play 3D
                    PlayButton3D(onClick = { onPlayAudio(null, correctSentence, ttsSpeed) })

                    AudioWaveform(
                        modifier = Modifier
                            .weight(1f)
                            .height(36.dp),
                        color = Color(0xFF3B82F6) // Màu sóng xanh dương đồng bộ với nút
                    )
                }
            }
        }

        // CỤM 3: DROP ZONE (Khu vực chứa các từ đã chọn)
        val dropZoneActive = selectedIndices.isNotEmpty()
        val dropZoneBg by animateColorAsState(targetValue = when {
            isEvaluated && isCorrect == true -> Color(0xFFD7FFB8)
            isEvaluated && isCorrect == false -> Color(0xFFFFE0E0)
            dropZoneActive -> Color(0xFFEFF6FF)
            else -> Color(0xFFF9FAFB)
        }, label = "bg")

        val dropZoneBorder by animateColorAsState(targetValue = when {
            isEvaluated && isCorrect == true -> Color(0xFF58CC02)
            isEvaluated && isCorrect == false -> Color(0xFFFF4B4B)
            dropZoneActive -> Color(0xFF93C5FD)
            else -> Color(0xFFD1D5DB)
        }, label = "border")

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .dashedBorder(color = dropZoneBorder, width = 2.dp, radius = 16.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(dropZoneBg)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Your sentence",
                color = Color(0xFF3B82F6),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            FlowRow(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // 1. Hiển thị các từ đã chọn
                selectedIndices.forEach { originalIndex ->
                    WordButton3D(
                        text = words[originalIndex],
                        onClick = {
                            if (isEvaluated) return@WordButton3D
                            val newSelections = selectedIndices.toMutableList().apply { remove(originalIndex) }
                            onAnswerSelected(newSelections, newSelections.joinToString(" ") { words[it] })
                        }
                    )
                }

                // 2. Chỉ hiển thị ô trống nét đứt tương ứng với số từ còn thiếu của câu đúng
                val emptySlotsCount = maxOf(0, correctWordsCount - selectedIndices.size)
                repeat(emptySlotsCount) {
                    Box(
                        modifier = Modifier
                            .width(64.dp)
                            .height(52.dp)
                            .dashedBorder(color = Color(0xFFD1D5DB), width = 2.dp, radius = 12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "💡 Tap a word below to add it to the sentence",
                color = Color(0xFF3B82F6),
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
        }

        // CỤM 4: ĐƯỜNG CHIA WORD BANK
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFFE5E7EB), thickness = 1.dp)
            Text(
                text = "Word bank",
                modifier = Modifier.padding(horizontal = 16.dp),
                color = Color(0xFF9CA3AF),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
            HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFFE5E7EB), thickness = 1.dp)
        }

        // CỤM 5: WORD BANK (Khu vực các nút từ vựng lựa chọn)
        Box(modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)) {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                words.forEachIndexed { index, word ->
                    val isSelected = selectedSet.contains(index)
                    if (isSelected) {
                        Box(
                            modifier = Modifier
                                .background(Color(0xFFF3F4F6), RoundedCornerShape(12.dp))
                                .border(2.dp, Color(0xFFE5E7EB), RoundedCornerShape(12.dp))
                                .padding(horizontal = 16.dp, vertical = 10.dp)
                        ) {
                            Text(text = word, color = Color.Transparent, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        }
                    } else {
                        WordButton3D(
                            text = word,
                            onClick = {
                                if (isEvaluated) return@WordButton3D
                                onPlayAudio(null, word, 1.0f)
                                val newSelections = selectedIndices + index
                                onAnswerSelected(newSelections, newSelections.joinToString(" ") { words[it] })
                            }
                        )
                    }
                }
            }
        }

        // CỤM 6: ĐIỀU CHỈNH TỐC ĐỘ
        if (showSpeedControl) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFE5E7EB))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Speed,
                            contentDescription = "Speed",
                            tint = Color(0xFF4B5563),
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "Speed",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1F2937)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    val sliderPosition = when (ttsSpeed) {
                        0.5f -> 0f
                        1.0f -> 1f
                        else -> 2f
                    }

                    Slider(
                        value = sliderPosition,
                        onValueChange = { newValue ->
                            val targetSpeed = when (newValue.roundToInt()) {
                                0 -> 0.5f
                                1 -> 1.0f
                                else -> 1.5f
                            }
                            if (targetSpeed != ttsSpeed) {
                                ttsSpeed = targetSpeed
                                onPlayAudio(null, correctSentence, targetSpeed)
                            }
                        },
                        valueRange = 0f..2f,
                        steps = 1,
                        colors = SliderDefaults.colors(
                            activeTrackColor = Color(0xFF3B82F6),
                            inactiveTrackColor = Color(0xFFE5E7EB),
                            thumbColor = Color(0xFF3B82F6)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("0.5x", fontSize = 12.sp, color = Color(0xFF9CA3AF), fontWeight = FontWeight.Medium)
                        Text("1.0x", fontSize = 12.sp, color = Color(0xFF3B82F6), fontWeight = FontWeight.Bold)
                        Text("1.5x", fontSize = 12.sp, color = Color(0xFF9CA3AF), fontWeight = FontWeight.Medium)
                    }
                }
            }
        }
    }
}


fun Modifier.dashedBorder(color: Color, width: Dp, radius: Dp) = this.composed {
    val density = LocalDensity.current
    val strokeWidthPx = remember(density, width) { with(density) { width.toPx() } }
    val cornerRadiusPx = remember(density, radius) { with(density) { radius.toPx() } }
    val pathEffect = remember { PathEffect.dashPathEffect(floatArrayOf(20f, 20f), 0f) }

    this.drawBehind {
        drawRoundRect(
            color = color,
            style = Stroke(
                width = strokeWidthPx,
                pathEffect = pathEffect
            ),
            cornerRadius = CornerRadius(cornerRadiusPx)
        )
    }
}

@Composable
fun SmallAudioButton3D(onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val yOffset = if (isPressed) 2.dp else 0.dp
    val bottomPadding = if (isPressed) 0.dp else 2.dp

    Box(
        modifier = Modifier
            .size(44.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .offset(y = 2.dp)
                .background(Color(0xFF84D8FF), RoundedCornerShape(12.dp))
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .offset(y = yOffset)
                .padding(bottom = bottomPadding)
                .background(Color(0xFFDDF4FF), RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.VolumeUp,
                contentDescription = "Play Audio",
                tint = Color(0xFF1CB0F6),
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

@Composable
fun WordButton3D(
    text: String,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val yOffset = if (isPressed) 4.dp else 0.dp
    val bottomPadding = if (isPressed) 0.dp else 4.dp

    Box(
        modifier = Modifier
            .height(IntrinsicSize.Min)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(y = 4.dp)
                .background(Color(0xFFE5E7EB), RoundedCornerShape(12.dp))
        )
        Box(
            modifier = Modifier
                .offset(y = yOffset)
                .padding(bottom = bottomPadding)
                .background(Color.White, RoundedCornerShape(12.dp))
                .border(2.dp, Color(0xFFE5E7EB), RoundedCornerShape(12.dp))
                .padding(horizontal = 16.dp, vertical = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                color = Color(0xFF1F2937),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}