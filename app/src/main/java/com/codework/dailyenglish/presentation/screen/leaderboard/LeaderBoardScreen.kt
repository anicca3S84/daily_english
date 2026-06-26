package com.codework.dailyenglish.presentation.screen.leaderboard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.codework.dailyenglish.domain.leaderboard.model.LeaderBoardProfile
import com.codework.dailyenglish.presentation.components.StateWrapper
import com.codework.dailyenglish.presentation.screen.leaderboard.mvi.LeaderboardState
import com.codework.dailyenglish.presentation.theme.AppColors
import com.codework.dailyenglish.presentation.theme.AppTextSizes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaderboardScreen(
    state: LeaderboardState,
    onBackClick: () -> Unit
) {
    Scaffold(
        containerColor = Color.White,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Leaderboard",
                        fontWeight = FontWeight.Bold,
                        fontSize = AppTextSizes.TextSizeTitle,
                        color = AppColors.TextMain
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = AppColors.TextMain
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
                .padding(paddingValues)
        ) { wrapper ->
            val listData = wrapper?.leaderboardList ?: emptyList()

            val top1 = listData.find { it.rank == 1 }
            val top2 = listData.find { it.rank == 2 }
            val top3 = listData.find { it.rank == 3 }
            val remainingUsers = listData.filter { it.rank > 3 }

            Column(modifier = Modifier.fillMaxSize()) {

                // ROW TOP 3: Cập nhật viền tròn bọc chân số theo màu BluePrimary của app bạn
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 20.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.Bottom
                ) {
                    PodiumAvatar(profile = top2, rank = 2, avatarSize = 85.dp, circleColor = AppColors.BluePrimary)
                    PodiumAvatar(profile = top1, rank = 1, avatarSize = 105.dp, circleColor = AppColors.BluePrimary, hasCrown = true)
                    PodiumAvatar(profile = top3, rank = 3, avatarSize = 75.dp, circleColor = AppColors.BluePrimary)
                }

                Spacer(modifier = Modifier.height(12.dp))

                // DANH SÁCH LIST DƯỚI: Đổi sang màu nền xanh nhạt mát lạnh (Tông Blue) thay cho màu kem nâu cũ
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                        .background(Color(0xFFEFF6FF)) // Màu nền Xanh Dương siêu nhạt (Slate/Blue tint 50) cực sạch
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(items = remainingUsers, key = { it.userId }) { user ->
                            LeaderboardRowItem(user = user)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PodiumAvatar(
    profile: LeaderBoardProfile?,
    rank: Int,
    avatarSize: androidx.compose.ui.unit.Dp,
    circleColor: Color,
    hasCrown: Boolean = false
) {
    if (profile == null) return

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(contentAlignment = Alignment.TopCenter) {
            Box(
                modifier = Modifier
                    .padding(top = if (hasCrown) 12.dp else 0.dp)
                    .size(avatarSize)
                    .background(Color.White, CircleShape)
                    .clip(CircleShape),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = profile.avatarUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(avatarSize - 6.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }

            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(circleColor, CircleShape)
                    .align(Alignment.BottomCenter),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = rank.toString(),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = if (profile.isCurrentUser) "You" else profile.fullName,
            fontSize = 15.sp,
            fontWeight = if (profile.isCurrentUser) FontWeight.Black else FontWeight.Bold,
            color = if (profile.isCurrentUser) AppColors.BluePrimary else AppColors.TextMain,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.widthIn(max = 90.dp),
            textAlign = TextAlign.Center
        )

        Text(
            text = "${profile.totalXp} pts",
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (profile.isCurrentUser) AppColors.BluePrimary else Color.Gray
        )
    }
}

@Composable
fun LeaderboardRowItem(user: LeaderBoardProfile) {

    val containerColor = Color.White

    val textColor =
        if (user.isCurrentUser) AppColors.BluePrimary
        else AppColors.TextMain

    val pointsColor =
        if (user.isCurrentUser) AppColors.BluePrimary
        else Color(0xFF64748B)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = containerColor
        ),
        border = if (user.isCurrentUser) {
            BorderStroke(
                width = 1.2.dp,
                color = AppColors.BluePrimary
            )
        } else {
            null
        },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = user.rank.toString(),
                fontSize = 16.sp,
                fontWeight = FontWeight.Black,
                color = textColor,
                modifier = Modifier.width(36.dp)
            )

            AsyncImage(
                model = user.avatarUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = if (user.isCurrentUser) "You" else user.fullName,
                fontSize = 16.sp,
                fontWeight = if (user.isCurrentUser) FontWeight.Black else FontWeight.Bold,
                color = textColor,
                modifier = Modifier.weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = "${user.totalXp} pts",
                fontSize = 16.sp,
                fontWeight = FontWeight.Black,
                color = pointsColor
            )
        }
    }
}