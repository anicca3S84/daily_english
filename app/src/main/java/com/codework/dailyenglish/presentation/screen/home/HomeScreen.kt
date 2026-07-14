package com.codework.dailyenglish.presentation.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codework.dailyenglish.R
import com.codework.dailyenglish.presentation.components.StateWrapper
import com.codework.dailyenglish.presentation.screen.home.components.WeeklyProgressCard
import com.codework.dailyenglish.presentation.screen.home.components.HomeHeader
import com.codework.dailyenglish.presentation.screen.home.model.QuickActionItem
import com.codework.dailyenglish.presentation.screen.home.mvi.HomeEvent
import com.codework.dailyenglish.presentation.screen.home.mvi.HomeState
import com.codework.dailyenglish.presentation.screen.subscription.SubscriptionPopup

@Composable
fun HomeScreen(
    state: HomeState,
    onEvent: (HomeEvent) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        onEvent(HomeEvent.OnEnterHomeScreen)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        // Đổi màu nền Scaffold thành xám nhạt để các thẻ màu trắng nổi bật hơn
        containerColor = Color(0xFFF8FAFC),
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding())
        ) {
            HomeContent(
                state = state,
                onEvent = onEvent,
                snackbarHostState = snackbarHostState
            )

            SubscriptionPopup(
                visible = state.showSubscriptionPopup,
                onDismiss = { onEvent(HomeEvent.OnDismissSubscriptionPopup) }
            )
        }
    }
}

@Composable
fun HomeContent(
    state: HomeState,
    onEvent: (HomeEvent) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // 1. Thẻ thông tin tài khoản người dùng
        item {
            HomeHeader(
                name = state.userName,
                avatarUrl = state.avatarUrl,
                isPremium = state.isPremium,
                onPremiumClick  = { onEvent(HomeEvent.OnPremiumCrownClick) }
            )
        }

        // 2. Banner tiến trình tuần "Keep it up! 💪"
        item {
            // Lấy tổng số phút trực tiếp từ data của BaseUiState, mặc định là 0 nếu chưa có dữ liệu
            val totalMinutes = state.weeklyStatsState.data?.sumOf { it.minutes } ?: 0

            WeeklyGoalBanner(
                totalTimeMinutes = totalMinutes,
                onViewProgressClick = { onEvent(HomeEvent.OnPremiumCrownClick) }
            )
        }

        // 3. Mục Quick Access thiết kế mới
        item {
            val actionList = listOf(
                QuickActionItem(
                    icon = R.drawable.ic_launcher_foreground,
                    label = "Anicca AI",
                    color = Color(0xFFE91E63),
                    onClick = { onEvent(HomeEvent.OnQuickActionGemini) }
                ),
                QuickActionItem(
                    icon = R.drawable.ic_launcher_foreground,
                    label = "Dictionary",
                    color = Color(0xFF2196F3),
                    onClick = { onEvent(HomeEvent.OnQuickActionDictionary) }
                ),
                QuickActionItem(
                    icon = R.drawable.ic_launcher_foreground,
                    label = "Saved Word",
                    color = Color(0xFFFF9800),
                    onClick = { onEvent(HomeEvent.OnQuickActionSavedWord) }
                ),
                QuickActionItem(
                    icon = R.drawable.ic_launcher_foreground,
                    label = "Translate",
                    color = Color(0xFF4CAF50),
                    onClick = { onEvent(HomeEvent.OnQuickActionTranslate) }
                )
            )
            QuickActionSection(actions = actionList)
        }

        // 4. Thống kê tiến trình học tập
        item {
            Text(
                text = "Learning Statistics",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E293B)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            StateWrapper(
                uiState = state.weeklyStatsState,
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 200.dp),
                snackbarHostState = snackbarHostState,
                onRetry = { onEvent(HomeEvent.OnRetry) }
            ) { data ->
                WeeklyProgressCard(weeklyStats = data ?: emptyList())
            }
        }
    }
}

