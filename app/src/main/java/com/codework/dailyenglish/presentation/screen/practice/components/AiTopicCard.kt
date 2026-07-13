package com.codework.dailyenglish.presentation.screen.practice.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.codework.dailyenglish.domain.vocabulary.model.VocabTopic
import com.codework.dailyenglish.presentation.theme.AppColors


@Composable
fun AiTopicCard(
    topic: VocabTopic,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bgColor = when (topic.name.lowercase()) {
        "business"   -> Color(0xFFEEF2FF)
        "travel"     -> Color(0xFFE0F2FE)
        "food"       -> Color(0xFFFFF7D6)
        "animals"    -> Color(0xFFDCFCE7)
        "technology" -> Color(0xFFF3E8FF)
        "health"     -> Color(0xFFFFE4E6)
        "sports"     -> Color(0xFFFFEDD5)
        "education"  -> Color(0xFFCCFBF1)
        else         -> Color(0xFFF8FAFC)
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(bottom = 8.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // 1. Khối Hình ảnh & Icon Micro
            Box(modifier = Modifier.size(68.dp)) {
                AsyncImage(
                    model = topic.imageUrl,
                    contentDescription = topic.name,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(bgColor) // Cập nhật màu nền động tại đây
                        .padding(8.dp), // Padding này giúp icon thu nhỏ lại bên trong hình tròn, không lo bị chạm viền
                    contentScale = ContentScale.Fit // Thay đổi từ Crop sang Fit để hiển thị toàn bộ icon
                )

                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(22.dp)
                        .background(
                            brush = AppColors.MainGradient,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Mic,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }

            Text(
                text = topic.name,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color(0xFF1E293B),
                maxLines = 2,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )

            // 3. Chip "Talk to AI"
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFEEF2FF)
            ) {
                Text(
                    text = "Talk to AI",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    style = LocalTextStyle.current.copy(
                        brush = AppColors.MainGradient
                    ),
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                )
            }
        }
    }
}