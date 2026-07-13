package com.codework.dailyenglish.presentation.screen.practice

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.SmartToy
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout // Thư viện xử lý layout lề âm
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codework.dailyenglish.R
import com.codework.dailyenglish.domain.vocabulary.model.VocabTopic
import com.codework.dailyenglish.presentation.components.BannerAd
import com.codework.dailyenglish.presentation.components.StateWrapper
import com.codework.dailyenglish.presentation.screen.practice.components.AiTopicCard
import com.codework.dailyenglish.presentation.screen.practice.components.HeaderWaveShape
import com.codework.dailyenglish.presentation.screen.practice.components.SeeAllCard
import com.codework.dailyenglish.presentation.screen.practice.mvi.PracticeEvent
import com.codework.dailyenglish.presentation.screen.practice.mvi.PracticeState
import com.codework.dailyenglish.presentation.theme.AppColors

@Composable
fun PracticeScreen(
    state: PracticeState,
    onEvent: (PracticeEvent) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC)),
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(270.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_practice_bg),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(HeaderWaveShape())
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(top = 8.dp)
                    ) {
                        Text(
                            text = "Practice Hub",
                            fontSize = 30.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
                            style = TextStyle(letterSpacing = (-0.5).sp)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Level up your speaking & skills",
                            fontSize = 15.sp,
                            color = Color.White.copy(alpha = 0.85f),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(30.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .layout { measurable, constraints ->
                        val placeable = measurable.measure(constraints)
                        val overlapPx = 75.dp.roundToPx()
                        val calculatedHeight = (placeable.height - overlapPx).coerceAtLeast(0)
                        layout(placeable.width, calculatedHeight) {
                            placeable.placeRelative(0, -overlapPx)
                        }
                    }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .background(Color(0xFFEEF2F6), CircleShape)
                            .border(1.dp, Color(0xFFE2E8F0), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.SmartToy,
                            contentDescription = "AI Assistant",
                            tint = Color.White,
                            modifier = Modifier
                                .size(32.dp)
                                .graphicsLayer {
                                    compositingStrategy = CompositingStrategy.Offscreen
                                }
                                .drawWithCache {
                                    onDrawWithContent {
                                        drawContent()
                                        drawRect(
                                            brush = AppColors.MainGradient,
                                            blendMode = BlendMode.SrcIn
                                        )
                                    }
                                }
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "AI Speaking Hub",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier
                                    .size(18.dp)
                                    .graphicsLayer {
                                        compositingStrategy = CompositingStrategy.Offscreen
                                    }
                                    .drawWithCache {
                                        onDrawWithContent {
                                            drawContent()
                                            drawRect(
                                                brush = AppColors.MainGradient,
                                                blendMode = BlendMode.SrcIn
                                            )
                                        }
                                    }
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Choose a topic and start talking",
                            fontSize = 14.sp,
                            color = Color(0xFF64748B),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                StateWrapper(uiState = state.topicsState) { topics ->
                    val allTopics = topics ?: emptyList()

                    Column(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            AiTopicCard(
                                topic = VocabTopic(
                                    id = "free_talk",
                                    name = "Free Talk",
                                    imageUrl = "https://cdn-icons-png.flaticon.com/512/2040/2040946.png",
                                    description = "Explore this topic",
                                    totalWords = 0
                                ),
                                onClick = {
                                    onEvent(PracticeEvent.OnAiTopicClick("free_talk", "Free Talk"))
                                },
                                modifier = Modifier.weight(1f)
                            )

                            allTopics.getOrNull(0)?.let { topic ->
                                AiTopicCard(
                                    topic = topic,
                                    onClick = {
                                        onEvent(PracticeEvent.OnAiTopicClick(topic.id, topic.name))
                                    },
                                    modifier = Modifier.weight(1f)
                                )
                            } ?: Spacer(modifier = Modifier.weight(1f))
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            allTopics.getOrNull(1)?.let { topic ->
                                AiTopicCard(
                                    topic = topic,
                                    onClick = {
                                        onEvent(PracticeEvent.OnAiTopicClick(topic.id, topic.name))
                                    },
                                    modifier = Modifier.weight(1f)
                                )
                            } ?: Spacer(modifier = Modifier.weight(1f))

                            allTopics.getOrNull(2)?.let { topic ->
                                AiTopicCard(
                                    topic = topic,
                                    onClick = {
                                        onEvent(PracticeEvent.OnAiTopicClick(topic.id, topic.name))
                                    },
                                    modifier = Modifier.weight(1f)
                                )
                            } ?: Spacer(modifier = Modifier.weight(1f))
                        }

                        if (!state.isPremium) {
                            BannerAd(
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        // Hàng 3
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            allTopics.getOrNull(3)?.let { topic ->
                                AiTopicCard(
                                    topic = topic,
                                    onClick = {
                                        onEvent(PracticeEvent.OnAiTopicClick(topic.id, topic.name))
                                    },
                                    modifier = Modifier.weight(1f)
                                )
                            } ?: Spacer(modifier = Modifier.weight(1f))

                            SeeAllCard(
                                onClick = { onEvent(PracticeEvent.OnExploreMoreAiTopics) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}