package com.codework.dailyenglish.presentation.screen.lessons.lesson.components.question

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.rounded.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SentenceBuilderQuestion(
    prompt: String,
    words: List<String>,
    correctSentence: String,
    selectedIndices: List<Int> = emptyList(),
    isEvaluated: Boolean = false,
    isCorrect: Boolean? = null,
    onAnswerSelected: (List<Int>, String) -> Unit
) {
    val context = LocalContext.current
    val isPreview = LocalInspectionMode.current
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }

    if (!isPreview) {
        DisposableEffect(context) {
            val textToSpeech = TextToSpeech(context) { status ->
                if (status == TextToSpeech.SUCCESS) {
                    tts?.language = Locale.US
                }
            }
            tts = textToSpeech
            onDispose { textToSpeech.shutdown() }
        }
    }

    var ttsSpeed by remember { mutableFloatStateOf(1.0f) }
    var isSpeedDropdownExpanded by remember { mutableStateOf(false) }
    val speedOptions = listOf(1.0f to "1.0x", 0.75f to "0.75x", 0.5f to "0.5x")

    val speakText = { text: String, speed: Float ->
        tts?.setSpeechRate(speed)
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Put the words in order",
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF1F2937)
            )

            Box {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .border(2.dp, Color(0xFFE5E7EB), RoundedCornerShape(12.dp))
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { isSpeedDropdownExpanded = true }
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "${speedOptions.find { it.first == ttsSpeed }?.second}",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4B5563),
                        fontSize = 14.sp
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = null,
                        tint = Color(0xFF9CA3AF),
                        modifier = Modifier.padding(start = 4.dp).size(20.dp)
                    )
                }

                DropdownMenu(
                    expanded = isSpeedDropdownExpanded,
                    onDismissRequest = { isSpeedDropdownExpanded = false },
                    modifier = Modifier.background(Color.White)
                ) {
                    speedOptions.forEach { (value, label) ->
                        DropdownMenuItem(
                            text = { Text(text = "Tốc độ: $label", fontWeight = FontWeight.Bold) },
                            onClick = {
                                ttsSpeed = value
                                isSpeedDropdownExpanded = false
                                speakText(correctSentence, value)
                            }
                        )
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Mascot Gấu
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFF3F4F6))
                    .border(2.dp, Color(0xFFE5E5E5), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "🐻", fontSize = 36.sp)
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .padding(top = 4.dp)
                        .background(Color(0xFFE5E5E5), RoundedCornerShape(16.dp))
                )

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White,
                    border = BorderStroke(2.dp, Color(0xFFE5E5E5))
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        SmallAudioButton3D(onClick = { speakText(correctSentence, ttsSpeed) })

                        Text(
                            text = prompt,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4B4B4B),
                            lineHeight = 26.sp
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .offset(x = (-6).dp, y = (-2).dp)
                        .size(14.dp)
                        .rotate(45f)
                        .drawBehind {
                            drawRect(Color.White)
                            val stroke = 2.dp.toPx()
                            drawLine(
                                color = Color(0xFFE5E5E5),
                                start = Offset(0f, 0f),
                                end = Offset(0f, size.height),
                                strokeWidth = stroke
                            )
                            drawLine(
                                color = Color(0xFFE5E5E5),
                                start = Offset(0f, size.height),
                                end = Offset(size.width, size.height),
                                strokeWidth = stroke
                            )
                        }
                )
            }
        }


        val isDropZoneActive = selectedIndices.isNotEmpty()
        val dropZoneBg = when {
            isEvaluated && isCorrect == true -> Color(0xFFD7FFB8) // Xanh thành công
            isEvaluated && isCorrect == false -> Color(0xFFFFE0E0) // Đỏ lỗi
            isDropZoneActive -> Color(0xFFEFF6FF)
            else -> Color(0xFFF9FAFB)
        }
        val dropZoneBorder = when {
            isEvaluated && isCorrect == true -> Color(0xFF58CC02)
            isEvaluated && isCorrect == false -> Color(0xFFFF4B4B)
            isDropZoneActive -> Color(0xFF93C5FD)
            else -> Color(0xFFD1D5DB)
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 140.dp, max = 220.dp)
                .dashedBorder(color = dropZoneBorder, width = 2.dp, radius = 16.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(dropZoneBg)
                .padding(16.dp),
            contentAlignment = Alignment.TopStart
        ) {
            if (!isDropZoneActive) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Choose the words below", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF9CA3AF), textAlign = TextAlign.Center)
                }
            }

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                selectedIndices.forEach { originalIndex ->
                    WordButton3D(
                        text = words[originalIndex],
                        onClick = {
                            // 🔥 KHÓA: Nếu đã chấm điểm thì không cho rút từ ra nữa
                            if (isEvaluated) return@WordButton3D

                            val newSelections = selectedIndices.toMutableList().apply { remove(originalIndex) }
                            val currentSentence = newSelections.joinToString(" ") { words[it] }
                            onAnswerSelected(newSelections, currentSentence)
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        HorizontalDivider(color = Color(0xFFF3F4F6), thickness = 2.dp)

        Spacer(modifier = Modifier.height(24.dp))

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            words.forEachIndexed { index, word ->
                val isSelected = selectedIndices.contains(index)

                if (isSelected) {
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFE5E7EB), RoundedCornerShape(12.dp))
                            .padding(horizontal = 16.dp, vertical = 10.dp)
                    ) {
                        Text(text = word, color = Color(0xFFE5E7EB), fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                } else {
                    WordButton3D(
                        text = word,
                        onClick = {
                            if (isEvaluated) return@WordButton3D
                            speakText(word, 1.0f)
                            val newSelections = selectedIndices + index
                            val currentSentence = newSelections.joinToString(" ") { words[it] }
                            onAnswerSelected(newSelections, currentSentence)
                        }
                    )
                }
            }
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
        // Lớp bóng đổ (Shadow)
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(y = 4.dp)
                .background(Color(0xFFE5E7EB), RoundedCornerShape(12.dp))
        )
        // Lớp nổi phía trên
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

fun Modifier.dashedBorder(color: Color, width: Dp, radius: Dp) = this.drawBehind {
    drawRoundRect(
        color = color,
        style = Stroke(
            width = width.toPx(),
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 20f), 0f)
        ),
        cornerRadius = CornerRadius(radius.toPx())
    )
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