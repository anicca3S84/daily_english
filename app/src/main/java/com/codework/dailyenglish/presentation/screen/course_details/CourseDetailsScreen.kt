package com.codework.dailyenglish.presentation.screen.course_details

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.codework.dailyenglish.R
import com.codework.dailyenglish.core.navigation.Route
import com.codework.dailyenglish.presentation.components.LoadingType
import com.codework.dailyenglish.presentation.components.StateWrapper
import com.codework.dailyenglish.presentation.screen.course_details.model.LessonStatus
import com.codework.dailyenglish.presentation.screen.course_details.mvi.CourseDetailEvent
import com.codework.dailyenglish.presentation.screen.course_details.mvi.CourseDetailState
import com.codework.dailyenglish.presentation.screen.lessons.lesson_preview.LessonPreviewPopup

// --- Định nghĩa Bảng màu hiện đại (Tailwind Style) ---
private val BgColor = Color(0xFFF8FAFC)
private val TextMain = Color(0xFF1E293B)
private val TextSub = Color(0xFF64748B)
private val BlueMain = Color(0xFF3B82F6)
private val OrangeMain = Color(0xFFF97316)
private val PurpleMain = Color(0xFFA855F7)
private val GreenMain = Color(0xFF22C55E)
private val GrayBorder = Color(0xFFF1F5F9)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseDetailsScreen(
    state: CourseDetailState,
    onEvent: (CourseDetailEvent) -> Unit,
    onBackClick: () -> Unit,
    navController: NavController
) {
    val snackBar = remember { SnackbarHostState() }
    var showPreview by remember { mutableStateOf(false) }
    var selectedLessonId by remember { mutableStateOf("") }
    var expandedModuleIndex by remember { mutableIntStateOf(0) } // Quản lý module nào đang mở

    Scaffold(
        snackbarHost = { SnackbarHost(snackBar) },
        containerColor = BgColor,
        topBar = {
            // 1. STICKY TOP BAR
            TopAppBar(
                title = {
                    Text(
                        text = "Chi tiết khóa học",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = TextMain
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextMain)
                    }
                },
                actions = {
                    IconButton(onClick = { /* Handle More */ }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "More", tint = TextMain)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White.copy(alpha = 0.9f)
                )
            )
        },

    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            StateWrapper(
                uiState = state.modulesState,
                modifier = Modifier.fillMaxSize(),
                snackbarHostState = snackBar,
                enablePullToRefresh = true,
                loadingType = LoadingType.CIRCULAR,
                onRetry = { onEvent(CourseDetailEvent.OnRefresh) }
            ) { modules ->
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 80.dp) // Tránh đè lên bottom bar
                ) {
                    // 2. HERO SECTION
                    item {
                        CourseHeroSection(state = state)
                    }

                    // 3. COURSE INFO CHIPS
                    item {
                        CourseStatsSection(state = state)
                    }

                    // 4. DESCRIPTION
                    item {
                        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 24.dp)) {
                            Text(
                                text = "Giới thiệu khóa học",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = TextMain,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            Text(
                                text = "Khóa học này cung cấp các nền tảng quan trọng nhất để bạn bắt đầu giao tiếp tự tin trong môi trường quốc tế. Bắt đầu hành trình chinh phục tiếng Anh ngay hôm nay!",
                                fontSize = 14.sp,
                                color = TextSub,
                                lineHeight = 22.sp
                            )
                        }
                    }

                    // 5. MODULES LIST (ACCORDION)
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Nội dung khóa học",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = TextMain
                            )
                            Surface(
                                color = BlueMain.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "Hoàn thành ${(state.overallProgress * 100).toInt()}%",
                                    color = BlueMain,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }

                    modules?.forEachIndexed { index, module ->
                        item {
                            ModuleAccordionItem(
                                title = module.title,
                                description = module.description,
                                lessonsCount = module.lessons.size,
                                isExpanded = expandedModuleIndex == index,
                                index = index,
                                onToggle = {
                                    expandedModuleIndex = if (expandedModuleIndex == index) -1 else index
                                }
                            ) {
                                // Danh sách bài học bên trong Module
                                Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)) {
                                    HorizontalDivider(color = GrayBorder, modifier = Modifier.padding(bottom = 8.dp))

                                    module.lessons.forEach { lesson ->
                                        LessonAccordionItem(
                                            lesson = lesson,
                                            onClick = {
                                                if (lesson.status != LessonStatus.LOCKED) {
                                                    selectedLessonId = lesson.id
                                                    showPreview = true
                                                } else {
                                                    onEvent(CourseDetailEvent.OnLessonClick(lesson.id, true))
                                                }
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Popup xem trước bài học
                val currentLesson = modules?.flatMap { it.lessons }?.find { it.id == selectedLessonId }
                if (showPreview && currentLesson != null) {
                    LessonPreviewPopup(
                        lessonId = selectedLessonId,
                        visible = true,
                        onDismiss = { showPreview = false },
                        onNavigateToLearn = { lessonId ->
                            showPreview = false
                            navController.navigate(Route.LessonScreen(lessonId = lessonId))
                        },
                        courseTheme = state.courseTheme
                    )
                }
            }
        }
    }
}

@Composable
fun CourseHeroSection(state: CourseDetailState) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .height(220.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(state.courseTheme.primaryColor) // Dùng màu theme hoặc thay bằng ảnh thật
    ) {
        // Overlay Gradient để chữ dễ đọc
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f)),
                        startY = 100f
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            Surface(
                color = BlueMain,
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "BEST SELLER",
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = state.courseTitle,
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                lineHeight = 30.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun CourseStatsSection(state: CourseDetailState) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatChip(
            modifier = Modifier.weight(1f),
            icon = Icons.Default.Schedule,
            iconColor = BlueMain,
            title = "Thời lượng",
            value = "12 giờ" // Có thể lấy từ state sau
        )
        StatChip(
            modifier = Modifier.weight(1f),
            icon = Icons.Default.BarChart,
            iconColor = OrangeMain,
            title = "Trình độ",
            value = "Cơ bản"
        )
        StatChip(
            modifier = Modifier.weight(1f),
            icon = Icons.Default.WorkspacePremium,
            iconColor = PurpleMain,
            title = "Bài học",
            value = "${state.totalLessons} bài"
        )
    }
}

