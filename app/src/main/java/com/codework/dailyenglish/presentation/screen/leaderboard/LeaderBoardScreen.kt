package com.codework.dailyenglish.presentation.screen.leaderboard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.codework.dailyenglish.domain.leaderboard.model.LeaderBoardProfile
import com.codework.dailyenglish.presentation.components.StateWrapper
import com.codework.dailyenglish.presentation.screen.leaderboard.mvi.LeaderboardState
import com.codework.dailyenglish.presentation.theme.AppColors
import com.codework.dailyenglish.presentation.theme.AppTextSizes

// -- Định nghĩa màu sắc nội bộ theo thiết kế mới --
private val PrimaryBlue = Color(0xFF515FE1)
private val LightBg = Color(0xFFFAFAFA)
private val TextDark = Color(0xFF1E293B)
private val TextGray = Color(0xFF64748B)

private val Top1Color = Color(0xFFF59E0B)
private val Top2Color = Color(0xFF82A4F8)
private val Top3Color = Color(0xFF8B5CF6)

private val TrendUp = Color(0xFF22C55E)
private val TrendDown = Color(0xFFEF4444)
private val TrendFlat = Color(0xFFCBD5E1)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaderboardScreen(
    state: LeaderboardState,
    onBackClick: () -> Unit
) {
    var selectedTab by remember { mutableStateOf("EXP") } // State giả cho UI Tab Toggle

    Scaffold(
        containerColor = Color.White,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "🌿", fontSize = 20.sp, color = PrimaryBlue) // Icon cành lá trang trí
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Leaderboard",
                            fontWeight = FontWeight.Bold,
                            fontSize = AppTextSizes.TextSizeTitle,
                            color = AppColors.TextMain
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "🌿", fontSize = 20.sp, color = PrimaryBlue)
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
                .padding(paddingValues)
        ) { wrapper ->
            val listData = wrapper?.leaderboardList ?: emptyList()

            val currentUser = listData.find { it.isCurrentUser }
            val currentUserRank = currentUser?.rank ?: 0

            val top1 = listData.find { it.rank == 1 }
            val top2 = listData.find { it.rank == 2 }
            val top3 = listData.find { it.rank == 3 }
            val remainingUsers = listData.filter { it.rank > 3 }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 1. Toggle Button (EXP / STREAK)
                item {
                    ExpStreakToggle(
                        selectedTab = selectedTab,
                        onTabSelected = { selectedTab = it }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // 2. Current Position Card
                item {
                    CurrentPositionCard(rank = currentUserRank)
                    Spacer(modifier = Modifier.height(24.dp))
                }

                // 3. Top 3 Podium
                item {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        // Ánh hào quang nhẹ phía sau Top 1
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
                            PodiumAvatar(profile = top2, rank = 2, avatarSize = 80.dp, color = Top2Color)
                            Spacer(modifier = Modifier.width(16.dp))
                            PodiumAvatar(profile = top1, rank = 1, avatarSize = 100.dp, color = Top1Color, hasCrown = true)
                            Spacer(modifier = Modifier.width(16.dp))
                            PodiumAvatar(profile = top3, rank = 3, avatarSize = 70.dp, color = Top3Color)
                        }
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                }

                // 4. Danh sách xếp hạng
                items(items = remainingUsers, key = { it.userId }) { user ->
                    LeaderboardRowItem(user = user)
                }

                // 5. Footer Slogan
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Keep learning to climb higher! 🚀",
                        fontSize = 13.sp,
                        color = TextGray,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ExpStreakToggle(selectedTab: String, onTabSelected: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(Color(0xFFF1F5F9), RoundedCornerShape(24.dp))
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TabItem(
            text = "EXP",
            isSelected = selectedTab == "EXP",
            modifier = Modifier.weight(1f),
            onClick = { onTabSelected("EXP") }
        )
        TabItem(
            text = "STREAK",
            isSelected = selectedTab == "STREAK",
            modifier = Modifier.weight(1f),
            onClick = { onTabSelected("STREAK") }
        )
    }
}

@Composable
private fun TabItem(text: String, isSelected: Boolean, modifier: Modifier, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .clip(RoundedCornerShape(20.dp))
            .background(if (isSelected) PrimaryBlue else Color.Transparent)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (isSelected) Color.White else TextGray,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
    }
}

@Composable
fun CurrentPositionCard(rank: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(16.dp))
            .border(1.dp, Color(0xFFF1F5F9), RoundedCornerShape(16.dp))
            .padding(vertical = 14.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Your current position",
            color = TextGray,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.width(16.dp))
        VerticalDivider(modifier = Modifier.height(20.dp), color = Color(0xFFE2E8F0))
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = if (rank > 0) rank.toString() else "-",
            color = PrimaryBlue,
            fontSize = 20.sp,
            fontWeight = FontWeight.Black
        )
    }
}

@Composable
fun PodiumAvatar(
    profile: LeaderBoardProfile?,
    rank: Int,
    avatarSize: Dp,
    color: Color,
    hasCrown: Boolean = false
) {
    if (profile == null) return

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier.padding(bottom = if (rank == 1) 16.dp else 0.dp)
    ) {
        Box(contentAlignment = Alignment.TopCenter) {
            // Vương miện cho Top 1
            if (hasCrown) {
                Text(
                    text = "👑",
                    fontSize = 32.sp,
                    modifier = Modifier.offset(y = (-24).dp).align(Alignment.TopCenter)
                )
            }

            Box(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .size(avatarSize)
                    .border(3.dp, color, CircleShape)
                    .padding(4.dp)
                    .clip(CircleShape),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = profile.avatarUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }

            // Badge Rank ở góc trên bên trái
            Box(
                modifier = Modifier
                    .offset(x = (-4).dp, y = 4.dp)
                    .size(24.dp)
                    .background(color, CircleShape)
                    .align(Alignment.TopStart),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = rank.toString(),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = if (profile.isCurrentUser) "You" else profile.fullName,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = TextDark,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.widthIn(max = 90.dp),
            textAlign = TextAlign.Center
        )

        Text(
            text = "${profile.totalXp} XP",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = PrimaryBlue
        )
    }
}

@Composable
fun LeaderboardRowItem(user: LeaderBoardProfile) {
    // Nếu là user hiện tại, hiển thị Box nổi bật (Giống bạn trong ảnh Rank 212)
    if (user.isCurrentUser) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F5FF)), // Nền xanh nhạt
            border = BorderStroke(1.dp, Color(0xFFB4C5FF)),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = user.rank.toString(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    color = PrimaryBlue,
                    modifier = Modifier.width(36.dp),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.width(16.dp))

                AsyncImage(
                    model = user.avatarUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = "You",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black,
                    color = PrimaryBlue,
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "${user.totalXp} XP",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black,
                    color = PrimaryBlue
                )
            }
        }
    } else {
        // UI Item danh sách bình thường (Rank 4, 5, 6...)
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = user.rank.toString(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark,
                    modifier = Modifier.width(28.dp),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.width(4.dp))

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
                    text = user.fullName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark,
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "${user.totalXp} XP",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryBlue
                )
            }
            HorizontalDivider(color = Color(0xFFF1F5F9), thickness = 1.dp)
        }
    }
}