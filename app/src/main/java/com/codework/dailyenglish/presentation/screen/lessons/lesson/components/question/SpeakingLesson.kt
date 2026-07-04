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
import androidx.compose.material.icons.rounded.PlayArrow
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
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import com.codework.dailyenglish.presentation.utils.calculateSimilarity


@Composable
fun SpeakingQuestion(
    prompt: String,
    targetText: String,
    phonetic: String,
    translation: String,
    initialAnswer: String? = null,
    isCorrect: Boolean? = null,
    isEvaluated: Boolean = false,
    modifier: Modifier = Modifier,
    onAnswerSelected: (String) -> Unit,
    onPlayAudio: () -> Unit,
) {
//    val context = LocalContext.current
//    val voiceToTextParser = remember { VoiceToTextParser(context) }
//    val voiceState by voiceToTextParser.state.collectAsState()
//
//    val currentAnswer = voiceState.spokenText.takeIf { it.isNotBlank() } ?: initialAnswer
//
//    val similarity = currentAnswer?.calculateSimilarity(targetText) ?: 0f
//    val isPass = if (isEvaluated && isCorrect != null) {
//        isCorrect
//    } else {
//        similarity >= 0.8f
//    }
//
//    val canInteract = !isEvaluated
//
//    val sentenceColor by animateColorAsState(
//        targetValue = when {
//            currentAnswer.isNullOrBlank() -> Color(0xFF1F2937)
//            isPass -> Color(0xFF10B981)
//            else -> Color(0xFFEF4444)
//        },
//        label = "textColor"
//    )
//
//    var hasPermission by remember {
//        mutableStateOf(
//            ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
//        )
//    }
//
//    val permissionLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.RequestPermission(),
//        onResult = { isGranted ->
//            hasPermission = isGranted
//            if (isGranted && canInteract) {
//                voiceToTextParser.startListening()
//            }
//        }
//    )
//
//    LaunchedEffect(voiceState.spokenText) {
//        if (voiceState.spokenText.isNotBlank()) {
//            onAnswerSelected(voiceState.spokenText)
//        }
//    }
//
//    DisposableEffect(Unit) {
//        onDispose { voiceToTextParser.stopListening() }
//    }
//
//    Column(
//        modifier = modifier
//            .fillMaxWidth()
//            .fillMaxHeight()
//            .padding(horizontal = 14.dp, vertical = 12.dp),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
//    ) {
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .shadow(20.dp, RoundedCornerShape(32.dp), spotColor = Color(0x1A000000))
//                .background(Color.White, RoundedCornerShape(32.dp))
//                .border(1.dp, Color(0xFFF3F4F6), RoundedCornerShape(32.dp))
//                .padding(vertical = 24.dp, horizontal = 24.dp),
//            contentAlignment = Alignment.TopCenter
//        ) {
//            Column(
//                horizontalAlignment = Alignment.CenterHorizontally,
//                modifier = Modifier.fillMaxSize()
//            ) {
//                Row(
//                    modifier = Modifier
//                        .background(Color(0xFFEFF6FF), RoundedCornerShape(16.dp))
//                        .padding(horizontal = 20.dp, vertical = 8.dp),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Icon(Icons.Rounded.GraphicEq, null, tint = Color(0xFF3B82F6), modifier = Modifier.size(14.dp))
//                    Spacer(Modifier.width(6.dp))
//                    Text(text = "Say this aloud", color = Color(0xFF2563EB), fontSize = 14.sp, fontWeight = FontWeight.W600)
//                }
//
//                Spacer(Modifier.weight(1f))
//
//                Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                    Text(
//                        text = if (currentAnswer.isNullOrBlank())
////                            targetText
//                            "They say I'm an old soul."
//                        else currentAnswer,
//                        fontSize = 30.sp,
//                        fontWeight = FontWeight.W600,
//                        color = sentenceColor,
//                        textAlign = TextAlign.Center,
//                        lineHeight = 36.sp,
//                        style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false)),
//                    )
//
//                    if (voiceState.error != null) {
//                        Text(text = voiceState.error!!, color = Color(0xFFEF4444), fontSize = 14.sp, modifier = Modifier.padding(top = 16.dp))
//                    }
//
//                    Spacer(Modifier.height(24.dp))
//
//                    Text(text =
////                        phonetic,
//                        "/ ðeɪ seɪ aɪm ən oʊld soʊl /",
//                        fontSize = 18.sp, color = Color(0xFF9CA3AF), textAlign = TextAlign.Center)
//
//                    Spacer(Modifier.height(24.dp))
//
//                    Box(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .background(Color(0xFFF9FAFB), RoundedCornerShape(16.dp))
//                            .padding(horizontal = 20.dp, vertical = 14.dp),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Text(text =
////                            translation,
//                            "Họ nói tôi là một người có\ntâm hồn già dặn.",
//                            fontSize = 15.sp, color = Color(0xFF6B7280), textAlign = TextAlign.Center)
//                    }
//                }
//
//                Spacer(Modifier.weight(1f))
//
//                Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                    Box(Modifier.fillMaxWidth(0.8f).height(1.dp).background(Color(0xFFF3F4F6)))
//                    Spacer(Modifier.height(24.dp))
//
//                    Text(
//                        text = when {
//                            isEvaluated -> "Completed"
//                            voiceState.isSpeaking -> "Listening... tap to stop"
//                            !currentAnswer.isNullOrBlank() -> "Recorded (Similarity: ${(similarity * 100).toInt()}%)"
//                            else -> "Tap the mic to speak"
//                        },
//                        fontSize = 15.sp,
//                        color = if (voiceState.isSpeaking) Color(0xFFEF4444) else Color(0xFF9CA3AF),
//                        modifier = Modifier.padding(bottom = 24.dp)
//                    )
//
//                    Row(
//                        modifier = Modifier.fillMaxWidth(),
//                        horizontalArrangement = Arrangement.Center,
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        ElegantAudioButton(Icons.Rounded.VolumeUp, onClick = onPlayAudio)
//                        Spacer(Modifier.width(32.dp))
//                        Box(contentAlignment = Alignment.Center) {
//                            if (voiceState.isSpeaking) MicRippleAnimation()
//
//                            MicButtonElegant(
//                                isRecording = voiceState.isSpeaking,
//                                isEnabled = canInteract, // Truyền trạng thái khóa mic vào đây
//                                onClick = {
//                                    if (!canInteract) return@MicButtonElegant
//
//                                    if (voiceState.isSpeaking) {
//                                        voiceToTextParser.stopListening()
//                                    } else {
//                                        if (hasPermission) {
//                                            voiceToTextParser.startListening()
//                                        } else {
//                                            permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
//                                        }
//                                    }
//                                }
//                            )
//                        }
//                        Spacer(Modifier.width(32.dp))
//                        ElegantAudioButton(Icons.Rounded.PlayArrow, onClick = onPlayAudio) // Hoặc đổi icon này nếu có logic khác
//                    }
//                }
//            }
//        }
//    }
}

