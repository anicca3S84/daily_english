package com.codework.daily_english.presentation.screen.practice

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.codework.daily_english.presentation.components.BaseScreen

@Composable
fun PracticeScreen(
    viewModel: PracticeViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    BaseScreen(state = state) { data ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface),
            contentPadding = PaddingValues(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // 1. Header & Stats
            item {
                PracticeHeaderSection(points = data.totalPoints, streak = data.streak)
            }

            // 2. REVIEW ZONE (Spaced Repetition) - Quan trọng nhất
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    SectionTitle(title = "Smart Review 🧠", subtitle = "Based on your forgetting curve")
                    Spacer(modifier = Modifier.height(12.dp))
                    SpacedRepetitionCard(
                        topicName = "Family & Friends", // Lấy dynamic từ bài học cũ
                        dueWords = 15,
                        onClick = { /* Navigate to Flashcard Flow */ }
                    )
                }
            }

            // 3. AI SPEAKING HUB (Tech Showcase)
            item {
                Column {
                    PaddingSectionTitle(title = "AI Speaking Hub 🤖", subtitle = "Roleplay with Gemini AI")
                    Spacer(modifier = Modifier.height(12.dp))

                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            AICard(
                                title = "Free Talk",
                                subtitle = "Chat about anything",
                                color = Brush.linearGradient(listOf(Color(0xFF4A00E0), Color(0xFF8E2DE2))),
                                icon = Icons.Default.Mic
                            )
                        }
                        item {
                            AICard(
                                title = "Roleplay: Family",
                                subtitle = "Practice recent topic",
                                color = Brush.linearGradient(listOf(Color(0xFFFD297B), Color(0xFFFF655B))),
                                icon = Icons.Default.RecordVoiceOver
                            )
                        }
                        item {
                            AICard(
                                title = "Interview Prep",
                                subtitle = "Professional mode",
                                color = Brush.linearGradient(listOf(Color(0xFF00C6FF), Color(0xFF0072FF))),
                                icon = Icons.Default.Work
                            )
                        }
                    }
                }
            }

            // 4. GAME ARENA (Gamification)
            item {
                PaddingSectionTitle(title = "Game Arena 🎮", subtitle = "Fun way to master skills")
            }

            // Grid Games (Dùng chunked để chia cột)
            // Mock data game (Bạn có thể đưa vào ViewModel)
            val games = listOf(
                GameItem("Word Match", "Vocabulary", Icons.Default.Extension, Color(0xFFFF9800)),
                GameItem("Speed Quiz", "Grammar", Icons.Default.Timer, Color(0xFF4CAF50)),
                GameItem("Listening Hero", "Listening", Icons.Default.Headset, Color(0xFF2196F3)),
                GameItem("Sentence Builder", "Writing", Icons.Default.Build, Color(0xFF9C27B0))
            )

            items(games.chunked(2)) { pair ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    repeat(pair.count()) { index ->
                        val game = pair[index]
                        GameCardPro(
                            game = game,
                            modifier = Modifier.weight(1f),
                            onClick = { /* Navigate to Game with Topic Selection */ }
                        )
                    }
                    if (pair.count() < 2) Spacer(modifier = Modifier.weight(1f))
                }
            }

            // Padding bottom
            item { Spacer(modifier = Modifier.height(32.dp)) }
        }
    }
}

// --- DATA CLASS CHO UI (Local) ---
data class GameItem(val name: String, val type: String, val icon: ImageVector, val color: Color)

// --- COMPOSABLES ---

@Composable
fun PaddingSectionTitle(title: String, subtitle: String) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        SectionTitle(title, subtitle)
    }
}

