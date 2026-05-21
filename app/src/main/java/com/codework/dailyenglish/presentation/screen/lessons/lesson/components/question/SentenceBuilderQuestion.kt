package com.codework.dailyenglish.presentation.screen.lessons.lesson.components.question

import android.speech.tts.TextToSpeech
import androidx.compose.animation.animateColorAsState
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

    // ROOT COLUMN: Chiếm trọn chiều cao màn hình
    Column(
        modifier = Modifier
            .fillMaxSize() // Quan trọng: Phải fillMaxSize để weight hoạt động
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        // CỤM 1: HEADER (Cố định ở trên)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Put the words in order",
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF1F2937)
            )

            // Speed Selector
            Box {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .border(2.dp, Color(0xFFE5E7EB), RoundedCornerShape(12.dp))
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { isSpeedDropdownExpanded = true }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "${speedOptions.find { it.first == ttsSpeed }?.second}",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4B5563),
                        fontSize = 13.sp
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = null,
                        tint = Color(0xFF9CA3AF),
                        modifier = Modifier.size(18.dp)
                    )
                }

                DropdownMenu(
                    expanded = isSpeedDropdownExpanded,
                    onDismissRequest = { isSpeedDropdownExpanded = false }
                ) {
                    speedOptions.forEach { (value, label) ->
                        DropdownMenuItem(
                            text = { Text(text = "Tốc độ: $label") },
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
                .padding(bottom = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFF3F4F6))
                    .border(2.dp, Color(0xFFE5E5E5), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "🐻", fontSize = 32.sp)
            }

            Box(modifier = Modifier.weight(1f).padding(start = 12.dp)) {
                Box(modifier = Modifier.matchParentSize().padding(top = 4.dp).background(Color(0xFFE5E5E5), RoundedCornerShape(16.dp)))
                Surface(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White,
                    border = BorderStroke(2.dp, Color(0xFFE5E5E5))
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        SmallAudioButton3D(onClick = { speakText(correctSentence, ttsSpeed) })
                        Text(text = prompt, fontSize = 17.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4B4B4B), lineHeight = 24.sp)
                    }
                }
            }
        }

        // CỤM 3: DROP ZONE (LÒ XO CHÍNH - weight(1f))
        // Nó sẽ tự giãn ra để chiếm toàn bộ phần giữa, không bao giờ nhảy size
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

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f) // <--- ĐÂY LÀ FIX: Nó sẽ không bao giờ ngắn lại hay dài ra bất ngờ
                .dashedBorder(color = dropZoneBorder, width = 2.dp, radius = 16.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(dropZoneBg)
                .padding(16.dp)
        ) {
            if (!dropZoneActive) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Choose the words below", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = Color(0xFF9CA3AF))
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
                            if (isEvaluated) return@WordButton3D
                            val newSelections = selectedIndices.toMutableList().apply { remove(originalIndex) }
                            onAnswerSelected(newSelections, newSelections.joinToString(" ") { words[it] })
                        }
                    )
                }
            }
        }

        // CỤM 4: WORD BANK (Cố định ở đáy)
        Spacer(modifier = Modifier.height(24.dp))
        HorizontalDivider(color = Color(0xFFF3F4F6), thickness = 2.dp)
        Spacer(modifier = Modifier.height(20.dp))

        // Dùng Box cố định chiều cao cho Word Bank để không bị nhảy khi click
        Box(modifier = Modifier.fillMaxWidth().heightIn(min = 120.dp)) {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                words.forEachIndexed { index, word ->
                    val isSelected = selectedIndices.contains(index)
                    if (isSelected) {
                        // Placeholder khi từ đã được chọn
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
                                onAnswerSelected(newSelections, newSelections.joinToString(" ") { words[it] })
                            }
                        )
                    }
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