package com.codework.dailyenglish.presentation.screen.grammar

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codework.dailyenglish.presentation.components.AppTopBar
import com.codework.dailyenglish.presentation.screen.grammar.model.GrammarTip
import com.codework.dailyenglish.presentation.screen.grammar.model.GrammarTopic
import com.codework.dailyenglish.presentation.screen.grammar.mvi.GrammarEvent
import com.codework.dailyenglish.presentation.screen.grammar.mvi.GrammarState
import com.codework.dailyenglish.presentation.theme.AppColors


@Composable
fun GrammarScreen(
    state: GrammarState,
    onEvent: (GrammarEvent) -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        containerColor = AppColors.AppBackground,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            AppTopBar(
                onBackClick = onBackClick,
                onMenuClick = { onEvent(GrammarEvent.OnSearchClick) },
                titleContent = {
                    Text(
                        text = "Grammar Handbook",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = AppColors.TextPrimary
                    )
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(AppColors.AppBackground)
        ) {

//                LazyColumn(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .padding(horizontal = 16.dp),
//                    verticalArrangement = Arrangement.spacedBy(16.dp)
//                ) {
//                    item { Spacer(modifier = Modifier.height(8.dp)) }
//
//                    items(data.grammarTopics) { topic ->
//                        GrammarTopicItem(
//                            topic = topic,
//                            onClick = { onEvent(GrammarEvent.OnTopicClick(topic.id)) }
//                        )
//                    }
//
//                    item { Spacer(modifier = Modifier.height(24.dp)) }
//                }
            }
        }
}

// --- COMPONENTS ---

@Composable
fun GrammarTipCard(tip: GrammarTip) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.horizontalGradient(listOf(Color(0xFFFFF7ED), Color.White)))
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Surface(
                shape = CircleShape,
                color = Color(0xFFF59E0B).copy(alpha = 0.1f),
                modifier = Modifier.size(40.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Outlined.Lightbulb,
                        contentDescription = null,
                        tint = Color(0xFFF59E0B),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = tip.title,
                    style = MaterialTheme.typography.labelMedium,
                    color = Color(0xFFF59E0B),
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = tip.content,
                    style = MaterialTheme.typography.bodyMedium,
                    color = AppColors.TextPrimary,
                    lineHeight = 20.sp
                )
            }
        }
    }
}


@Composable
fun GrammarTopicItem(
    topic: GrammarTopic,
    onClick: () -> Unit
) {
    val progressAnimated by animateFloatAsState(targetValue = topic.progress, label = "progress")

    Surface(
        onClick = onClick,
        enabled = !topic.isLocked,
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = 1.dp,
        border = BorderStroke(1.dp, Color(0xFFF1F5F9)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon bên trái
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(if (topic.isLocked) Color(0xFFF1F5F9) else AppColors.BluePrimary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                if (topic.isLocked) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = AppColors.GrayText.copy(alpha = 0.5f),
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.School,
                        contentDescription = null,
                        tint = AppColors.BluePrimary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Nội dung chính
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = topic.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (topic.isLocked) AppColors.GrayText else AppColors.TextPrimary
                    )

                    // Level Badge
                    Surface(
                        color = if (topic.isLocked) Color(0xFFF1F5F9) else Color(0xFFF0FDF4),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = topic.level,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = if (topic.isLocked) AppColors.GrayText else AppColors.SuccessGreen,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = topic.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = AppColors.GrayText,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Thanh tiến độ
                if (!topic.isLocked) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        LinearProgressIndicator(
                            progress = { progressAnimated },
                            modifier = Modifier
                                .weight(1f)
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = if (progressAnimated >= 1.0f) AppColors.SuccessGreen else AppColors.BluePrimary,
                            trackColor = Color(0xFFF1F5F9)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "${(progressAnimated * 100).toInt()}%",
                            style = MaterialTheme.typography.labelSmall,
                            color = AppColors.GrayText,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}