@Composable
fun MicButtonElegant(
    isRecording: Boolean,
    onClick: () -> Unit,
    isEnabled: Boolean = true,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(if (isPressed) 0.9f else 1f, label = "scale")

    val gradientColors = when {
        !isEnabled -> listOf(Color(0xFFD1D5DB), Color(0xFF9CA3AF)) // Màu xám khi bị vô hiệu hóa
        isRecording -> listOf(Color(0xFFEF4444), Color(0xFFEC4899)) // Màu đỏ khi đang thu âm
        else -> listOf(Color(0xFF2563EB), Color(0xFF60A5FA)) // Màu xanh mặc định
    }
    val shadowColor = if (isRecording) Color(0x66EF4444) else Color(0x662563EB)

    Box(
        modifier = Modifier
            .size(80.dp)
            .alpha(if (isEnabled) 1f else 0.6f)
            .scale(scale)
            .shadow(
                elevation = if (isEnabled) 16.dp else 0.dp, // Bỏ bóng khi disabled
                shape = CircleShape,
                spotColor = shadowColor,
                ambientColor = shadowColor
            )
            .background(Brush.linearGradient(gradientColors), CircleShape)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = isEnabled, // Khóa click thực sự ở cấp độ Compose
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Rounded.Mic,
            contentDescription = "Microphone",
            tint = Color.White,
            modifier = Modifier.size(36.dp)
        )
    }
}

@Composable
fun ElegantAudioButton(icon: ImageVector, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(if (isPressed) 0.9f else 1f, label = "scale")

    Box(
        modifier = Modifier
            .size(52.dp)
            .scale(scale)
            .clip(CircleShape)
            .background(Color(0xFFEFF6FF))
            .border(1.dp, Color(0xFFDBEAFE), CircleShape)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "Audio",
            tint = Color(0xFF2563EB),
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun MicRippleAnimation() {
    val infiniteTransition = rememberInfiniteTransition(label = "Ripple")
    val progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "RippleProgress"
    )

    val rippleGradient = Brush.linearGradient(listOf(Color(0x663B82F6), Color(0x66A855F7)))

    Box(
        modifier = Modifier
            .size(80.dp)
            .scale(1f + progress * 1.5f)
            .alpha((0.8f - progress).coerceIn(0f, 1f))
            .background(rippleGradient, CircleShape)
    )

    val progress2 = (progress + 0.5f) % 1f
    Box(
        modifier = Modifier
            .size(80.dp)
            .scale(1f + progress2 * 1.5f)
            .alpha((0.8f - progress2).coerceIn(0f, 1f))
            .background(rippleGradient, CircleShape)
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFF8FAFC, name = "1. Trạng thái Mặc định")
@Composable
fun SpeakingQuestionPreview() {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        SpeakingQuestion(
            modifier = Modifier.fillMaxWidth(),
            prompt = "Đọc to câu này",
            targetText = "They say I'm an old soul.",
            phonetic = "/ ðeɪ seɪ aɪm ən oʊld soʊl /",
            translation = "Họ nói tôi là một người có\ntâm hồn già dặn.",
            initialAnswer = null,
            onAnswerSelected = {},
            onPlayAudio = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF8FAFC, name = "2. Trạng thái Đã ghi âm")
@Composable
fun SpeakingQuestionRecordedPreview() {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        SpeakingQuestion(
            modifier = Modifier.fillMaxWidth(),
            prompt = "Đọc to câu này",
            targetText = "They say I'm an old soul.",
            phonetic = "/ ðeɪ seɪ aɪm ən oʊld soʊl /",
            translation = "Họ nói tôi là một người có\ntâm hồn già dặn.",
            initialAnswer = "They say I'm an old soul.",
            onAnswerSelected = {},
            onPlayAudio = {}
        )
    }
}