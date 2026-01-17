package com.codework.daily_english.presentation.screen.home
import androidx.compose.runtime.Composable
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.codework.daily_english.presentation.components.BaseScreen
import com.codework.daily_english.presentation.components.CourseCard
import com.codework.daily_english.presentation.components.LessonItemCard
import com.codework.daily_english.presentation.components.QuickActionSection
import com.codework.daily_english.presentation.components.SeeAllHeader
import com.codework.daily_english.presentation.components.WeeklyProgressCard
import com.codework.daily_english.presentation.model.CourseItem
import com.codework.daily_english.presentation.screen.home.HomeData

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // --- SỬ DỤNG BASESCREEN TẠI ĐÂY ---
            BaseScreen(
                state = state,
                snackbarHostState = snackbarHostState,
                onRetry = { viewModel.onEvent(HomeEvent.OnRetry) }
            ) { data ->
                // Chỉ vẽ nội dung khi có Data (Success)
                HomeContent(
                    data = data,
                    onEvent = viewModel::onEvent
                )
            }
        }
    }
}

// ... Import LazyRow, items ...

@Composable
fun HomeContent(
    data: HomeData,
    onEvent: (HomeEvent) -> Unit
) {

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 16.dp), // Bỏ padding ngang để lướt full màn hình đẹp hơn
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // 1. Header (Thêm padding ngang thủ công vì LazyColumn đã bỏ padding ngang)
        item {
            Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                HomeHeader(name = data.userName, streak = data.streakDay)
            }
        }

        item {
            // Giả sử trong HomeData bạn có thêm object wordOfTheDay
            WordOfTheDayCard(
                word = "Perseverance",
                phonetic = "/ˌpərsəˈvirəns/",
                meaning = "Sự kiên trì, bền bỉ",
                onListen = { /* onEvent(HomeEvent.OnPlayAudio) */ }
            )
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
                        items(data.continueLearningList) { lesson ->
                            LessonItemCard(
                                item = lesson,
                                onClick = { onEvent(HomeEvent.OnContinueLesson) }
                            )
                        }
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

        item {
            QuickActionSection(onEvent = onEvent)
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
                    WeeklyProgressCard(weeklyStats = data.weeklyStats)
                }
            }
        }
    }
}

@Composable
fun HomeHeader(
    name: String,
    streak: Int,
    hasNotification: Boolean = true, // Giả lập có thông báo mới
    onNotificationClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp), // Thêm chút padding trên dưới
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // --- PHẦN 1: CHÀO HỎI (TRÁI) ---
        Column(
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Good Morning, 👋", // Thêm icon vẫy tay cho thân thiện
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = name,
                style = MaterialTheme.typography.headlineSmall, // Dùng headlineSmall cho vừa vặn hơn
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        // --- PHẦN 2: STREAK & NOTIFICATION (PHẢI) ---
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp) // Khoảng cách giữa Lửa và Chuông
        ) {
            // 2.1 Streak Badge (Hình viên thuốc bo tròn)
            Surface(
                color = Color(0xFFFF5722).copy(alpha = 0.1f), // Màu Cam Lửa nhạt
                shape = CircleShape, // Bo tròn hoàn toàn
                border = BorderStroke(1.dp, Color(0xFFFF5722).copy(alpha = 0.3f))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Rounded.LocalFireDepartment, // Icon Lửa tròn trịa
                        contentDescription = "Streak",
                        tint = Color(0xFFFF5722), // Màu cam đậm
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "$streak",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFF5722)
                    )
                }
            }

            // 2.2 Notification Icon (Có chấm đỏ)
            IconButton(
                onClick = onNotificationClick,
                modifier = Modifier
                    .size(44.dp) // Tăng vùng bấm
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        shape = CircleShape
                    )
            ) {
                // BadgedBox là component chuẩn M3 để tạo chấm đỏ thông báo
                BadgedBox(
                    badge = {
                        if (hasNotification) {
                            Badge(
                                containerColor = Color.Red,
                                contentColor = Color.White,
                                modifier = Modifier.size(8.dp) // Chấm đỏ nhỏ xinh
                            )
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Notifications,
                        contentDescription = "Notifications",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}



@Composable
fun WordOfTheDayCard(word: String, phonetic: String, meaning: String, onListen: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Word of the Day", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                Text(text = word, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Text(text = phonetic, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = meaning, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
            }

            IconButton(
                onClick = onListen,
                colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Icon(imageVector = Icons.Default.VolumeUp, contentDescription = null, tint = Color.White)
            }
        }
    }
}