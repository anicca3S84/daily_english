package com.codework.dailyenglish.presentation.screen.word_detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.VolumeUp
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.BookmarkBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codework.dailyenglish.presentation.components.StateWrapper
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
        containerColor = AppColors.AppBackground,
        topBar = {
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
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    // Ở đây bạn chỉnh 16.dp hoặc 20.dp nó sẽ cách chính xác ngay lập tức
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text(
                        text = word.word,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF0F172A),
                        letterSpacing = (-0.5).sp
                    )

                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(AppColors.BluePrimary, shape = androidx.compose.foundation.shape.CircleShape)
                            // Thêm clip để khi bấm vào hiệu ứng lan tỏa (ripple) nằm gọn trong hình tròn
                            .clip(androidx.compose.foundation.shape.CircleShape)
                            .clickable { onEvent(WordDetailEvent.OnPlayAudio) },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.VolumeUp,
                            contentDescription = "Play Audio",
                            modifier = Modifier.size(18.dp),
                            tint = Color.White
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))

                // Row chứa Phiên âm, Dấu chấm và Ý nghĩa cạnh nhau
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = word.usPhonetic,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W400,
                        color = AppColors.TitleColor
                    )

                    // Dấu chấm ở giữa
                    Text(
                        text = " • ",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF94A3B8),
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )

                    Text(
                        text = word.meaning,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = AppColors.BluePrimary,
                        lineHeight = 26.sp
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                // ĐƯỜNG KẺ 3 MÀU NGĂN CÁCH (Đã sửa lỗi hiển thị và căn chỉnh cân đối)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(3.dp)
                ) {
                    Box(modifier = Modifier.weight(0.3f).fillMaxHeight().background(Color(0xFF3B82F6)))
                    Box(modifier = Modifier.weight(0.4f).fillMaxHeight().background(Color(0xFFEF4444)))
                    Box(modifier = Modifier.weight(0.3f).fillMaxHeight().background(Color(0xFFF59E0B)))
                }

                Spacer(modifier = Modifier.height(24.dp))

                var definitionIndex = 1

                word.definitions.forEach { definition ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, Color(0xFFF1F5F9)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(18.dp)
                        ) {
                            // Tag Từ loại + Sắc thái nghĩa
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                TypeBadge(type = definition.type)

                                if (definition.aspect.isNotEmpty()) {
                                    Text(
                                        text = "• ${definition.aspect}",
                                        fontSize = 13.sp,
                                        fontStyle = FontStyle.Italic,
                                        color = Color(0xFF64748B)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Duyệt qua từng Nhóm nghĩa
                            definition.meanings.forEachIndexed { index, meaningGroup ->
                                if (index > 0) {
                                    HorizontalDivider(
                                        color = Color(0xFFF1F5F9),
                                        thickness = 1.dp,
                                        modifier = Modifier.padding(vertical = 16.dp)
                                    )
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Text(
                                        text = "$definitionIndex.",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF64748B)
                                    )

                                    definitionIndex++

                                    Column(modifier = Modifier.weight(1f)) {
                                        // Tiêu đề nghĩa / Từ đồng nghĩa
                                        if (meaningGroup.explanation.isNotEmpty()) {
                                            Text(
                                                text = meaningGroup.explanation,
                                                fontSize = 16.sp,
                                                color = Color(0xFF2563EB),
                                                fontWeight = FontWeight.Bold,
                                                lineHeight = 22.sp
                                            )
                                            Spacer(modifier = Modifier.height(4.dp))
                                        }

                                        // Định nghĩa chi tiết
                                        Text(
                                            text = meaningGroup.description,
                                            fontSize = 15.sp,
                                            color = Color(0xFF1E293B),
                                            fontWeight = FontWeight.Normal,
                                            lineHeight = 22.sp
                                        )

                                        // Ví dụ minh họa
                                        if (!meaningGroup.examples.isNullOrEmpty()) {
                                            Spacer(modifier = Modifier.height(10.dp))
                                            meaningGroup.examples.forEach { sentence ->
                                                ExampleSentenceBlock(sentence = sentence)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Badge hiển thị từ loại viết hoa chữ cái đầu thanh lịch
 */
@Composable
fun TypeBadge(type: String) {
    val formattedType = type.lowercase().replaceFirstChar { it.uppercase() }
    val (bgColor, textColor) = when (type.lowercase()) {
        "noun" -> Color(0xFFFFE4E6) to Color(0xFF9F1239)
        "verb" -> Color(0xFFDCFCE7) to Color(0xFF166534)
        "adjective", "adj" -> Color(0xFFDBEAFE) to Color(0xFF1E40AF)
        "adverb", "adv" -> Color(0xFFFEF9C3) to Color(0xFF854D0E)
        else -> Color(0xFFF1F5F9) to Color(0xFF475569)
    }

    Surface(
        color = bgColor,
        shape = RoundedCornerShape(6.dp),
        modifier = Modifier.wrapContentSize()
    ) {
        Text(
            text = formattedType,
            fontSize = 11.sp,
            fontWeight = FontWeight.ExtraBold,
            color = textColor,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            letterSpacing = 0.5.sp
        )
    }
}

/**
 * Khung hiển thị câu ví dụ đi kèm một thanh chỉ thị đứng ở lề trái
 */
@Composable
fun ExampleSentenceBlock(sentence: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .padding(vertical = 3.dp)
    ) {
        Box(
            modifier = Modifier
                .width(3.dp)
                .fillMaxHeight()
                .background(Color(0xFFE2E8F0), shape = RoundedCornerShape(2.dp))
        )

        Spacer(modifier = Modifier.width(10.dp))

        Text(
            text = "“$sentence”",
            fontSize = 14.sp,
            color = Color(0xFF64748B),
            fontStyle = FontStyle.Italic,
            lineHeight = 20.sp,
            modifier = Modifier.weight(1f)
        )
    }
}