@Composable
fun SectionTitle(title: String, subtitle: String) {
    Column {
        Text(text = title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold)
        Text(text = subtitle, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
    }
}

@Composable
fun PracticeHeaderSection(points: Int, streak: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(230.dp) // Tăng chiều cao tổng để cân bằng khi tràn viền
    ) {
        // 1. Background: Tràn lên trên cùng, chỉ bo góc dưới

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(190.dp) // Tăng nhẹ để phủ xuống dưới StatusBar sâu hơn
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF2196F3), // Xanh dương sáng
                            Color(0xFF6375DC)  // Tím Indigo đậm
                        )
                    ),


                    shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
                )
        )

        // 2. Nội dung Header: Sử dụng statusBarsPadding để tự động né camera/đồng hồ
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding() // Đây là chìa khóa để nội dung lùi xuống dưới StatusBar
                .padding(horizontal = 24.dp) // Căn lề trái phải
                .padding(top = 16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Practice Arena",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.weight(1f)
                )
                Surface(color = Color.White.copy(0.2f), shape = CircleShape) {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.History, null, tint = Color.White)
                    }
                }
            }
            Text(
                text = "Let's boost your skills!",
                color = Color.White.copy(0.8f),
                style = MaterialTheme.typography.bodyMedium
            )
        }

        // 3. Card thống kê nổi (Giữ nguyên logic cũ)
        Card(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 24.dp)
                .fillMaxWidth()
                .height(80.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItemValue(value = "$points", label = "Total XP", icon = Icons.Default.EmojiEvents, color = Color(0xFFFFC107))
                VerticalDivider(modifier = Modifier.height(40.dp).width(1.dp), color = Color.LightGray)
                StatItemValue(value = "$streak", label = "Day Streak", icon = Icons.Default.LocalFireDepartment, color = Color(0xFFFF5722))
            }
        }
    }
}

@Composable
fun StatItemValue(value: String, label: String, icon: ImageVector, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Surface(shape = CircleShape, color = color.copy(alpha = 0.1f), modifier = Modifier.size(40.dp)) {
            Icon(icon, null, tint = color, modifier = Modifier.padding(8.dp))
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(text = value, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            Text(text = label, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        }
    }
}

@Composable
fun SpacedRepetitionCard(topicName: String, dueWords: Int, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f))
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Circular Progress giả lập
            Box(contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    progress = { 0.7f },
                    modifier = Modifier.size(60.dp),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = Color.White
                )
                Text(text = "70%", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Flashcard Review", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                Text(text = topicName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(text = "$dueWords words due today", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }

            Surface(shape = CircleShape, color = MaterialTheme.colorScheme.primary) {
                Icon(Icons.AutoMirrored.Filled.ArrowForward, null, tint = Color.White, modifier = Modifier.padding(8.dp))
            }
        }
    }
}

@Composable
fun AICard(title: String, subtitle: String, color: Brush, icon: ImageVector) {
    Card(
        modifier = Modifier.width(160.dp).height(180.dp),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize().background(color)
        ) {
            // Decorative Circles
            Box(modifier = Modifier.offset(x = 80.dp, y = (-20).dp).size(100.dp).background(Color.White.copy(0.1f), CircleShape))

            Column(
                modifier = Modifier.padding(16.dp).fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Surface(shape = RoundedCornerShape(12.dp), color = Color.White.copy(0.2f)) {
                    Icon(icon, null, tint = Color.White, modifier = Modifier.padding(8.dp))
                }

                Column {
                    Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.White)
                    Text(text = subtitle, style = MaterialTheme.typography.bodySmall, color = Color.White.copy(0.8f))
                }
            }
        }
    }
}

@Composable
fun GameCardPro(game: GameItem, modifier: Modifier, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = modifier.height(130.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Vệt màu trang trí bên phải
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .width(40.dp)
                    .fillMaxHeight()
                    .background(game.color.copy(alpha = 0.1f))
            )

            Column(
                modifier = Modifier.padding(16.dp).fillMaxSize(),
                verticalArrangement = Arrangement.Center
            ) {
                Icon(game.icon, null, tint = game.color, modifier = Modifier.size(32.dp))
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = game.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(text = game.type, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
            }
        }
    }
}