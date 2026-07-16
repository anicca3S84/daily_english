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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.StarBorder
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
import com.codework.dailyenglish.presentation.screen.flashcard.components.FinishDeckWidget
import com.codework.dailyenglish.presentation.screen.flashcard.components.FlashcardBackFace
import com.codework.dailyenglish.presentation.screen.flashcard.components.FlashcardFrontFace
import com.codework.dailyenglish.presentation.screen.flashcard.components.MainAudioButton3D
import com.codework.dailyenglish.presentation.screen.flashcard.components.SnailAudioButton3D
import com.codework.dailyenglish.presentation.screen.flashcard.mvi.FlashcardEvent
import com.codework.dailyenglish.presentation.screen.flashcard.mvi.FlashcardState
import com.codework.dailyenglish.presentation.theme.AppColors


@Composable
fun FlashcardScreen(
    state: FlashcardState,
    onEvent: (FlashcardEvent) -> Unit,
    onBackClick: () -> Unit
) {

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
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            if (state.isFinished) {
                FinishDeckWidget(onBackClick)
            } else if (state.showReviewOptions) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "You finished this round!",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = AppColors.TextMain
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "You have ${state.forgotWords.size} words left to learn. What do you want to do?",
                        fontSize = 16.sp,
                        color = Color(0xFF64748B),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Button(
                        onClick = { onEvent(FlashcardEvent.OnReviewForgotWords) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AppColors.BluePrimary)
                    ) {
                        Text(text = "Review forgotten words", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedButton(
                        onClick = { onEvent(FlashcardEvent.OnForceFinish) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                    ) {
                        Text(
                            text = "Finish lesson",
                            fontSize = 15.sp,
                            color = Color(0xFF64748B),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            } else {
                state.words.getOrNull(state.currentIndex)?.let { word ->
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color.White)
                                .border(
                                    1.dp,
                                    Color(0xFFE2E8F0),
                                    RoundedCornerShape(16.dp)
                                )
                        ) {

                            Row(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(vertical = 14.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Close,
                                    contentDescription = null,
                                    tint = Color(0xFFEF4444),
                                    modifier = Modifier.size(18.dp)
                                )

                                Spacer(modifier = Modifier.width(6.dp))

                                Text(
                                    text = "Learning: ${state.forgotWords.size}",
                                    color = Color(0xFFEF4444),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .width(1.dp)
                                    .height(28.dp)
                                    .background(Color(0xFFE2E8F0))
                                    .align(Alignment.CenterVertically)
                            )

                            Row(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(vertical = 14.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Check,
                                    contentDescription = null,
                                    tint = AppColors.BluePrimary,
                                    modifier = Modifier.size(18.dp)
                                )

                                Spacer(modifier = Modifier.width(6.dp))

                                Text(
                                    text = "Mastered: ${state.currentIndex - state.forgotWords.size}",
                                    color = AppColors.BluePrimary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(40.dp))

                        val rotation3d by animateFloatAsState(
                            targetValue = if (state.isFlipped) 180f else 0f,
                            animationSpec = tween(durationMillis = 500),
                            label = "CardFlipAnimation"
                        )

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight(0.70f)
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
                                colors = CardDefaults.cardColors(containerColor = AppColors.FlashCardBackground),
                                border = BorderStroke(width = 1.dp, color = AppColors.FlashCardBorder),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .graphicsLayer {
                                            if (rotation3d > 90f) rotationY = 180f
                                        }
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .padding(16.dp)
                                            .size(42.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFFF8FAFC))
                                            .border(width = 1.dp, color = Color(0xFFE2E8F0), shape = CircleShape)
                                            .clickable(
                                                interactionSource = remember { MutableInteractionSource() },
                                                indication = ripple()
                                            ) {
                                                onEvent(FlashcardEvent.OnToggleSaveClick)
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = if (word.isSaved) Icons.Rounded.Star else Icons.Rounded.StarBorder,
                                            contentDescription = "Save Word",
                                            tint = if (word.isSaved) Color(0xFFFFC83D) else Color(0xFF94A3B8),
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }

                                    if (rotation3d <= 90f) {
                                        FlashcardFrontFace(
                                            word = word.word,
                                            phonetic = word.usPhonetic
                                        )
                                    } else {
                                        FlashcardBackFace(
                                            meaning = word.meaning,
                                            examples = word.examples
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.weight(1f))

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                MainAudioButton3D(
                                    onClick = { onEvent(FlashcardEvent.OnPlayAudio) }
                                )

                                SnailAudioButton3D(
                                    onClick = { onEvent(FlashcardEvent.OnPlayAudioSlow)  }
                                )
                            }

                            Spacer(modifier = Modifier.weight(1f))
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Button(
                                onClick = {
                                    onEvent(FlashcardEvent.OnStillLearningClick)
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(50.dp),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFEF4444),
                                    contentColor = Color.White
                                )
                            ) {
                                Text(text = "Forgot", fontSize = 15.sp, fontWeight = FontWeight.Black)
                            }

                            Button(
                                onClick = {
                                    onEvent(FlashcardEvent.OnKnowItClick)
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(50.dp),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = AppColors.BluePrimary,
                                    contentColor = Color.White
                                )
                            ) {
                                Text(text = "Know it", fontSize = 15.sp, fontWeight = FontWeight.Black)
                            }
                        }
                    }
                }
            }
        }
    }
}