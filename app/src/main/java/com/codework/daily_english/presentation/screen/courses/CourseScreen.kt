package com.codework.daily_english.presentation.screen.courses

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.codework.daily_english.presentation.components.BaseScreen
import com.codework.daily_english.presentation.components.CurvedHeaderWrapper // Import Base
import com.codework.daily_english.presentation.components.HeaderStatItem    // Import Base
import com.codework.daily_english.core.base.BaseState
import com.codework.daily_english.presentation.screen.courses.model.TopicItem
import com.codework.daily_english.presentation.screen.courses.mvi.CourseEvent
import com.codework.daily_english.presentation.screen.courses.mvi.CourseState
import com.codework.daily_english.presentation.theme.AppBackground
import com.codework.daily_english.presentation.theme.BluePrimary
import com.codework.daily_english.presentation.theme.IndigoPurple
import com.codework.daily_english.presentation.theme.MainGradient

@Composable
fun CourseScreen(
    state: BaseState<CourseState>,
    onEvent: (CourseEvent) -> Unit,
) {
    BaseScreen(state = state) { data ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AppBackground)
        ) {
            // 1. NỀN GRID
            GridBackground()

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 100.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 2. STICKY HEADER (Dùng Base Wrapper)
                stickyHeader {
                    val completedLessons = data.topics.count { it.progress >= 1.0f }
                    val totalLessons = data.topics.size
                    val progressPercentage = if (totalLessons > 0) (completedLessons.toFloat() / totalLessons * 100).toInt() else 0

                    CurvedHeaderWrapper(
                        headerHeight = 260.dp, // Cao hơn để chứa list Level
                        gradientHeight = 220.dp,
                        content = {
                            // Nội dung riêng: Title + List Level
                            Text(
                                text = "Choose your level",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 24.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            LazyRow(
                                contentPadding = PaddingValues(horizontal = 24.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(data.availableLevels) { level ->
                                    LevelIconCard(
                                        levelName = level,
                                        isSelected = level == data.selectedLevel,
                                        onClick = { onEvent(CourseEvent.OnLevelSelected(level)) }
                                    )
                                }
                            }
                        },
                        bottomCardContent = {
                            // Card thống kê: Lessons Done + %
                            HeaderStatItem(
                                value = "$completedLessons/$totalLessons",
                                label = "Lessons Done",
                                icon = Icons.Default.CheckCircle,
                                iconColor = BluePrimary
                            )
                            Divider(
                                modifier = Modifier
                                    .height(40.dp)
                                    .width(1.dp),
                                color = Color.LightGray.copy(alpha = 0.5f)
                            )
                            HeaderStatItem(
                                value = "$progressPercentage%",
                                label = "Completion",
                                icon = Icons.Default.PieChart,
                                iconColor = IndigoPurple
                            )
                        }
                    )
                }

                item { Spacer(modifier = Modifier.height(32.dp)) }

                if (data.topics.isEmpty()) {
                    item { EmptyCourseState() }
                } else {
                    // 3. DANH SÁCH BÀI HỌC (Giữ nguyên)
                    itemsIndexed(data.topics) { index, topic ->
                        val amplitude = 85.dp
                        val xOffset = when (index % 2) { 0 -> -amplitude / 2; 1 -> amplitude / 2; else -> 0.dp }
                        val nextIndex = index + 1
                        val nextXOffset = if (nextIndex < data.topics.size) { when (nextIndex % 2) { 0 -> -amplitude / 2; 1 -> amplitude / 2; else -> 0.dp } } else xOffset

                        GamifiedLessonItem(
                            item = topic,
                            xOffset = xOffset,
                            nextXOffset = nextXOffset,
                            isLast = index == data.topics.lastIndex,
                            onClick = { if (!topic.isLocked) onEvent(CourseEvent.OnNavigateToLesson(topic.id)) }
                        )
                    }
                }
            }
        }
    }
}

// ... (Các Component con GridBackground, GamifiedLessonItem, LevelIconCard, LessonNode... giữ nguyên y hệt file cũ)
@Composable
fun GridBackground() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val gridSize = 24.dp.toPx()
        val gridColor = Color.Gray.copy(alpha = 0.1f)
        val strokeWidth = 1.dp.toPx()
        var x = 0f
        while (x < canvasWidth) {
            drawLine(color = gridColor, start = Offset(x, 0f), end = Offset(x, canvasHeight), strokeWidth = strokeWidth)
            x += gridSize
        }
        var y = 0f
        while (y < canvasHeight) {
            drawLine(color = gridColor, start = Offset(0f, y), end = Offset(canvasWidth, y), strokeWidth = strokeWidth)
            y += gridSize
        }
    }
}

