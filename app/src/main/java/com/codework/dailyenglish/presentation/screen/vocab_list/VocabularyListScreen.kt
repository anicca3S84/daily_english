package com.codework.dailyenglish.presentation.screen.vocab_list


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.BookmarkBorder
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.RadioButtonUnchecked
import androidx.compose.material.icons.rounded.School
import androidx.compose.material.icons.rounded.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codework.dailyenglish.domain.dictionary.model.Word
import com.codework.dailyenglish.presentation.components.StateWrapper
import com.codework.dailyenglish.presentation.screen.vocab_list.components.EmptySavedState
import com.codework.dailyenglish.presentation.screen.vocab_list.mvi.VocabularyListEvent
import com.codework.dailyenglish.presentation.screen.vocab_list.mvi.VocabularyListState
import com.codework.dailyenglish.presentation.theme.AppColors
import com.codework.dailyenglish.presentation.theme.AppTextSizes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VocabularyListScreen(
    state: VocabularyListState,
    onEvent: (VocabularyListEvent) -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        containerColor = AppColors.AppBackground,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = state.title,
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
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        }
    ) { paddingValues ->
        StateWrapper(
            uiState = state.vocabWords,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) { wordsList ->
            Box(modifier = Modifier.fillMaxSize().background(AppColors.AppBackground)) {
                if (wordsList.isNullOrEmpty()) {
                    EmptySavedState()
                } else {
                    val isAllSelected = state.selectedWordIds.size == wordsList.size

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // 1. THANH TIỆN ÍCH CHỌN TẤT CẢ (Nằm ở đầu để quản lý)
                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onEvent(VocabularyListEvent.OnSelectAllToggle) }
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = if (isAllSelected) Icons.Rounded.CheckCircle else Icons.Rounded.RadioButtonUnchecked,
                                    contentDescription = null,
                                    tint = if (isAllSelected) AppColors.BluePrimary else Color.Gray,
                                    modifier = Modifier.size(22.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (isAllSelected) "Deselect All" else "Select All Items",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = AppColors.TextMain
                                )
                            }
                        }

                        // 2. DANH SÁCH THẺ TỪ VỰNG (Giữ ở giữa)
                        items(items = wordsList, key = { it.id }) { word ->
                            val isSelected = state.selectedWordIds.contains(word.id)
                            VocabularyItemCard(
                                word = word,
                                isSavedMode = state.isSavedMode,
                                isSelected = isSelected,
                                onSelectToggle = { onEvent(VocabularyListEvent.OnSelectWordToggle(word.id)) },
                                onPlayAudio = { onEvent(VocabularyListEvent.OnPlayAudio(word)) },
                                onToggleSave = { onEvent(VocabularyListEvent.OnToggleSaveClick(word)) }
                            )
                        }

                        // 3. CHUYỂN NÚT START FLASHCARD XUỐNG CUỐI LIST TẠI ĐÂY
                        item {
                            Spacer(modifier = Modifier.height(8.dp)) // Tạo chút khoảng cách với item cuối cùng
                            Button(
                                onClick = { onEvent(VocabularyListEvent.OnFlashCardClick) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = AppColors.BluePrimary,
                                    disabledContainerColor = Color.LightGray
                                ),
                                enabled = state.selectedWordIds.isNotEmpty()
                            ) {
                                Icon(imageVector = Icons.Rounded.School, contentDescription = null, tint = Color.White)
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "Start Flashcard (${state.selectedWordIds.size})",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }

                        // Thêm một khoảng đệm nhỏ cuối cùng để nút không bị dính sát vào cạnh dưới màn hình khi cuộn kịch sàn
                        item {
                            Spacer(modifier = Modifier.height(24.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun VocabularyItemCard(
    word: Word,
    isSavedMode: Boolean,
    isSelected: Boolean,
    onSelectToggle: () -> Unit,
    onPlayAudio: () -> Unit,
    onToggleSave: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelectToggle() }, // Bấm vào bất kỳ đâu trên card cũng có thể tích chọn nhanh
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFFF0F7FF) else Color.White // Đổi màu nền nhẹ nếu được chọn
        ),
        border = BorderStroke(
            width = 1.dp,
            color = if (isSelected) AppColors.BluePrimary.copy(alpha = 0.5f) else Color(0xFFE2E8F0)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // KHU VỰC 1: Ô CHECKBOX CUSTOM TRÒN BÊN TRÁI
            Icon(
                imageVector = if (isSelected) Icons.Rounded.CheckCircle else Icons.Rounded.RadioButtonUnchecked,
                contentDescription = "Select status",
                tint = if (isSelected) AppColors.BluePrimary else Color(0xFFCBD5E1),
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onSelectToggle() }
            )

            Spacer(modifier = Modifier.width(14.dp))

            // KHU VỰC 2: NỘI DUNG TỪ VỰNG CHÍNH Ở GIỮA
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = word.word,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = AppColors.TextMain
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    // Nút bấm phát âm dạng nhỏ cạnh chữ
                    Icon(
                        imageVector = Icons.Rounded.VolumeUp,
                        contentDescription = "Play Audio",
                        tint = AppColors.BluePrimary,
                        modifier = Modifier
                            .size(20.dp)
                            .clickable { onPlayAudio() }
                    )
                }

                Text(
                    text = word.usPhonetic,
                    fontSize = 14.sp,
                    color = AppColors.BluePrimary,
                    modifier = Modifier.padding(top = 2.dp)
                )

                Text(
                    text = word.meaning,
                    fontSize = 15.sp,
                    color = Color(0xFF475569),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            // KHU VỰC 3: ACTION BUTTONS (NÚT SAVE / GỠ SAVE) BÊN PHẢI
            IconButton(onClick = onToggleSave) {
                Icon(
                    imageVector = if (isSavedMode || word.isSaved) Icons.Rounded.Bookmark else Icons.Rounded.BookmarkBorder,
                    contentDescription = "Save Action",
                    tint = if (isSavedMode || word.isSaved) AppColors.BluePrimary else Color(0xFF94A3B8),
                    modifier = Modifier.size(26.dp)
                )
            }
        }
    }
}