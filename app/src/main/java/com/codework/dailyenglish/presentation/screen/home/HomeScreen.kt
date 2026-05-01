package com.codework.dailyenglish.presentation.screen.home

import androidx.compose.runtime.Composable
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material3.*
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.codework.dailyenglish.presentation.components.CourseCard
import com.codework.dailyenglish.presentation.components.QuickActionSection
import com.codework.dailyenglish.presentation.components.SeeAllHeader
import com.codework.dailyenglish.presentation.screen.home.model.CourseItem
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.codework.dailyenglish.R
import com.codework.dailyenglish.presentation.screen.home.model.QuickActionItem
import com.codework.dailyenglish.presentation.screen.home.mvi.HomeEvent
import com.codework.dailyenglish.presentation.screen.home.mvi.HomeState

@Composable
fun HomeScreen(
    state: HomeState,
    onEvent: (HomeEvent) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }


    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { _ ->
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // --- SỬ DỤNG BASESCREEN TẠI ĐÂY ---
                // Chỉ vẽ nội dung khi có Data (Success)
                HomeContent(
//                    data = data,
                    onEvent = onEvent
                )
        }
    }
}

// ... Import LazyRow, items ...

@Composable
fun HomeContent(
//    data: HomeState,
    onEvent: (HomeEvent) -> Unit
) {

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp), // Bỏ padding ngang để lướt full màn hình đẹp hơn
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // 1. Header (Thêm padding ngang thủ công vì LazyColumn đã bỏ padding ngang)
        item {
            Box(modifier = Modifier.padding(horizontal = 16.dp)) {
//                HomeHeader(name = data.userName, streak = data.streakDay)
            }
        }

        item {
            Column {
                SeeAllHeader(
                    title = "Continue Learning",
                    onSeeAllClick = {
                        // onEvent(HomeEvent.OnNavigateToCourse)
                    }
                ) {
                    // Đây là phần 'content' được truyền vào SeeAllHeader
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
//                        items(data.continueLearningList) { lesson ->
//                            LessonCard(
//                                item = lesson,
//                                onClick = { onEvent(HomeEvent.OnContinueLesson) }
//                            )
//                        }
                    }
                }
            }
        }

        // 3. Daily Quest
        item {
            Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                // ... (Code Daily Quest cũ)
            }
        }

        // Trong HomeContent
        // fake data
        item {
            // 1. Định nghĩa danh sách các hành động
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
                    label = "Grammar",
                    color = Color(0xFFFF9800),
                    onClick = { onEvent(HomeEvent.OnQuickActionGrammar) }
                ),
                QuickActionItem(
                    icon = R.drawable.ic_launcher_foreground,
                    label = "Translate",
                    color = Color(0xFF4CAF50),
                    onClick = { onEvent(HomeEvent.OnQuickActionTranslate) }
                )
            )

            // 2. Gọi Component
            QuickActionSection(actions = actionList, )
        }


        // Dữ liệu giả để test giao diện
        val mockCourses = listOf(
            CourseItem(
                id = "1",
                title = "English for Beginners: Start Here",
                thumbnailUrl = "https://picsum.photos/300/200" // Ảnh random
            ),
            CourseItem(
                id = "2",
                title = "Business Communication Masterclass",
                thumbnailUrl = "https://picsum.photos/300/201"
            ),
            CourseItem(
                id = "3",
                title = "IELTS Speaking Success",
                thumbnailUrl = "https://picsum.photos/300/202"
            ),
            CourseItem(
                id = "4",
                title = "American Pronunciation Workshop",
                thumbnailUrl = "https://picsum.photos/300/203"
            )
        )

        item {
            Column {
                SeeAllHeader(
                    title = "Top Courses",
                    onSeeAllClick = {
                        // onEvent(HomeEvent.OnNavigateToCourse)
                    }
                ) {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // SỬA Ở ĐÂY: Dùng mockCourses thay vì data.continueLearningList
                        items(mockCourses) { course ->
                            CourseCard(
                                course = course, // Truyền đúng kiểu dữ liệu CourseItem
                                onClick = {
                                    // Chưa có logic click thì để trống hoặc log ra
                                    println("Clicked course: ${course.title}")
                                }
                            )
                        }
                    }
                }
            }
        }



        item {
            SeeAllHeader(
                title = "Learning Statistics", // Hoặc "Your Activity"
                onSeeAllClick = { /* Có thể điều hướng sang màn hình báo cáo chi tiết */ }
            ) {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 8.dp) // Thêm chút khoảng cách dưới biểu đồ
                ) {
//                    WeeklyProgressCard(weeklyStats = data.weeklyStats)
                }
            }
        }
    }
}

@Composable
fun HomeHeader(
    name: String,
    streak: Int,
    avatarUrl: String? = "https://images.unsplash.com/photo-1504609773096-104ff2c73ba4", // Thêm ảnh đại diện
    hasNotification: Boolean = true,
    onNotificationClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // --- PHẦN 1: PROFILE & CHÀO HỎI (TRÁI) ---
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp)) // Bo góc vùng click cho tinh tế
                .clickable { onProfileClick() }
                .padding(4.dp)
        ) {
            // Bao quanh Avatar bằng một lớp Border mỏng để trông chuyên nghiệp hơn
            Surface(
                shape = CircleShape,
                modifier = Modifier.size(52.dp), // Tăng nhẹ size tổng thể
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.primaryContainer)
            ) {
                AsyncImage(
                    model = avatarUrl ?: "https://www.CuaHangCuaBan.com/default-avatar.png", // Fallback nếu url null
                    contentDescription = "User Avatar",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop,
                    // Hiện icon mặc định trong khi chờ tải ảnh
                    placeholder = painterResource(id = com.codework.dailyenglish.R.drawable.ic_launcher_foreground),
                    // Hiện icon lỗi nếu link ảnh hỏng
                    error = painterResource(id = com.codework.dailyenglish.R.drawable.ic_launcher_foreground)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = "Hello,",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                    letterSpacing = 0.5.sp
                )
                Text(
                    text = "$name ✨",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        // --- PHẦN 2: STREAK & NOTIFICATION (PHẢI) ---
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Streak Chip - Thiết kế dạng "Pill" hiện đại
            Surface(
                color = Color(0xFFFF5722).copy(alpha = 0.15f),
                shape = MaterialTheme.shapes.extraLarge,
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Rounded.LocalFireDepartment,
                        contentDescription = null,
                        tint = Color(0xFFFF5722),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "$streak",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFFFF5722)
                    )
                }
            }

            // Nút thông báo - Minimalist
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                    .clickable { onNotificationClick() },
                contentAlignment = Alignment.Center
            ) {
                BadgedBox(
                    badge = {
                        if (hasNotification) {
                            Badge(
                                modifier = Modifier.size(6.dp),
                                containerColor = Color(0xFFFF3D00) // Màu đỏ rực
                            )
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Notifications,
                        contentDescription = null,
                        modifier = Modifier.size(22.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}


