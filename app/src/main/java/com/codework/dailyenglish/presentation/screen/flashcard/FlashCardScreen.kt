package com.codework.dailyenglish.presentation.screen.flashcard

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Replay
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.StarBorder
import androidx.compose.material.icons.rounded.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codework.dailyenglish.presentation.components.AppProgressTopBar
import com.codework.dailyenglish.presentation.screen.flashcard.mvi.FlashcardEvent
import com.codework.dailyenglish.presentation.screen.flashcard.mvi.FlashcardState
import com.codework.dailyenglish.presentation.theme.AppColors

@Composable
fun FlashcardScreen(
    state: FlashcardState,
    onEvent: (FlashcardEvent) -> Unit,
    onBackClick: () -> Unit
) {
    var forgotCount by rememberSaveable { mutableIntStateOf(0) }
    var knowItCount by rememberSaveable { mutableIntStateOf(0) }

    Scaffold(
        containerColor = AppColors.AppBackground,
        topBar = {
            AppProgressTopBar(
                progress = state.progress,
                progressText = state.progressText,
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            if (state.isFinished) {
                FinishDeckWidget(onBackClick)
            } else {
                state.words.getOrNull(state.currentIndex)?.let { word ->
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(4.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp)) // Bo góc mượt mà toàn khối
                        ) {
                            // Cụm Chưa thuộc (Nửa trái đỏ)
                            Row(
                                modifier = Modifier
                                    .weight(1f) // Chiếm exacly 50%
                                    .background(Color(0xFFEF4444))
                                    .padding(vertical = 10.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Close,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "$forgotCount",
                                    color = Color.White,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 15.sp
                                )
                            }

                            // Cụm Đã thuộc (Nửa phải xanh)
                            Row(
                                modifier = Modifier
                                    .weight(1f) // Chiếm exactly 50%
                                    .background(Color(0xFF22C55E))
                                    .padding(vertical = 10.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Check,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "$knowItCount",
                                    color = Color.White,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 15.sp
                                )
                            }
                        }

                        // Hiệu ứng lật 3D mượt mà
                        val rotation3d by animateFloatAsState(
                            targetValue = if (state.isFlipped) 180f else 0f,
                            animationSpec = tween(durationMillis = 500),
                            label = "CardFlipAnimation"
                        )

                        // --- CONTAINER CARD ---
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(vertical = 16.dp)
                                .graphicsLayer {
                                    rotationY = rotation3d
                                    cameraDistance = 14 * density
                                }
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) {
                                    onEvent(FlashcardEvent.OnCardClick)
                                },
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .graphicsLayer {
                                        if (rotation3d > 90f) rotationY = 180f
                                    }
                            ) {
                                // 🌟 NÚT STAR/SAVE TÍCH HỢP TRÊN GÓC PHẢI CARD
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(20.dp)
                                        .size(32.dp)
                                        .clickable(
                                            interactionSource = remember { MutableInteractionSource() },
                                            indication = null
                                        ) {
                                            onEvent(FlashcardEvent.OnToggleSaveClick)
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = if (word.isSaved) Icons.Rounded.Star else Icons.Rounded.StarBorder,
                                        contentDescription = "Save Word",
                                        tint = if (word.isSaved) Color(0xFFF59E0B) else Color(0xFF94A3B8),
                                        modifier = Modifier.size(30.dp)
                                    )
                                }

                                // --- NỘI DUNG THẺ ---
                                if (rotation3d <= 90f) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(24.dp)
                                    ) {

                                        Column(
                                            modifier = Modifier
                                                .align(Alignment.Center)
                                                .fillMaxWidth(),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {

                                            Text(
                                                text = word.word,
                                                fontSize = 30.sp,
                                                fontWeight = FontWeight.Black,
                                                color = AppColors.TextMain,
                                                textAlign = TextAlign.Center,
                                                letterSpacing = 0.5.sp
                                            )

                                            Spacer(modifier = Modifier.height(8.dp))

                                            Text(
                                                text = word.usPhonetic,
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.Medium,
                                                color = Color(0xFF64748B),
                                                textAlign = TextAlign.Center
                                            )

                                            Spacer(modifier = Modifier.height(36.dp))

                                            Row(
                                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                MainAudioButton3D(
                                                    onClick = {
                                                        onEvent(FlashcardEvent.OnPlayAudio)
                                                    }
                                                )

                                                SnailAudioButton3D(
                                                    onClick = {
                                                        // TODO: Phát âm chậm
                                                    }
                                                )
                                            }
                                        }
                                    }
                                } else {
                                    // MẶT SAU THẺ: NGHĨA VÀ VÍ DỤ
                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(28.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Text(
                                            text = word.meaning,
                                            fontSize = 26.sp,
                                            fontWeight = FontWeight.Black,
                                            color = Color(0xFF0F172A),
                                            textAlign = TextAlign.Center
                                        )

                                        if (word.examples.isNotEmpty()) {
                                            Spacer(modifier = Modifier.height(24.dp))
                                            Column(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .background(Color(0xFFF8FAFC), RoundedCornerShape(16.dp))
                                                    .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(16.dp))
                                                    .padding(16.dp)
                                            ) {
                                                Text(
                                                    text = "EXAMPLE SENTENCE",
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Black,
                                                    color = AppColors.BluePrimary,
                                                    letterSpacing = 1.sp
                                                )
                                                Spacer(modifier = Modifier.height(8.dp))
                                                Text(
                                                    text = word.examples.first(),
                                                    fontSize = 15.sp,
                                                    color = Color(0xFF334155),
                                                    fontWeight = FontWeight.Bold,
                                                    lineHeight = 22.sp
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Button(
                                onClick = {
                                    forgotCount++
                                    onEvent(FlashcardEvent.OnStillLearningClick)
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(56.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFEF4444),
                                    contentColor = Color.White
                                )
                            ) {
                                Text(text = "FORGOT", fontSize = 15.sp, fontWeight = FontWeight.Black)
                            }

                            Button(
                                onClick = {
                                    knowItCount++
                                    onEvent(FlashcardEvent.OnKnowItClick)
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(56.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF22C55E),
                                    contentColor = Color.White
                                )
                            ) {
                                Text(text = "KNOW IT", fontSize = 15.sp, fontWeight = FontWeight.Black)
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun SnailAudioButton3D(onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val yOffset by animateDpAsState(
        targetValue = if (isPressed) 4.dp else 0.dp,
        label = "yOffset"
    )

    Box(
        modifier = Modifier
            .size(64.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .offset(y = 4.dp)
                .background(Color(0xFFE5E5E5), CircleShape)
        )
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .offset(y = yOffset),
            shape = CircleShape,
            color = Color.White,
            border = BorderStroke(2.dp, Color(0xFFE5E5E5))
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(text = "🐌", fontSize = 24.sp)
            }
        }
    }
}

@Composable
fun MainAudioButton3D(onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val yOffset by animateDpAsState(
        targetValue = if (isPressed) 4.dp else 0.dp,
        label = "yOffset"
    )

    Box(
        modifier = Modifier
            .size(72.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .offset(y = 4.dp)
                .background(Color(0xFF1899D6), CircleShape)
        )
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .offset(y = yOffset),
            shape = CircleShape,
            color = Color(0xFF1CB0F6),
            border = BorderStroke(2.dp, Color(0xFF1899D6))
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Rounded.VolumeUp,
                    contentDescription = "TTS normal speed",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}

@Composable
fun FinishDeckWidget(onBackClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "🏆", fontSize = 72.sp)
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Session Completed!",
            fontSize = 26.sp,
            fontWeight = FontWeight.Black,
            color = AppColors.TextMain
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "You have reviewed all selected cards successfully.",
            fontSize = 15.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = onBackClick,
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .height(54.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AppColors.BluePrimary)
        ) {
            Icon(imageVector = Icons.Rounded.Replay, contentDescription = null, tint = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Restart Session", fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}