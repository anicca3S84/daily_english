package com.codework.daily_english.presentation.screen.lessons.lesson
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.codework.daily_english.presentation.components.AppTopBar
import com.codework.daily_english.presentation.components.BaseScreen
import com.codework.daily_english.presentation.components.PrimaryButton
import com.codework.daily_english.core.base.BaseState
import com.codework.daily_english.presentation.screen.lessons.lesson.model.LessonStep
import com.codework.daily_english.presentation.screen.lessons.lesson.model.LessonStepType
import com.codework.daily_english.presentation.screen.lessons.lesson.model.StepStatus
import com.codework.daily_english.presentation.screen.lessons.lesson.mvi.LessonEvent
import com.codework.daily_english.presentation.screen.lessons.lesson.mvi.LessonState
import com.codework.daily_english.presentation.theme.BluePrimary
import com.codework.daily_english.presentation.theme.GrayText
import com.codework.daily_english.presentation.theme.MainGradient
import com.codework.daily_english.presentation.theme.ReviewGradient
import com.codework.daily_english.presentation.theme.SuccessGreen

// ĐÃ XÓA CÁC ĐỊNH NGHĨA ENUM VÀ DATA CLASS Ở ĐÂY VÌ ĐÃ CHUYỂN SANG CONTRACT

@Composable
fun LessonScreen(
    state: BaseState<LessonState>, // SỬA LessonData -> LessonState
    onEvent: (LessonEvent) -> Unit,
    onBackClick: () -> Unit,
) {
    val lessonData = (state as? BaseState.Success)?.data
    val steps = lessonData?.steps ?: emptyList()

    val completedCount = steps.count { it.status == StepStatus.COMPLETED }
    val totalCount = steps.size

    val bottomButtonText = when {
        completedCount == 0 -> "Start lesson"
        completedCount == totalCount -> "Review lesson"
        else -> "Continue learning"
    }

    val buttonBackground = if (completedCount == totalCount && totalCount > 0) {
        ReviewGradient
    } else {
        MainGradient
    }

    Scaffold(
        containerColor = Color(0xFFF9FAFB),
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            AppTopBar(
                onBackClick = onBackClick,
                titleContent = {
                    Text(
                        text = "Unit 1",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F2937)
                    )
                },
                onMenuClick = null,
            )
        },
        bottomBar = {
            if (state is BaseState.Success) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, bottom = 32.dp, top = 8.dp)
                ) {
                    PrimaryButton(
                        text = bottomButtonText,
                        onClick = { onEvent(LessonEvent.OnStartPractice) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(12.dp, CircleShape, spotColor = BluePrimary.copy(alpha = 0.4f)),
                        brush = buttonBackground
                    )
                }
            }
        }
    ) { paddingValues ->
        BaseScreen(
            state = state,
            onRetry = { onEvent(LessonEvent.OnRetry) } // Bổ sung onRetry cho BaseScreen
        ) { data ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                LessonHeaderCoverSection(
                    imageUrl = data.imageUrl,
                    title = data.title,
                    description = data.description,
                    completedSteps = data.steps.count { it.status == StepStatus.COMPLETED },
                    totalSteps = data.steps.size
                )

                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .background(Color(0xFFF9FAFB))
                ) {
                    item { Spacer(modifier = Modifier.height(16.dp)) }

                    item {
                        Text(
                            text = "Contents",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1F2937),
                            modifier = Modifier.padding(horizontal = 24.dp)
                        )
                    }

                    item { Spacer(modifier = Modifier.height(12.dp)) }

                    itemsIndexed(data.steps) { index, step ->
                        LessonPathItemModern(
                            step = step,
                            isLast = index == data.steps.lastIndex,
                            onClick = { onEvent(LessonEvent.OnStepClick(step.id)) }
                        )
                    }

                    item { Spacer(modifier = Modifier.height(120.dp)) }
                }
            }
        }
    }
}

