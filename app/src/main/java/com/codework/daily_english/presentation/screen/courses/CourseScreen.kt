package com.codework.daily_english.presentation.screen.courses

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.codework.daily_english.presentation.components.BaseScreen
import com.codework.daily_english.presentation.model.TopicItem

@Composable
fun CourseScreen(viewModel: CourseViewModel = viewModel()) {
    val state by viewModel.uiState.collectAsState()

    BaseScreen(state = state) { data ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F7FA)), // 1. Đổi màu nền sang Xám rất nhạt để làm nổi bật Card trắng
            contentPadding = PaddingValues(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // --- NÂNG CẤP 1: HEADER MÀU SẮC ---
            item {
                CourseHeaderSection(
                    currentLevel = data.selectedLevel,
                    progress = 0.45f // Ví dụ: Tổng tiến độ của Level này
                )
            }

            // --- NÂNG CẤP 2: LEVEL SELECTOR NỔI BẬT HƠN ---
            item {
                Text(
                    text = "Select Level",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                LevelSelector(
                    levels = data.availableLevels,
                    selectedLevel = data.selectedLevel,
                    onLevelSelected = { viewModel.onEvent(CourseEvent.OnLevelSelected(it)) }
                )
            }

            items(data.topics) { topic ->
                Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                    TopicItemCard(
                        item = topic,
                        onClick = { viewModel.onEvent(CourseEvent.OnTopicClick(topic.id)) }
                    )
                }
            }
        }
    }
}

// --- COMPONENT MỚI: HEADER GRADIENT ---
@Composable
fun CourseHeaderSection(currentLevel: String, progress: Float) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp) // Chiều cao cố định
            .clip(RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp)) // Bo tròn đáy
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                    )
                )
            )
    ) {
        // Họa tiết trang trí mờ (Circles)
        Box(
            modifier = Modifier
                .offset(x = 200.dp, y = (-50).dp)
                .size(200.dp)
                .background(Color.White.copy(alpha = 0.1f), CircleShape)
        )

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Text bên trái
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Current Level",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.White.copy(alpha = 0.8f)
                )
                Text(
                    text = currentLevel,
                    style = MaterialTheme.typography.displaySmall, // Chữ to đùng
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Thanh Progress Bar nhỏ trong Header
                Text(
                    text = "Overall Progress: ${(progress * 100).toInt()}%",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = Color(0xFFFFC107), // Màu vàng nổi bật trên nền xanh
                    trackColor = Color.White.copy(alpha = 0.3f)
                )
            }

            // Icon/Ảnh bên phải (Tạo cảm giác đầy đặn)
            Icon(
                imageVector = Icons.Default.EmojiEvents, // Icon cái cúp
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.9f),
                modifier = Modifier
                    .size(100.dp)
                    .graphicsLayer {
                        rotationZ = 15f // Xoay nhẹ cho nghệ thuật
                    }
            )
        }
    }
}

@Composable
fun LevelSelector(
    levels: List<String>,
    selectedLevel: String,
    onLevelSelected: (String) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.padding(bottom = 16.dp)
    ) {
        items(levels) { level ->
            val isSelected = level == selectedLevel
            FilterChip(
                selected = isSelected,
                onClick = { onLevelSelected(level) },
                label = { Text(level, fontWeight = if(isSelected) FontWeight.Bold else FontWeight.Normal) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                border = FilterChipDefaults.filterChipBorder(
                    borderColor = Color.Transparent,
                    selectedBorderColor = Color.Transparent,
                    enabled = true,
                    selected = isSelected
                ),
                shape = CircleShape
            )
        }
    }
}

@Composable
fun TopicItemCard(
    item: TopicItem,
    onClick: () -> Unit
) {
    Card(
        onClick = if (item.isLocked) {{}} else onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box {
            Row(
                modifier = Modifier.padding(12.dp).fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Ảnh Thumbnail
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(12.dp))
                ) {
                    AsyncImage(
                        model = item.imageUrl,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Overlay khóa
                    if (item.isLocked) {
                        Box(
                            modifier = Modifier.fillMaxSize().background(Color.Black.copy(0.4f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Lock, null, tint = Color.White)
                        }
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Nội dung text
                Column(modifier = Modifier.weight(1f)) {
                    // Badge nhỏ trên đầu tiêu đề (Lấp khoảng trống)
                    if (!item.isLocked) {
                        Surface(
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(4.dp),
                            modifier = Modifier.padding(bottom = 4.dp)
                        ) {
                            Text(
                                text = "VOCAB • LISTENING", // Fake tags để nhìn đầy đặn
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (item.isLocked) Color.Gray else Color.Black
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    if (!item.isLocked) {
                        // Nút Start dạng chữ thay vì icon Play (Tạo điểm nhấn hành động)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            LinearProgressIndicator(
                                progress = { item.progress },
                                modifier = Modifier.weight(1f).height(6.dp).clip(RoundedCornerShape(3.dp)),
                                strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            // Nút tròn nhỏ
                            FilledIconButton(
                                onClick = onClick,
                                modifier = Modifier.size(32.dp),
                                colors = IconButtonDefaults.filledIconButtonColors(containerColor = MaterialTheme.colorScheme.primary)
                            ) {
                                Icon(Icons.Default.PlayArrow, null, modifier = Modifier.size(16.dp))
                            }
                        }
                    } else {
                        Text("Locked", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                    }
                }
            }

            // --- NÂNG CẤP 3: BADGE TRẠNG THÁI GÓC (FREE / PRO) ---
            // Một dải ruy băng nhỏ ở góc trái trên (nếu cần) hoặc góc phải
            // Ở đây tôi dùng icon ngôi sao đơn giản ở góc phải trên để đánh dấu Premium
            if (item.isLocked) {
                // Không làm gì
            } else {
                // Ví dụ: Đánh dấu bài Free
                Surface(
                    color = Color(0xFF4CAF50), // Màu xanh Free
                    shape = RoundedCornerShape(bottomStart = 8.dp),
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Text(
                        text = "FREE",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        fontSize = 10.sp
                    )
                }
            }
        }
    }
}