package com.codework.daily_english.presentation.screen.courses

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.codework.daily_english.presentation.components.BaseScreen
import com.codework.daily_english.presentation.components.UiState
import com.codework.daily_english.presentation.model.TopicItem
import com.codework.daily_english.presentation.navigation.Route

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CourseScreen(
    state: UiState<CourseData>,
    onEvent: (CourseEvent) -> Unit,
) {
    BaseScreen(state = state) { data ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F7FA)),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            // 1. HEADER
            item {
                CourseHeaderSection(
                    currentLevel = data.selectedLevel,
                    progress = 0.45f
                )
            }

            // 2. LEVEL SELECTOR
            stickyHeader {
                Surface(
                    color = Color(0xFFF5F7FA),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                ) {
                    Column {
                        Text(
                            text = "Select Level",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                        )
                        LevelSelector(
                            levels = data.availableLevels,
                            selectedLevel = data.selectedLevel,
                            onLevelSelected = {
                                // Gọi Event đổi Level -> ViewModel xử lý
                                onEvent(CourseEvent.OnLevelSelected(it))
                            }
                        )
                    }
                }
            }

            if (data.topics.isEmpty()) {
                item { EmptyCourseState() }
            } else {
                itemsIndexed(data.topics) { index, topic ->
                    Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                        TimelineTopicItem(
                            item = topic,
                            isFirst = index == 0,
                            isLast = index == data.topics.lastIndex,
                            onClick = {
                                onEvent(
                                    CourseEvent.OnNavigateToLesson(
                                        topic.id
                                    )
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

// ... Các Component con (Header, TimelineItem...) Giữ nguyên như cũ ...

// -----------------------------------------------------------
// --- COMPONENT: HEADER ---
// -----------------------------------------------------------
@Composable
fun CourseHeaderSection(currentLevel: String, progress: Float) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .statusBarsPadding()
            .padding(16.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            shape = RoundedCornerShape(32.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                Color(0xFF4FACFE), // Blue
                                Color(0xFF8E2DE2), // Purple
                                Color(0xFFF64F59)  // Soft Pink
                            )
                        )
                    )
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawCircle(
                        color = Color.White.copy(alpha = 0.1f),
                        radius = size.minDimension / 1.5f,
                        center = androidx.compose.ui.geometry.Offset(
                            size.width * 0.9f,
                            size.height * 0.1f
                        )
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Surface(
                            color = Color.White.copy(alpha = 0.2f),
                            shape = CircleShape,
                            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.3f))
                        ) {
                            Text(
                                text = "LEVEL $currentLevel",
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Keep going,\nExplorer!",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            lineHeight = 28.sp
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Bottom
                            ) {
                                Text(
                                    text = "Current Progress",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.White.copy(alpha = 0.8f)
                                )
                                Text(
                                    text = "${(progress * 100).toInt()}%",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            LinearProgressIndicator(
                                progress = { progress },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(CircleShape),
                                color = Color.White,
                                trackColor = Color.White.copy(alpha = 0.2f),
                                strokeCap = StrokeCap.Round
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Box(contentAlignment = Alignment.Center) {
                        Surface(
                            modifier = Modifier.size(80.dp),
                            color = Color.White.copy(alpha = 0.15f),
                            shape = CircleShape
                        ) {}
                        Icon(
                            imageVector = Icons.Default.RocketLaunch,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier
                                .size(50.dp)
                                .graphicsLayer { rotationZ = -45f }
                        )
                    }
                }
            }
        }
    }
}

// -----------------------------------------------------------
// --- COMPONENT: LEVEL SELECTOR ---
// -----------------------------------------------------------
@Composable
fun LevelSelector(
    levels: List<String>,
    selectedLevel: String,
    onLevelSelected: (String) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(levels) { level ->
            val isSelected = level == selectedLevel
            Surface(
                onClick = { onLevelSelected(level) },
                shape = CircleShape,
                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.White,
                border = if (isSelected) null else BorderStroke(1.dp, Color.LightGray),
                shadowElevation = if (isSelected) 4.dp else 0.dp
            ) {
                Text(
                    text = level,
                    style = MaterialTheme.typography.labelLarge,
                    color = if (isSelected) Color.White else Color.Gray,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
                )
            }
        }
    }
}

// -----------------------------------------------------------
// --- COMPONENT: TIMELINE ITEM ---
// -----------------------------------------------------------
@Composable
fun TimelineTopicItem(
    item: TopicItem,
    isFirst: Boolean,
    isLast: Boolean,
    onClick: () -> Unit
) {
    Row(modifier = Modifier.height(IntrinsicSize.Min)) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(32.dp)
        ) {
            if (!isFirst) {
                VerticalDivider(
                    modifier = Modifier.weight(1f),
                    color = if (item.isLocked) Color.LightGray else MaterialTheme.colorScheme.primary,
                    thickness = 2.dp
                )
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }

            Box(
                modifier = Modifier
                    .size(14.dp)
                    .clip(CircleShape)
                    .background(if (item.isLocked) Color.LightGray else MaterialTheme.colorScheme.primary)
                    .border(2.dp, Color.White, CircleShape)
            )

            if (!isLast) {
                VerticalDivider(
                    modifier = Modifier.weight(1f),
                    color = if (item.isLocked) Color.LightGray else MaterialTheme.colorScheme.primary,
                    thickness = 2.dp
                )
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Box(modifier = Modifier
            .padding(bottom = 16.dp)
            .weight(1f)) {
            TopicItemCard(item = item, onClick = onClick)
        }
    }
}

// -----------------------------------------------------------
// --- COMPONENT: TOPIC CARD ---
// -----------------------------------------------------------
@Composable
fun TopicItemCard(
    item: TopicItem,
    onClick: () -> Unit
) {
    val alpha = if (item.isLocked) 0.6f else 1f

    Card(
        onClick = if (item.isLocked) {
            {}
        } else onClick,
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer { this.alpha = alpha },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = if (item.isLocked) 0.dp else 2.dp)
    ) {
        Box {
            Row(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(12.dp))
                ) {
                    AsyncImage(
                        model = item.imageUrl,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    if (item.isLocked) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black.copy(0.4f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Lock, null, tint = Color.White)
                        }
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    if (!item.isLocked) {
                        Surface(
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(4.dp),
                            modifier = Modifier.padding(bottom = 4.dp)
                        ) {
                            Text(
                                text = "VOCAB • GRAMMAR",
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    if (!item.isLocked) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            LinearProgressIndicator(
                                progress = { item.progress },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp)),
                                strokeCap = StrokeCap.Round
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            FilledIconButton(
                                onClick = onClick,
                                modifier = Modifier.size(32.dp),
                                colors = IconButtonDefaults.filledIconButtonColors(containerColor = MaterialTheme.colorScheme.primary)
                            ) {
                                Icon(Icons.Default.PlayArrow, null, modifier = Modifier.size(16.dp))
                            }
                        }
                    } else {
                        Text(
                            "Finish previous lesson",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.Gray
                        )
                    }
                }
            }

            if (!item.isLocked) {
                Surface(
                    color = Color(0xFF4CAF50),
                    shape = RoundedCornerShape(bottomStart = 8.dp),
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Text(
                        text = "FREE",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        fontSize = 10.sp
                    )
                }
            }
        }
    }
}

// -----------------------------------------------------------
// --- COMPONENT: EMPTY STATE ---
// -----------------------------------------------------------
@Composable
fun EmptyCourseState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 60.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.SearchOff,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = Color.LightGray
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No lessons found",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color.Gray
        )
        Text(
            text = "Please check back later!",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.LightGray
        )
    }
}