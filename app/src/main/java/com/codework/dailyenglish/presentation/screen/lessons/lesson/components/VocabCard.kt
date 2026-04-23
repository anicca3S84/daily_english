package com.codework.dailyenglish.presentation.screen.lessons.lesson.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codework.dailyenglish.domain.lesson.model.StepData
import com.codework.dailyenglish.presentation.components.PhoneticGroup

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun VocabCard(
    data: StepData.Vocab,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp), // Tăng shadow nhẹ cho card nổi bật
        shape = RoundedCornerShape(32.dp) // Bo góc tròn trịa hơn
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 32.dp) // Tăng padding để tạo khoảng trắng sang trọng
        ) {

            // ==========================================
            // 1. HERO SECTION (Từ vựng & Phát âm)
            // Đẩy ra giữa màn hình để bù đắp khoảng trống của ảnh
            // ==========================================
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = data.word,
                    style = MaterialTheme.typography.displayMedium, // Chữ to khổng lồ
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF0F172A), // Đen tuyền
                    textAlign = TextAlign.Center,
                    letterSpacing = (-1).sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(0.8f), // Giới hạn chiều rộng để 2 nút gần nhau hơn
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    PhoneticGroup(
                        label = "UK",
                        phonetic = data.ukPhonetic.orEmpty(),
                        mainColor = Color(0xFF3B82F6),
                        onClick = { /* TODO: Play ukAudioUrl */ }
                    )
                    PhoneticGroup(
                        label = "US",
                        phonetic = data.usPhonetic.orEmpty(),
                        mainColor = Color(0xFFEF4444),
                        onClick = { /* TODO: Play usAudioUrl */ }
                    )
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 24.dp),
                thickness = 1.dp,
                color = Color(0xFFE2E8F0)
            )

            // ==========================================
            // 2. MEANING SECTION
            // ==========================================
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "ĐỊNH NGHĨA",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color(0xFF64748B),
                    letterSpacing = 1.5.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = data.meaning,
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color(0xFF334155),
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 32.sp
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // ==========================================
            // 3. EXAMPLES SECTION (Thiết kế dạng Blockquote)
            // ==========================================
            if (!data.examples.isNullOrEmpty()) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "VÍ DỤ THỰC TẾ",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color(0xFF64748B),
                        letterSpacing = 1.5.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    data.examples.forEach { example ->
                        // Thiết kế Box ví dụ có nền xám nhạt và viền trái màu xanh
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFF8FAFC))
                        ) {
                            Row(modifier = Modifier.fillMaxWidth()) {
                                Box(
                                    modifier = Modifier
                                        .width(4.dp)
                                        .background(MaterialTheme.colorScheme.primary)
                                )
                                Text(
                                    text = example,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = Color(0xFF475569),
                                    lineHeight = 24.sp,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // ==========================================
            // 4. SUGGESTIONS SECTION
            // ==========================================
            if (!data.suggests.isNullOrEmpty()) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "TỪ ĐỒNG NGHĨA / LIÊN QUAN",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color(0xFF64748B),
                        letterSpacing = 1.5.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        data.suggests.forEach { tag ->
                            SuggestionChip(text = tag)
                        }
                    }
                }
            }

            // Spacer dưới cùng để khi scroll không bị dính đáy
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun SuggestionChip(text: String) {
    Text(
        text = text,
        modifier = Modifier
            .background(
                color = Color(0xFFF8FAFC),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 12.dp, vertical = 6.dp),
        style = MaterialTheme.typography.bodyMedium,
        color = Color(0xFF64748B)
    )
}