@Composable
fun WeeklyGoalBanner(
    totalTimeMinutes: Int,
    modifier: Modifier = Modifier,
    onViewProgressClick: () -> Unit = {}
) {
    // 1. Logic định dạng thời gian dạng "X h Y phút" hoặc "X phút"
    val displayTime = if (totalTimeMinutes >= 60) {
        val hours = totalTimeMinutes / 60
        val minutes = totalTimeMinutes % 60
        if (minutes > 0) {
            "$hours h $minutes phút"
        } else {
            "$hours h"
        }
    } else {
        "$totalTimeMinutes phút"
    }

    // 2. Tự động giảm kích thước chữ nếu chuỗi hiển thị quá dài để không bị tràn vòng tròn
    val timeFontSize = if (displayTime.length > 7) 13.sp else 20.sp

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF4FA4FF),
                            Color(0xFF8B5CF6),
                            Color(0xFFD946EF)
                        )
                    )
                )
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1.5f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Keep it up!",
                            color = Color.White,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "💪", fontSize = 20.sp)
                    }

                    Text(
                        text = "You're doing great this week!",
                        color = Color.White.copy(alpha = 0.9f),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                // Vòng tròn tiến trình hiển thị thời gian học
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(100.dp)
                        .padding(4.dp)
                ) {
                    CircularProgressIndicator(
                        progress = { 1f },
                        color = Color.White.copy(alpha = 0.2f),
                        strokeWidth = 8.dp,
                        modifier = Modifier.fillMaxSize()
                    )

                    val progressValue = (totalTimeMinutes.toFloat() / 120f).coerceIn(0f, 1f) // Giả định mục tiêu tuần là 120 phút
                    CircularProgressIndicator(
                        progress = { if (progressValue > 0f) progressValue else 0.15f },
                        color = Color.White,
                        strokeWidth = 8.dp,
                        modifier = Modifier.fillMaxSize()
                    )

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(horizontal = 6.dp)
                    ) {
                        Text(
                            text = displayTime,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = timeFontSize, // Áp dụng cỡ chữ động chống tràn
                            maxLines = 1
                        )
                        Text(
                            text = "This Week",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun QuickActionSection(
    actions: List<QuickActionItem>,
    onSeeAllClick: () -> Unit = {}
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Quick Access",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E293B)
                )
            )
            TextButton(
                onClick = onSeeAllClick,
                contentPadding = PaddingValues(0.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "See All",
                        color = Color(0xFF6366F1),
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = null,
                        tint = Color(0xFF6366F1),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        // Grid 2x2
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (actions.isNotEmpty()) {
                    QuickAccessCard(
                        item = actions[0],
                        backgroundColor = Color(0xFFFFF2F5),
                        circleColor = Color(0xFFFFE3E8),
                        iconBgColor = Color(0xFFFFD1DC),
                        iconColor = Color(0xFFE91E63),
                        arrowBgColor = Color(0xFFFFE2E8),
                        modifier = Modifier.weight(1f)
                    )
                }
                if (actions.size > 1) {
                    QuickAccessCard(
                        item = actions[1],
                        backgroundColor = Color(0xFFF0F7FF),
                        circleColor = Color(0xFFE0EFFE),
                        iconBgColor = Color(0xFFD4E6FC),
                        iconColor = Color(0xFF2196F3),
                        arrowBgColor = Color(0xFFE3F2FD),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (actions.size > 2) {
                    QuickAccessCard(
                        item = actions[2],
                        backgroundColor = Color(0xFFFFF9EE),
                        circleColor = Color(0xFFFFF2D5),
                        iconBgColor = Color(0xFFFFE8C3),
                        iconColor = Color(0xFFFF9800),
                        arrowBgColor = Color(0xFFFFF3E0),
                        modifier = Modifier.weight(1f)
                    )
                }
                if (actions.size > 3) {
                    QuickAccessCard(
                        item = actions[3],
                        backgroundColor = Color(0xFFF1FBF3),
                        circleColor = Color(0xFFE2F6E7),
                        iconBgColor = Color(0xFFD0F1D8),
                        iconColor = Color(0xFF4CAF50),
                        arrowBgColor = Color(0xFFE8F5E9),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
fun QuickAccessCard(
    item: QuickActionItem,
    backgroundColor: Color,
    circleColor: Color,
    iconBgColor: Color,
    iconColor: Color,
    arrowBgColor: Color,
    modifier: Modifier = Modifier
) {
    // Tự động phân chia mô tả dựa trên nhãn nút để không làm thay đổi lớp Model của bạn
    val description = when (item.label) {
        "Anicca AI" -> "You'r smart learning assistant"
        "Dictionary" -> "Find word meanings"
        "Saved Word" -> "Review your saved words"
        "Translate" -> "Translate any text"
        else -> ""
    }

    // Tự động gán Vector đẹp mắt thay cho ic_launcher mặc định nếu cần
    val iconVector = when (item.label) {
        "Anicca AI" -> Icons.Default.Face
        "Dictionary" -> Icons.Default.Book
        "Saved Word" -> Icons.Default.Bookmark
        "Translate" -> Icons.Default.Translate
        else -> null
    }

    Card(
        onClick = item.onClick,
        modifier = modifier
            .height(160.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(24.dp))
        ) {
            // Vòng tròn trang trí mờ ở góc phải trên cùng
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .offset(x = 40.dp, y = (-20).dp)
                    .background(circleColor, shape = androidx.compose.foundation.shape.CircleShape)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Hộp Icon
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(iconBgColor, shape = RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    if (iconVector != null) {
                        Icon(
                            imageVector = iconVector,
                            contentDescription = item.label,
                            tint = iconColor,
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Icon(
                            painter = painterResource(id = item.icon),
                            contentDescription = item.label,
                            tint = iconColor,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                // Tiêu đề & Nút bấm dưới chân thẻ
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = item.label,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1E293B)
                            )
                        )
                        Text(
                            text = description,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color(0xFF64748B),
                                fontSize = 11.sp
                            ),
                            maxLines = 2
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .background(arrowBgColor, shape = androidx.compose.foundation.shape.CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowRight,
                            contentDescription = null,
                            tint = iconColor,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}