@Composable
fun LessonHeaderCoverSection(
    imageUrl: String,
    title: String,
    description: String,
    completedSteps: Int,
    totalSteps: Int
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .background(MainGradient)
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Surface(
                    shape = CircleShape,
                    color = Color.White.copy(alpha = 0.2f),
                    modifier = Modifier.size(56.dp),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.3f))
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(text = "☕", fontSize = 28.sp)
                    }
                }

                Surface(
                    color = Color.Black.copy(alpha = 0.6f),
                    shape = RoundedCornerShape(50),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.2f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            if (completedSteps == totalSteps) Icons.Default.CheckCircle else Icons.Default.TrendingUp,
                            null,
                            modifier = Modifier.size(14.dp),
                            tint = if (completedSteps == totalSteps) SuccessGreen else Color.White
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "$completedSteps/$totalSteps",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = if (completedSteps == totalSteps) SuccessGreen else Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Black,
                color = Color.White,
                lineHeight = 32.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.9f),
                lineHeight = 24.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun LessonPathItemModern(
    step: LessonStep,
    isLast: Boolean,
    onClick: () -> Unit
) {
    val isActive = step.status == StepStatus.UNLOCKED
    val isCompleted = step.status == StepStatus.COMPLETED
    val isLocked = step.status == StepStatus.LOCKED

    val mainColor = when {
        isCompleted -> SuccessGreen
        isActive -> BluePrimary
        else -> Color(0xFFD1D5DB)
    }

    val iconVector = when (step.type) {
        LessonStepType.VOCABULARY -> Icons.Default.Style
        LessonStepType.LISTENING -> Icons.Default.Headphones
        LessonStepType.SENTENCE_BUILDER -> Icons.Default.Build
        LessonStepType.SPEAKING_AI -> Icons.Default.Mic
        LessonStepType.QUIZ -> Icons.Default.EmojiEvents
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .clickable(enabled = !isLocked) { onClick() }
            .padding(horizontal = 24.dp)
    ) {
        // CỘT 1: TIMELINE
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(40.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                if (isActive) {
                    Box(modifier = Modifier.size(40.dp).background(BluePrimary.copy(alpha = 0.2f), CircleShape))
                }

                Surface(
                    shape = CircleShape,
                    color = if (isLocked) Color.White else mainColor,
                    border = if (isLocked) BorderStroke(2.dp, mainColor) else null,
                    modifier = Modifier.size(32.dp),
                    shadowElevation = if (isActive) 4.dp else 0.dp
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        if (isCompleted) {
                            Icon(Icons.Default.Check, null, tint = Color.White, modifier = Modifier.size(16.dp))
                        } else {
                            Icon(iconVector, null, tint = if (isLocked) mainColor else Color.White, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .fillMaxHeight()
                        .padding(top = 4.dp, bottom = 4.dp)
                        .background(if (isCompleted) SuccessGreen else Color(0xFFE5E7EB))
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        // CỘT 2: CONTENT
        Box(
            modifier = Modifier
                .padding(bottom = 24.dp)
                .weight(1f)
        ) {
            // CASE 1: ĐANG HỌC
            if (isActive) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                    border = BorderStroke(1.dp, BluePrimary.copy(alpha = 0.5f)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = step.type.name.replace("_", " "),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = BluePrimary
                        )
                        // SỬA ĐỔI QUAN TRỌNG: Giảm khoảng cách từ 6.dp xuống 2.dp
                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = step.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1F2937)
                        )
                        Text(
                            text = step.subtitle,
                            style = MaterialTheme.typography.bodyMedium,
                            color = GrayText
                        )
                    }
                }
            }
            // CASE 2: ĐÃ XONG
            else if (isCompleted) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
                    border = BorderStroke(1.dp, SuccessGreen.copy(alpha = 0.3f)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = step.title,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1F2937).copy(alpha = 0.8f)
                            )
                            Text(
                                text = step.subtitle,
                                style = MaterialTheme.typography.bodySmall,
                                color = GrayText,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Completed",
                                style = MaterialTheme.typography.labelSmall,
                                color = SuccessGreen,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        // Nút Replay
                        Surface(
                            shape = CircleShape,
                            color = Color.White,
                            modifier = Modifier.size(32.dp),
                            border = BorderStroke(1.dp, SuccessGreen.copy(alpha = 0.3f))
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    Icons.Default.Refresh,
                                    contentDescription = "Review",
                                    tint = SuccessGreen,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }
            // CASE 3: KHÓA
            else {
                Column(modifier = Modifier.padding(top = 4.dp)) {
                    Text(
                        text = step.type.name.replace("_", " "),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray
                    )
                    // SỬA ĐỔI: Giảm khoảng cách cho đồng bộ
                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = step.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray
                    )
                    Text(
                        text = step.subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFFD1D5DB),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}