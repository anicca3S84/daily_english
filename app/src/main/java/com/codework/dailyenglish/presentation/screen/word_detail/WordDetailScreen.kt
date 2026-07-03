package com.codework.dailyenglish.presentation.screen.word_detail

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.VolumeUp
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.BookmarkBorder
import androidx.compose.material.icons.rounded.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codework.dailyenglish.presentation.components.StateWrapper
import com.codework.dailyenglish.presentation.screen.flashcard.components.MainAudioButton3D
import com.codework.dailyenglish.presentation.screen.word_detail.components.highlightTargetWord
import com.codework.dailyenglish.presentation.screen.word_detail.mvi.WordDetailEvent
import com.codework.dailyenglish.presentation.screen.word_detail.mvi.WordDetailState
import com.codework.dailyenglish.presentation.theme.AppColors
import com.codework.dailyenglish.presentation.theme.AppTextSizes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WordDetailScreen(
    state: WordDetailState,
    onEvent: (WordDetailEvent) -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        containerColor = Color.White,
        topBar = {
            // Đổi sang CenterAlignedTopAppBar để tiêu đề luôn ra chính giữa màn hình
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Word detail",
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
                actions = {
                    state.wordState.data?.let { word ->
                        IconButton(onClick = { onEvent(WordDetailEvent.OnToggleSave) }) {
                            Icon(
                                imageVector = if (word.isSaved) Icons.Rounded.Bookmark else Icons.Rounded.BookmarkBorder,
                                contentDescription = "Save Word",
                                tint = if (word.isSaved) AppColors.BluePrimary else Color(0xFF94A3B8)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        }
    ) { paddingValues ->
        StateWrapper(
            uiState = state.wordState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) { word ->
            if (word == null) return@StateWrapper

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                // 1. TỪ VỰNG CHÍNH (Kích thước lớn chuẩn phong cách ảnh mẫu)
                Text(
                    text = word.word,
                    fontSize = 44.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A),
                    letterSpacing = (-0.5).sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                // 2. HÀNG PHIÊN ÂM + NÚT PHÁT ÂM (Nhỏ gọn sát cạnh nhau)
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = word.usPhonetic,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF475569)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(
                        onClick = { onEvent(WordDetailEvent.OnPlayAudio) },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.VolumeUp,
                            contentDescription = "Play Audio",
                            tint = Color(0xFF334155),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 3. ĐƯỜNG KẺ PHÂN ĐOẠN MÀU (Accent Line chuẩn như trong ảnh mẫu)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(3.dp)
                ) {
                    Box(modifier = Modifier.weight(0.3f).fillMaxHeight().background(Color(0xFFEF4444)))
                    Box(modifier = Modifier.weight(0.4f).fillMaxHeight().background(Color(0xFF3B82F6)))
                    Box(modifier = Modifier.weight(0.3f).fillMaxHeight().background(Color(0xFFF59E0B)))
                }

                Spacer(modifier = Modifier.height(28.dp))

                // 4. KHỐI HIỂN THỊ TỪ LOẠI (Noun / Verb / Adjective viết thường tinh tế)
                Text(
                    text = word.type.lowercase(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFFE11D48) // Màu đỏ gạch/hồng nhạt hiện đại
                )

                Spacer(modifier = Modifier.height(12.dp))

                // 5. KHỐI ĐỊNH NGHĨA ĐÁNH SỐ (Layout thoáng đãng)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "1.",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF0F172A)
                    )

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = word.meaning,
                            fontSize = 18.sp,
                            color = Color(0xFF334155),
                            fontWeight = FontWeight.Medium,
                            lineHeight = 28.sp
                        )

                        // 6. KHỐI VÍ DỤ ĐI KÈM (Đặt ngay dưới định nghĩa, thụt lề nhẹ)
                        if (word.examples.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(16.dp))

                            word.examples.forEach { sentence ->
                                Text(
                                    text = highlightTargetWord(fullSentence = sentence, targetWord = word.word),
                                    fontSize = 16.sp,
                                    color = Color(0xFF64748B), // Màu chữ ví dụ nhẹ hơn chữ chính
                                    fontWeight = FontWeight.Normal,
                                    lineHeight = 24.sp,
                                    modifier = Modifier.padding(bottom = 12.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
