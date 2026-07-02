package com.codework.dailyenglish.presentation.screen.word_detail

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.BookmarkBorder
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
import com.codework.dailyenglish.presentation.screen.flashcard.MainAudioButton3D // Tái sử dụng nút loa 3D của bạn
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
            TopAppBar(
                title = {
                    Text(
                        text = "Word detail",
                        fontWeight = FontWeight.Bold,
                        fontSize = AppTextSizes.TextSizeTitle,
                        textAlign = TextAlign.Center,
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
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
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
                    .padding(horizontal = 24.dp, vertical = 8.dp)
            ) {

                // 1. REGION: CORE WORD & TYPE & PHONETICS
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        // Từ vựng in đậm
                        Text(
                            text = word.word,
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Black,
                            color = AppColors.TextMain,
                            letterSpacing = (-0.5).sp
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        // Loại từ + Phiên âm phẳng nằm ngang sát nhau
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = word.type,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = AppColors.BluePrimary,
                                fontStyle = FontStyle.Italic
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = word.usPhonetic,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF64748B)
                            )
                        }
                    }

                    // Loa phát âm 3D Duolingo của bạn nằm gọn ở góc phải cực xịn
                    MainAudioButton3D(
                        onClick = { onEvent(WordDetailEvent.OnPlayAudio) }
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))
                HorizontalDivider(color = Color(0xFFF1F5F9), thickness = 1.dp)
                Spacer(modifier = Modifier.height(24.dp))

                // 2. REGION: ENGLISH DEFINITION
                Text(
                    text = "Definition",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF94A3B8), // Chữ xám nhạt như ảnh mẫu của bạn
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = word.meaning,
                    fontSize = 17.sp,
                    color = AppColors.TextMain,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 26.sp
                )

                Spacer(modifier = Modifier.height(32.dp))
                HorizontalDivider(color = Color(0xFFF1F5F9), thickness = 1.dp)
                Spacer(modifier = Modifier.height(24.dp))

                // 3. REGION: CONTEXT EXAMPLES
                Text(
                    text = "Examples",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF94A3B8),
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Vòng lặp in danh sách câu ví dụ dấu chấm tròn đầu dòng
                word.examples.forEach { sentence ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = "•",
                            fontSize = 18.sp,
                            color = AppColors.BluePrimary,
                            modifier = Modifier.padding(end = 12.dp)
                        )

                        // Hàm tự động highlight từ khóa mục tiêu nằm trong câu ví dụ
                        Text(
                            text = highlightTargetWord(fullSentence = sentence, targetWord = word.word),
                            fontSize = 16.sp,
                            color = Color(0xFF334155),
                            fontWeight = FontWeight.Normal,
                            lineHeight = 24.sp
                        )
                    }
                }
            }
        }
    }
}

/**
 * 🔥 UTILS HELPER: Tìm kiếm từ khóa và tự động bôi đậm (Bold)
 * từ vựng mục tiêu ngay trong câu ví dụ một cách linh hoạt
 */
@Composable
fun highlightTargetWord(fullSentence: String, targetWord: String): androidx.compose.ui.text.AnnotatedString {
    return buildAnnotatedString {
        val lowerSentence = fullSentence.lowercase()
        val lowerTarget = targetWord.lowercase()
        val startIndex = lowerSentence.indexOf(lowerTarget)

        if (startIndex != -1) {
            val endIndex = startIndex + lowerTarget.length
            append(fullSentence.substring(0, startIndex))
            withStyle(style = SpanStyle(fontWeight = FontWeight.Black, color = Color(0xFF0F172A))) {
                append(fullSentence.substring(startIndex, endIndex))
            }
            append(fullSentence.substring(endIndex))
        } else {
            append(fullSentence)
        }
    }
}