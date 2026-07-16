package com.codework.dailyenglish.presentation.screen.leaderboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codework.dailyenglish.presentation.components.StateWrapper
import com.codework.dailyenglish.presentation.screen.courses.mvi.CourseEvent
import com.codework.dailyenglish.presentation.screen.leaderboard.components.CurrentPositionCard
import com.codework.dailyenglish.presentation.screen.leaderboard.components.ExpStreakToggle
import com.codework.dailyenglish.presentation.screen.leaderboard.components.LeaderboardRowItem
import com.codework.dailyenglish.presentation.screen.leaderboard.components.PodiumAvatar
import com.codework.dailyenglish.presentation.screen.leaderboard.components.TimeFilterRow
import com.codework.dailyenglish.presentation.screen.leaderboard.mvi.LeaderboardEvent
import com.codework.dailyenglish.presentation.screen.leaderboard.mvi.LeaderboardState
import com.codework.dailyenglish.presentation.theme.AppColors
import com.codework.dailyenglish.presentation.theme.AppTextSizes

val TextDark = Color(0xFF1E293B)
val TextGray = Color(0xFF64748B)

private val Top1Color = Color(0xFFF59E0B)
private val Top2Color = Color(0xFF82A4F8)
private val Top3Color = Color(0xFF8B5CF6)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaderboardScreen(
    state: LeaderboardState,
    onEvent: (LeaderboardEvent) -> Unit,
    onBackClick: () -> Unit
) {
    val isStreak = state.selectedTab == "STREAK"

    Scaffold(
        containerColor = Color.White,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Leaderboard",
                            fontWeight = FontWeight.Bold,
                            fontSize = AppTextSizes.TextSizeTitle,
                            color = AppColors.TextMain
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = TextDark
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        }
    ) { paddingValues ->
        StateWrapper(
            uiState = state.leaderboardState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            onRetry = {
                if (state.selectedTab.isEmpty()) {
                    onEvent(LeaderboardEvent.LoadLeaderboard)
                } else {
                    onEvent(LeaderboardEvent.OnTabChanged(state.selectedTab))
                }
            }
        ) { wrapper ->

            if (state.leaderboardState.error != null && wrapper == null) {
                return@StateWrapper
            }

            val listData = wrapper?.leaderboardList ?: emptyList()
            val currentUser = wrapper?.myRank
            val currentUserRank = currentUser?.rank ?: listData.find { it.isCurrentUser }?.rank ?: 0

            val top1 = listData.find { it.rank == 1 }
            val top2 = listData.find { it.rank == 2 }
            val top3 = listData.find { it.rank == 3 }
            val remainingUsers = listData.filter { it.rank > 3 }

            // Dùng Column tổng để phân tách phần Cố định (Header) và phần Cuộn (List)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) {
                // ================= PHẦN HEADER CỐ ĐỊNH =================
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ExpStreakToggle(
                        selectedTab = state.selectedTab,
                        onTabSelected = { onEvent(LeaderboardEvent.OnTabChanged(it)) }
                    )

                    if (state.selectedTab == "EXP") {
                        Spacer(modifier = Modifier.height(16.dp))
                        TimeFilterRow(
                            selectedFilter = state.selectedTimeFilter,
                            onFilterSelected = { onEvent(LeaderboardEvent.OnTimeFilterChanged(it)) }
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    CurrentPositionCard(rank = currentUserRank)
                }

                // ================= PHẦN CUỘN TRÒN (LIST) =================
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f), // Chiếm trọn không gian còn lại và tự cuộn bên trong
                    contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Cục bục vinh quang (Top 1, 2, 3) nay nằm ở vị trí đầu tiên của List cuộn
                    item {
                        Spacer(modifier = Modifier.height(12.dp))
                        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                            Box(
                                modifier = Modifier
                                    .size(180.dp)
                                    .background(
                                        brush = Brush.radialGradient(
                                            colors = listOf(Color(0xFFFEF3C7), Color.Transparent)
                                        ),
                                        shape = CircleShape
                                    )
                                    .align(Alignment.TopCenter)
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.Bottom
                            ) {
                                PodiumAvatar(
                                    profile = top2, rank = 2, avatarSize = 80.dp,
                                    color = Top2Color, isStreak = isStreak,
                                    modifier = Modifier.weight(1f)
                                )
                                PodiumAvatar(
                                    profile = top1, rank = 1, avatarSize = 100.dp,
                                    color = Top1Color, hasCrown = true, isStreak = isStreak,
                                    modifier = Modifier.weight(1f)
                                )
                                PodiumAvatar(
                                    profile = top3, rank = 3, avatarSize = 70.dp,
                                    color = Top3Color, isStreak = isStreak,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(32.dp))
                    }

                    // Danh sách các user xếp hạng sau Top 3
                    items(items = remainingUsers, key = { it.userId }) { user ->
                        LeaderboardRowItem(user = user, isStreak = isStreak)
                    }
                }
            }
        }
    }
}