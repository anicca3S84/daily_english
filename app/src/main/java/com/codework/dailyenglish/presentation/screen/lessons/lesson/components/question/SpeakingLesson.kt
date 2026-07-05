package com.codework.dailyenglish.presentation.screen.lessons.lesson.components.question

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.GraphicEq
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material.icons.rounded.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.codework.dailyenglish.core.utils.VoiceToTextParserState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.material3.Surface
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke

import androidx.compose.ui.unit.Dp
import com.codework.dailyenglish.presentation.screen.flashcard.components.MainAudioButton3D
import com.codework.dailyenglish.presentation.screen.flashcard.components.SnailAudioButton3D
import com.codework.dailyenglish.presentation.screen.lessons.lesson.components.question.components.MicRippleAnimation


@Composable
fun SpeakingQuestion(
    prompt: String,
    targetText: String,
    phonetic: String,
    translation: String,
    initialAnswer: String? = null,
    isCorrect: Boolean? = null,
    isEvaluated: Boolean = false,
    score: Int? = null,
    voiceState: VoiceToTextParserState,
    modifier: Modifier = Modifier,
    onAnswerSelected: (String) -> Unit,
    onStartSpeech: () -> Unit,
    onStopSpeech: () -> Unit,
    onPlayAudio: () -> Unit,
    onPlayAudioSlow: () -> Unit,
) {
    val context = LocalContext.current
    val canInteract = !isEvaluated

    // SỬA LỖI ĐÈ CHỮ: Chỉ lấy text nhận diện thời gian thực khi Micro đang bật,
    // ngược lại ưu tiên hiển thị đáp án đã lưu cho riêng câu này.
    val currentAnswer = if (voiceState.isListening) {
        voiceState.spokenText.takeIf { it.isNotBlank() } ?: initialAnswer
    } else {
        initialAnswer
    }

    // Xác định màu sắc dựa theo mốc điểm hoặc dựa theo isCorrect phòng khi score bị null
    val scoreColor = when {
        !isEvaluated -> Color(0xFFE5E7EB) // Mặc định khi chưa chấm
        score != null -> {
            when {
                score < 30 -> Color(0xFFEF4444) // Đỏ (Kém)
                score < 60 -> Color(0xFFF59E0B) // Vàng (Trung bình)
                else -> Color(0xFF10B981) // Xanh lá (Tốt)
            }
        }
        else -> if (isCorrect == true) Color(0xFF10B981) else Color(0xFFEF4444)
    }

    // Tính toán góc quét của vòng cung mượt mà
    val targetSweepAngle = when {
        !isEvaluated -> 270f
        score != null -> 270f * (score.coerceIn(0, 100) / 100f)
        isCorrect == true -> 270f
        else -> 0f
    }

    val animatedSweepAngle by animateFloatAsState(
        targetValue = targetSweepAngle,
        animationSpec = tween(1200, easing = FastOutSlowInEasing),
        label = "sweepAngle"
    )

    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            hasPermission = isGranted
            if (isGranted && canInteract) {
                onStartSpeech()
            }
        }
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF3F7FA))
            .padding(top = 28.dp, bottom = 48.dp, start = 24.dp, end = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // 1. Chữ tiêu đề trên đầu
        Text(
            text = prompt.ifBlank { "Say this aloud" },
            color = Color(0xFF3B82F6),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier.size(150.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawArc(
                        color = Color(0xFFE5E7EB).copy(alpha = 0.5f),
                        startAngle = 135f,
                        sweepAngle = 270f,
                        useCenter = false,
                        style = Stroke(width = 10.dp.toPx(), cap = StrokeCap.Round)
                    )
                    drawArc(
                        color = scoreColor,
                        startAngle = 135f,
                        sweepAngle = animatedSweepAngle,
                        useCenter = false,
                        style = Stroke(width = 10.dp.toPx(), cap = StrokeCap.Round)
                    )
                }
                Text(
                    text = if (isEvaluated) {
                        score?.toString() ?: if (isCorrect == true) "100" else "0"
                    } else "--",
                    fontSize = 38.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isEvaluated) scoreColor else Color(0xFF9CA3AF)
                )
            }

            Spacer(Modifier.height(32.dp))

            // Từ mẫu hiển thị (hoặc câu trả lời đang nói)
            Text(
                text = if (currentAnswer.isNullOrBlank()) targetText else currentAnswer,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF111827),
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(8.dp))

            if (phonetic.isNotBlank()) {
                Text(
                    text = phonetic,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF8C96A3),
                    textAlign = TextAlign.Center
                )
            }

            if (translation.isNotBlank()) {
                Spacer(Modifier.height(6.dp))
                Text(
                    text = translation,
                    fontSize = 14.sp,
                    color = Color(0xFF9CA3AF),
                    textAlign = TextAlign.Center
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            MainAudioButton3D(onClick = onPlayAudio)
            SnailAudioButton3D(onClick = onPlayAudioSlow)
        }

        Box(
            modifier = Modifier.size(110.dp),
            contentAlignment = Alignment.Center
        ) {
            if (voiceState.isListening) {
                MicRippleAnimation(micSize = 76.dp)
            } else {
                Box(
                    modifier = Modifier
                        .size(110.dp)
                        .background(Color(0xFFE8F0FE).copy(alpha = 0.5f), CircleShape)
                )
            }

            Box(
                modifier = Modifier
                    .size(76.dp)
                    .shadow(4.dp, CircleShape)
                    .background(Color.White, CircleShape)
                    .clickable(
                        enabled = canInteract,
                        onClick = {
                            if (!canInteract) return@clickable

                            if (voiceState.isListening) {
                                onStopSpeech()
                            } else {
                                if (hasPermission) {
                                    onStartSpeech()
                                } else {
                                    permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                                }
                            }
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.Mic,
                    contentDescription = "Microphone",
                    tint = if (voiceState.isListening) Color(0xFFEF4444) else Color(0xFF3B82F6),
                    modifier = Modifier.size(36.dp)
                )
            }
        }
    }
}


