package com.codework.dailyenglish.presentation.components
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.codework.dailyenglish.presentation.screen.home.model.CourseItem

@Composable
fun CourseCard(
    course: CourseItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(200.dp) // Chiều rộng cố định cho list ngang
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            // --- ẢNH TỐI ƯU ---
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(course.thumbnailUrl)
                    .crossfade(true) // Hiệu ứng hiện ảnh mượt mà
                    .build(),
                contentDescription = course.title,
                contentScale = ContentScale.Crop, // Cắt ảnh cho vừa khung
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp) // Chiều cao cố định
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),

                // Hiển thị loading xoay vòng khi đang tải ảnh
                loading = {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                    }
                },

                // Hiển thị màu xám nếu link ảnh chết/lỗi
                error = {
                    Box(modifier = Modifier.fillMaxSize().padding(20.dp), contentAlignment = androidx.compose.ui.Alignment.Center) {
                        Text("No Image", style = MaterialTheme.typography.labelSmall)
                    }
                }
            )

            // --- TIÊU ĐỀ ---
            Text(
                text = course.title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(12.dp)
            )
        }
    }
}