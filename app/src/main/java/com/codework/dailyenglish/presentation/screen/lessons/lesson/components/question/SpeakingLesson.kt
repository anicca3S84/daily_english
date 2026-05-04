package com.codework.dailyenglish.presentation.screen.lessons.lesson.components.question

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material.icons.rounded.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun SpeakingQuestion(
    prompt: String,
    targetText: String,
    phonetic: String,
    translation: String,
    initialAnswer: String? = null,
    onAnswerSelected: (String) -> Unit,
    onPlayAudio: () -> Unit
) {
    var isRecording by remember { mutableStateOf(false) }
    var spokenText by remember(initialAnswer) { mutableStateOf(initialAnswer) }

    LaunchedEffect(isRecording) {
        if (isRecording) {
            spokenText = null
            delay(3000)
            isRecording = false
            spokenText = targetText
            onAnswerSelected(targetText)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 1. Tiêu đề
        Text(
            text = prompt,
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF1F2937),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp),
            textAlign = TextAlign.Start
        )

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.TopCenter
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 48.dp)
                    .background(Color.White, RoundedCornerShape(24.dp))
                    .border(2.dp, Color(0xFFE5E7EB), RoundedCornerShape(24.dp))
                    .padding(top = 40.dp, bottom = 24.dp, start = 24.dp, end = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(y = (-24).dp, x = 8.dp)
                ) {
                    SmallAudioButton(onClick = onPlayAudio)
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = targetText,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF1F2937),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Text(
                        text = phonetic,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF3B82F6),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    Box(
                        modifier = Modifier
                            .width(64.dp)
                            .height(4.dp)
                            .background(Color(0xFFE5E7EB), RoundedCornerShape(2.dp))
                            .padding(bottom = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = translation,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF6B7280),
                        textAlign = TextAlign.Center
                    )
                }
            }

            Box(
                modifier = Modifier
                    .size(96.dp)
                    .background(Color(0xFFF3E8FF), CircleShape)
                    .border(4.dp, Color(0xFFE9D5FF), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "👩‍🏫", fontSize = 48.sp)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        AnimatedVisibility(
            visible = !spokenText.isNullOrEmpty() && !isRecording,
            enter = fadeIn() + slideInVertically { it / 2 },
            exit = fadeOut()
        ) {
            val isCorrect = spokenText?.trim().equals(targetText.trim(), ignoreCase = true)

            val bgColor = if (isCorrect) Color(0xFFF0FDF4) else Color(0xFFF9FAFB)
            val borderColor = if (isCorrect) Color(0xFF4ADE80) else Color(0xFFD1D5DB)
            val textColor = if (isCorrect) Color(0xFF16A34A) else Color(0xFF374151)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .dashedBorder(color = borderColor, width = 2.dp, radius = 16.dp)
                    .background(bgColor, RoundedCornerShape(16.dp))
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if (isCorrect) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Correct",
                        tint = Color(0xFF22C55E),
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
                Text(
                    text = "\"${spokenText.orEmpty()}\"",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Text(
                text = when {
                    isRecording -> "ĐANG NGHE..."
                    !spokenText.isNullOrEmpty() -> "CHẠM ĐỂ NÓI LẠI"
                    else -> "NHẤN VÀO MIC ĐỂ NÓI"
                },
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                color = if (isRecording) Color(0xFFEF4444) else Color(0xFF9CA3AF),
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(128.dp)
            ) {
                if (isRecording) {
                    RippleAnimation()
                }

                MicButton3D(
                    isRecording = isRecording,
                    onClick = {
                        if (!isRecording) {
                            isRecording = true
                        } else {
                            isRecording = false
                        }
                    }
                )
            }
        }
    }
}


@Composable
fun RippleAnimation() {
    val infiniteTransition = rememberInfiniteTransition(label = "RippleTransition")
    val progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "RippleProgress"
    )

    val scale1 = 1f + progress * 0.8f
    val alpha1 = 0.8f - (progress * 0.8f)

    val progress2 = (progress + 0.5f) % 1f
    val scale2 = 1f + progress2 * 0.8f
    val alpha2 = 0.8f - (progress2 * 0.8f)

    Box(
        modifier = Modifier
            .size(96.dp)
            .scale(scale1)
            .alpha(alpha1.coerceIn(0f, 1f))
            .background(Color(0xFF60A5FA), CircleShape)
    )

    Box(
        modifier = Modifier
            .size(96.dp)
            .scale(scale2)
            .alpha(alpha2.coerceIn(0f, 1f))
            .background(Color(0xFF93C5FD), CircleShape)
    )
}

@Composable
fun MicButton3D(
    isRecording: Boolean,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val yOffset = if (isPressed) 6.dp else 0.dp
    val shadowSize = if (isPressed) 0.dp else 6.dp

    val bgColor = if (isRecording) Color(0xFFEF4444) else Color(0xFF3B82F6)
    val shadowColor = if (isRecording) Color(0xFFB91C1C) else Color(0xFF1D4ED8)

    Box(
        modifier = Modifier
            .size(96.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.TopCenter
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .offset(y = 6.dp)
                .background(shadowColor, CircleShape)
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .offset(y = yOffset)
                .padding(bottom = shadowSize)
                .background(bgColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.Mic,
                contentDescription = "Microphone",
                tint = Color.White,
                modifier = Modifier.size(48.dp)
            )
        }
    }
}

@Composable
fun SmallAudioButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .background(Color(0xFFEFF6FF), CircleShape)
            .clip(CircleShape)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Rounded.VolumeUp,
            contentDescription = "Listen",
            tint = Color(0xFF3B82F6),
            modifier = Modifier.size(24.dp)
        )
    }
}


@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, name = "1. Trạng thái Ban đầu")
@Composable
fun PreviewSpeakingQuestion_Idle() {
    MaterialTheme {
        Box(modifier = Modifier.fillMaxSize().background(Color(0xFFF3F4F6))) {
            SpeakingQuestion(
                prompt = "Speak this sentence",
                targetText = "Hello, how are you?",
                phonetic = "/həˈləʊ, haʊ ɑr ju?/",
                translation = "Xin chào, bạn khỏe không?",
                initialAnswer = null,
                onAnswerSelected = {},
                onPlayAudio = {}
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, name = "2. Trạng thái Đã trả lời")
@Composable
fun PreviewSpeakingQuestion_Answered() {
    MaterialTheme {
        Box(modifier = Modifier.fillMaxSize().background(Color(0xFFF3F4F6))) {
            SpeakingQuestion(
                prompt = "Speak this sentence",
                targetText = "Hello, how are you?",
                phonetic = "/həˈləʊ, haʊ ɑr ju?/",
                translation = "Xin chào, bạn khỏe không?",
                initialAnswer = "Hello, how are you?", // Giả lập đã nói đúng
                onAnswerSelected = {},
                onPlayAudio = {}
            )
        }
    }
}