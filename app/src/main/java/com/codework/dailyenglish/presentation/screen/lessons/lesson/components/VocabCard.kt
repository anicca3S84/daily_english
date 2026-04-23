package com.codework.dailyenglish.presentation.screen.lessons.lesson.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Lightbulb
import androidx.compose.material.icons.rounded.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codework.dailyenglish.domain.lesson.model.StepData

// --- Bảng màu Premium (Indigo & Slate) ---
private val Indigo50 = Color(0xFFEEF2FF)
private val Indigo100 = Color(0xFFE0E7FF)
private val Indigo500 = Color(0xFF6366F1)
private val Indigo600 = Color(0xFF4F46E5)

private val Emerald50 = Color(0xFFECFDF5)
private val Emerald600 = Color(0xFF059669)

private val Slate400 = Color(0xFF94A3B8)
private val Slate500 = Color(0xFF64748B)
private val Slate700 = Color(0xFF334155)
private val Slate900 = Color(0xFF0F172A)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun VocabCard(
    data: StepData.Vocab,
    modifier: Modifier = Modifier
) {
    // Gradient nền cực nhẹ từ trên xuống tạo chiều sâu
    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(Indigo50, Color.White, Color.White),
        startY = 0f,
        endY = 800f
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundBrush)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
                .padding(top = 40.dp, bottom = 120.dp) // Chừa không gian rộng rãi phía dưới
        ) {
            // ==========================================
            // 1. HEADER: WORD & MEANING
            // ==========================================
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = data.word,
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Black,
                    color = Slate900,
                    letterSpacing = (-1.5).sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 56.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = data.meaning,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Indigo600,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(36.dp))

            // ==========================================
            // 2. PHONETICS & AUDIO (Pill Buttons)
            // ==========================================
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (!data.ukPhonetic.isNullOrEmpty()) {
                    PremiumPhoneticButton(
                        label = "UK",
                        phonetic = data.ukPhonetic,
                        onClick = { /* TODO */ },
                        modifier = Modifier.weight(1f)
                    )
                }
                if (!data.usPhonetic.isNullOrEmpty()) {
                    PremiumPhoneticButton(
                        label = "US",
                        phonetic = data.usPhonetic,
                        onClick = { /* TODO */ },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(36.dp))

            // ==========================================
            // 3. CONTEXT HINT (Thẻ Mẹo cực xinh)
            // ==========================================
            // Mock data, thay bằng `data.contextHint` sau
            val contextHint = "Dùng trong giao tiếp trang trọng hoặc khi khen ngợi một điều gì đó thực sự lôi cuốn, thay thế cho 'very interesting'."

            Surface(
                color = Emerald50,
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(Color.White, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Lightbulb,
                            contentDescription = "Mẹo",
                            tint = Emerald600,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = "Cách dùng chuẩn",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Emerald600,
                            letterSpacing = 0.5.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = contextHint,
                            fontSize = 15.sp,
                            color = Slate700,
                            lineHeight = 24.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // ==========================================
            // 4. EXAMPLES (Quote line style)
            // ==========================================
            if (!data.examples.isNullOrEmpty()) {
                Text(
                    text = "VÍ DỤ THỰC TẾ",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black,
                    color = Slate400,
                    letterSpacing = 1.5.sp
                )
                Spacer(modifier = Modifier.height(20.dp))

                data.examples.forEach { example ->
                    Row(
                        modifier = Modifier.padding(bottom = 20.dp).fillMaxWidth(),
                        verticalAlignment = Alignment.Top
                    ) {
                        // Đường line dọc bên trái mượt mà
                        Box(
                            modifier = Modifier
                                .padding(top = 4.dp)
                                .width(4.dp)
                                .height(24.dp)
                                .clip(RoundedCornerShape(50))
                                .background(Indigo100)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = example,
                            fontSize = 17.sp,
                            color = Slate900,
                            lineHeight = 26.sp,
                            fontWeight = FontWeight.Normal
                        )
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }

            // ==========================================
            // 5. SUGGESTIONS (Soft Chips)
            // ==========================================
            if (!data.suggests.isNullOrEmpty()) {
                Text(
                    text = "TỪ LIÊN QUAN",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black,
                    color = Slate400,
                    letterSpacing = 1.5.sp
                )
                Spacer(modifier = Modifier.height(16.dp))

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    data.suggests.forEach { word ->
                        PremiumChip(text = word)
                    }
                }
            }
        }
    }
}

@Composable
fun PremiumPhoneticButton(
    label: String,
    phonetic: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Dùng Shadow mờ nhẹ thay vì Border cứng ngắc
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(100), // Bo tròn thành hình viên thuốc (Pill)
        color = Color.White,
        shadowElevation = 4.dp,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Rounded.VolumeUp,
                contentDescription = null,
                tint = Indigo500,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = label,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black,
                    color = Slate400,
                    letterSpacing = 1.sp
                )
                Text(
                    text = phonetic,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Slate700
                )
            }
        }
    }
}

@Composable
fun PremiumChip(text: String) {
    Surface(
        shape = RoundedCornerShape(100), // Bo tròn tối đa
        color = Indigo50, // Nền xanh tím siêu nhạt
    ) {
        Text(
            text = text,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = Indigo600, // Chữ xanh tím đậm
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

// ==========================================
// PREVIEW - XEM TRƯỚC TRONG ANDROID STUDIO
// ==========================================
@Preview(showBackground = true, device = "id:pixel_7_pro")
@Composable
fun VocabCardPreview() {
    val dummyData = StepData.Vocab(
        word = "Fascinating",
        meaning = "Cực kỳ lôi cuốn, hấp dẫn",
        imageUrl = null,
        ukPhonetic = "/ˈfæs.ɪ.neɪ.tɪŋ/",
        usPhonetic = "/ˈfæs.ən.eɪ.tɪŋ/",
        ukAudioUrl = "",
        usAudioUrl = "",
        examples = listOf(
            "The museum offers a fascinating glimpse into Roman life.",
            "I found the whole movie absolutely fascinating from start to finish."
        ),
        suggests = listOf("Interesting", "Captivating", "Engaging", "Alluring")
    )

    // Bọc trong theme cơ bản để test
    MaterialTheme {
        VocabCard(data = dummyData)
    }
}