@Composable
fun GamifiedLessonItem(item: TopicItem, xOffset: Dp, nextXOffset: Dp, isLast: Boolean, onClick: () -> Unit) {
    val nodeSize = 110.dp
    val itemHeight = 220.dp
    val inactivePathColor = Color(0xFFCFD8DC)
    val isCompleted = item.progress >= 1.0f

    Box(modifier = Modifier.fillMaxWidth().height(itemHeight), contentAlignment = Alignment.TopCenter) {
        if (!isLast) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val startX = size.width / 2 + xOffset.toPx()
                val startY = nodeSize.toPx() / 2 + 10.dp.toPx()
                val endX = size.width / 2 + nextXOffset.toPx()
                val endY = startY + itemHeight.toPx()
                val path = Path().apply {
                    moveTo(startX, startY)
                    val cpY1 = startY + (endY - startY) * 0.7f
                    val cpY2 = endY - (endY - startY) * 0.7f
                    cubicTo(startX, cpY1, endX, cpY2, endX, endY)
                }
                val outerPathColor = if (isCompleted) BluePrimary.copy(alpha = 0.3f) else Color.White
                drawPath(path = path, color = outerPathColor, style = Stroke(width = 24f, cap = StrokeCap.Round))
                if (isCompleted) {
                    drawPath(path = path, brush = MainGradient, style = Stroke(width = 10f, cap = StrokeCap.Round))
                } else {
                    drawPath(path = path, color = inactivePathColor, style = Stroke(width = 6f, cap = StrokeCap.Round, pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 20f), 0f)))
                }
            }
        }
        Box(modifier = Modifier.offset(x = xOffset)) { LessonNode(item = item, nodeSize = nodeSize, onClick = onClick) }
    }
}

@Composable
fun LevelIconCard(levelName: String, isSelected: Boolean, onClick: () -> Unit) {
    val iconEmoji = when {
        levelName.contains("A1") -> "🌱"
        levelName.contains("A2") -> "🌿"
        levelName.contains("B1") -> "🌳"
        else -> "🎓"
    }
    val backgroundColor by animateColorAsState(if (isSelected) Color.White else Color.White.copy(alpha = 0.2f), label = "")
    val contentColor by animateColorAsState(if (isSelected) BluePrimary else Color.White, label = "")
    Surface(
        shape = RoundedCornerShape(10.dp), color = backgroundColor, shadowElevation = if (isSelected) 4.dp else 0.dp,
        modifier = Modifier.height(50.dp).clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) { onClick() }
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
            Text(text = iconEmoji, fontSize = 20.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = levelName, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = contentColor)
            if (isSelected) {
                Spacer(modifier = Modifier.width(8.dp))
                Icon(Icons.Default.Check, null, tint = BluePrimary, modifier = Modifier.size(16.dp))
            }
        }
    }
}

@Composable
fun LessonNode(item: TopicItem, nodeSize: Dp, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(nodeSize).shadow(if (item.isLocked) 0.dp else 12.dp, CircleShape)
                .clip(CircleShape).background(if (item.isLocked) Color(0xFFECEFF1) else Color.White)
                .clickable(enabled = !item.isLocked) { onClick() }
        ) {
            if (!item.isLocked) {
                AsyncImage(model = item.imageUrl, contentDescription = null, modifier = Modifier.fillMaxSize().padding(8.dp).clip(CircleShape), contentScale = ContentScale.Crop, colorFilter = if (item.progress >= 1.0f) androidx.compose.ui.graphics.ColorFilter.tint(Color.Black.copy(alpha = 0.1f), androidx.compose.ui.graphics.BlendMode.Darken) else null)
            }
            if (!item.isLocked && item.progress < 1.0f && item.progress > 0f) {
                Canvas(modifier = Modifier.fillMaxSize().padding(3.dp)) {
                    val strokeWidthPx = 6.dp.toPx()
                    drawArc(color = Color(0xFFECEFF1), startAngle = -90f, sweepAngle = 360f, useCenter = false, style = Stroke(width = strokeWidthPx, cap = StrokeCap.Round))
                    drawArc(brush = MainGradient, startAngle = -90f, sweepAngle = 360f * item.progress, useCenter = false, style = Stroke(width = strokeWidthPx, cap = StrokeCap.Round))
                }
            }
            if (item.progress >= 1.0f) {
                Box(modifier = Modifier.fillMaxSize().padding(8.dp).clip(CircleShape), contentAlignment = Alignment.Center) { Icon(Icons.Default.Check, null, tint = BluePrimary, modifier = Modifier.size(48.dp)) }
                Box(modifier = Modifier.fillMaxSize().border(BorderStroke(6.dp, MainGradient), shape = CircleShape))
            }
            if (item.isLocked) {
                Box(modifier = Modifier.fillMaxSize().background(Color(0xFFECEFF1)), contentAlignment = Alignment.Center) { Icon(Icons.Default.Lock, null, tint = Color(0xFFB0BEC5), modifier = Modifier.size(40.dp)) }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        if (item.progress > 0) {
            Row {
                repeat(3) { i ->
                    val starColor = if (item.progress >= (i + 1) * 0.33f) Color(0xFFFFD54F) else Color(0xFFCFD8DC)
                    Icon(Icons.Default.Star, null, tint = starColor, modifier = Modifier.size(18.dp))
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
        }
        Surface(
            color = if (item.isLocked) Color.Transparent else Color.White,
            shape = RoundedCornerShape(12.dp), shadowElevation = if (item.isLocked) 0.dp else 2.dp,
            border = if (item.isLocked) null else BorderStroke(1.dp, Color(0xFFECEFF1))
        ) {
            Text(text = item.title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = if (item.isLocked) Color.Gray else Color(0xFF263238), modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp), maxLines = 1)
        }
    }
}

@Composable
fun EmptyCourseState() {
    Column(modifier = Modifier.fillMaxWidth().padding(top = 100.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(Icons.Default.SearchOff, null, modifier = Modifier.size(80.dp), tint = Color(0xFFCFD8DC))
        Text(text = "No lessons available", color = Color.Gray)
    }
}