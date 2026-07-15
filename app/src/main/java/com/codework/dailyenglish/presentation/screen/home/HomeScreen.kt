package com.codework.dailyenglish.presentation.screen.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.codework.dailyenglish.R
import com.codework.dailyenglish.presentation.components.StateWrapper
import com.codework.dailyenglish.presentation.screen.home.components.WeeklyProgressCard
import com.codework.dailyenglish.presentation.screen.home.components.HomeHeader
import com.codework.dailyenglish.presentation.screen.home.components.QuickActionSection
import com.codework.dailyenglish.presentation.screen.home.components.WeeklyGoalBanner
import com.codework.dailyenglish.presentation.screen.home.model.QuickActionItem
import com.codework.dailyenglish.presentation.screen.home.mvi.HomeEvent
import com.codework.dailyenglish.presentation.screen.home.mvi.HomeState
import com.codework.dailyenglish.presentation.screen.subscription.SubscriptionPopup
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import com.codework.dailyenglish.presentation.components.LoadingType


@OptIn(ExperimentalMaterial3Api::class)
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
        containerColor = Color(0xFFF8FAFC),
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding())
        ) {
            // Tích hợp PullToRefreshBox để người dùng vuốt kéo tải lại toàn bộ thông tin màn hình Home
            PullToRefreshBox(
                isRefreshing = state.weeklyStatsState.isRefreshing,
                onRefresh = { onEvent(HomeEvent.OnRetry) },
                modifier = Modifier.fillMaxSize()
            ) {
                HomeContent(
                    state = state,
                    onEvent = onEvent,
                    snackbarHostState = snackbarHostState
                )
            }

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
        item {
            HomeHeader(
                name = state.userName,
                avatarUrl = state.avatarUrl,
                isPremium = state.isPremium,
                onPremiumClick  = { onEvent(HomeEvent.OnPremiumCrownClick) }
            )
        }

        item {
            val totalMinutes = state.weeklyStatsState.data?.sumOf { it.minutes } ?: 0

            WeeklyGoalBanner(
                totalTimeMinutes = totalMinutes,
                onViewProgressClick = { onEvent(HomeEvent.OnPremiumCrownClick) }
            )
        }

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

        item {
            Text(
                text = "Learning Statistics",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E293B)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // StateWrapper quản lý riêng cho phần biểu đồ học tập
            StateWrapper(
                uiState = state.weeklyStatsState,
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 200.dp),
                snackbarHostState = snackbarHostState,
                loadingType = LoadingType.CIRCULAR,
                enablePullToRefresh = false, // Tắt kéo ở đây vì đã bọc PullToRefresh ở ngoài HomeScreen
                onRetry = { onEvent(HomeEvent.OnRetry) }
            ) { data ->

                // --- ĐOẠN XỬ LÝ TRÁNH ĐÈ GIAO DIỆN ---
                if (state.weeklyStatsState.error != null && (data == null || data.isEmpty())) {
                    // Trả về một Box trống cao 200.dp để ErrorOverlay tự căn giữa gọn gàng dưới tiêu đề
                    Box(modifier = Modifier.fillMaxWidth().height(200.dp))
                } else {
                    // Nếu có dữ liệu, hiển thị biểu đồ thống kê học tập bình thường
                    WeeklyProgressCard(weeklyStats = data ?: emptyList())
                }
            }
        }
    }
}
