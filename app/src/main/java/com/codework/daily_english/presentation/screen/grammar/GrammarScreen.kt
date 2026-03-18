package com.codework.daily_english.presentation.screen.grammar

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items // Quan trọng: Import extension items cho List
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
import com.codework.daily_english.presentation.components.AppTopBar
import com.codework.daily_english.presentation.components.BaseScreen
import com.codework.daily_english.presentation.components.UiState
import com.codework.daily_english.presentation.model.GrammarTip
import com.codework.daily_english.presentation.model.GrammarTopic
import com.codework.daily_english.presentation.theme.AppBackground
import com.codework.daily_english.presentation.theme.BluePrimary
import com.codework.daily_english.presentation.theme.GrayText
import com.codework.daily_english.presentation.theme.SuccessGreen
import com.codework.daily_english.presentation.theme.TextPrimary

@Composable
fun GrammarScreen(
    state: UiState<GrammarState>, // SỬA: GrammarData -> GrammarState
    onEvent: (GrammarEvent) -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        containerColor = AppBackground,
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
                        color = TextPrimary
                    )
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(AppBackground)
        ) {
            BaseScreen(state = state) { data ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item { Spacer(modifier = Modifier.height(8.dp)) }

                    // 1. DAILY TIP CARD
                    data.dailyTip?.let { tip ->
                        item {
                            GrammarTipCard(tip)
                        }
                    }

                    // 2. FILTER TABS
                    item {
                        FilterTabs(
                            selectedLevel = data.selectedLevel,
                            onLevelSelected = { onEvent(GrammarEvent.OnLevelFilterSelected(it)) }
                        )
                    }

                    // 3. TOPIC LIST
                    // Vì data.grammarTopics đã được định nghĩa đúng kiểu List<GrammarTopic> trong GrammarData
                    // nên items() sẽ hoạt động đúng.
                    items(data.grammarTopics) { topic ->
                        GrammarTopicItem(
                            topic = topic,
                            onClick = { onEvent(GrammarEvent.OnTopicClick(topic.id)) }
                        )
                    }

                    item { Spacer(modifier = Modifier.height(24.dp)) }
                }
            }
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
                    color = TextPrimary,
                    lineHeight = 20.sp
                )
            }
        }
    }
}

@Composable
fun FilterTabs(
    selectedLevel: String,
    onLevelSelected: (String) -> Unit
) {
    val levels = listOf("All", "A1", "A2", "B1", "B2")

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(vertical = 4.dp)
    ) {
        items(levels) { level ->
            val isSelected = selectedLevel == level
            val bgColor by animateColorAsState(if (isSelected) BluePrimary else Color.White, label = "bg")
            val contentColor by animateColorAsState(if (isSelected) Color.White else GrayText, label = "content")
            val borderColor = if (isSelected) Color.Transparent else Color(0xFFE5E7EB)

            Surface(
                onClick = { onLevelSelected(level) },
                shape = RoundedCornerShape(20.dp),
                color = bgColor,
                border = BorderStroke(1.dp, borderColor),
                modifier = Modifier.height(36.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = level,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = contentColor
                    )
                }
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
                    .background(if (topic.isLocked) Color(0xFFF1F5F9) else BluePrimary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                if (topic.isLocked) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = GrayText.copy(alpha = 0.5f),
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.School,
                        contentDescription = null,
                        tint = BluePrimary,
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
                        color = if (topic.isLocked) GrayText else TextPrimary
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
                            color = if (topic.isLocked) GrayText else SuccessGreen,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = topic.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = GrayText,
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
                            color = if (progressAnimated >= 1.0f) SuccessGreen else BluePrimary,
                            trackColor = Color(0xFFF1F5F9)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "${(progressAnimated * 100).toInt()}%",
                            style = MaterialTheme.typography.labelSmall,
                            color = GrayText,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}