@Composable
fun StatChip(modifier: Modifier, icon: ImageVector, iconColor: Color, title: String, value: String) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        border = BorderStroke(1.dp, GrayBorder),
        shadowElevation = 1.dp
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = title, fontSize = 10.sp, color = TextSub, fontWeight = FontWeight.Medium)
            Text(text = value, fontSize = 12.sp, color = TextMain, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun ModuleAccordionItem(
    title: String,
    description: String,
    lessonsCount: Int,
    isExpanded: Boolean,
    index: Int,
    onToggle: () -> Unit,
    content: @Composable () -> Unit
) {
    val rotation by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        animationSpec = tween(300),
        label = "arrowRotation"
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        border = BorderStroke(1.dp, if (isExpanded) BlueMain.copy(alpha = 0.3f) else GrayBorder),
        shadowElevation = if (isExpanded) 4.dp else 0.dp
    ) {
        Column {
            // Header bấm để mở
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggle() }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Số thứ tự Module
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isExpanded) BlueMain else BgColor),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${index + 1}",
                        fontWeight = FontWeight.Bold,
                        color = if (isExpanded) Color.White else TextSub
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = TextMain
                    )
                    Text(
                        text = "$lessonsCount bài học",
                        fontSize = 12.sp,
                        color = TextSub,
                        fontWeight = FontWeight.Medium
                    )
                }

                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Toggle",
                    tint = if (isExpanded) BlueMain else TextSub,
                    modifier = Modifier.rotate(rotation)
                )
            }

            // Nội dung xổ xuống
            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically(animationSpec = tween(300)),
                exit = shrinkVertically(animationSpec = tween(300))
            ) {
                content()
            }
        }
    }
}

@Composable
fun LessonAccordionItem(
    lesson: com.codework.dailyenglish.presentation.screen.course_details.model.LessonUi,
    onClick: () -> Unit
) {
    val isLocked = lesson.status == LessonStatus.LOCKED
    val isCompleted = lesson.status == LessonStatus.COMPLETED

    // Tự động chọn Icon dựa theo kỹ năng đầu tiên (có thể tùy biến thêm)
    val firstSkill = lesson.skills.firstOrNull()?.type?.name ?: "UNKNOWN"
    val iconData = when {
        firstSkill.contains("VOCAB", ignoreCase = true) -> Pair(Icons.Default.Bookmark, OrangeMain)
        firstSkill.contains("THEORY", ignoreCase = true) -> Pair(Icons.Default.MenuBook, BlueMain)
        firstSkill.contains("QUESTION", ignoreCase = true) -> Pair(Icons.Default.HelpOutline, PurpleMain)
        else -> Pair(Icons.Default.PlayCircleOutline, TextSub)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(enabled = true, onClick = onClick)
            .background(if (lesson.status != LessonStatus.LOCKED && !isCompleted) BlueMain.copy(alpha = 0.05f) else Color.Transparent)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon bài học
        Surface(
            modifier = Modifier.size(36.dp),
            shape = RoundedCornerShape(10.dp),
            color = Color.White,
            border = BorderStroke(1.dp, GrayBorder)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = iconData.first,
                    contentDescription = null,
                    tint = if (isLocked) Color.LightGray else iconData.second,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Thông tin bài học
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = lesson.title,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = if (isLocked) TextSub.copy(alpha = 0.6f) else TextMain
            )
            Text(
                text = "Bài học • ${lesson.expReward} XP",
                fontSize = 10.sp,
                color = TextSub,
                fontWeight = FontWeight.Medium
            )
        }

        // Icon trạng thái (Khóa, Hoàn thành)
        Box(modifier = Modifier.size(24.dp), contentAlignment = Alignment.Center) {
            when {
                isCompleted -> Icon(Icons.Default.CheckCircle, contentDescription = "Done", tint = GreenMain, modifier = Modifier.size(20.dp))
                isLocked -> Icon(Icons.Default.Lock, contentDescription = "Locked", tint = Color.LightGray, modifier = Modifier.size(18.dp))
                else -> CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp, color = BlueMain)
            }
        }
    }
}