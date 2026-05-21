package com.codework.dailyenglish.presentation.screen.saved_word

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.BookmarkBorder
import androidx.compose.material.icons.rounded.FormatQuote
import androidx.compose.material.icons.rounded.Lightbulb
import androidx.compose.material.icons.rounded.Quiz
import androidx.compose.material.icons.rounded.Spellcheck
import androidx.compose.material.icons.rounded.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.codework.dailyenglish.presentation.components.StateWrapper
import com.codework.dailyenglish.presentation.screen.saved_word.mvi.SavedWordsEvent
import com.codework.dailyenglish.presentation.screen.saved_word.mvi.SavedWordsState
import com.codework.dailyenglish.presentation.theme.AppColors
import com.codework.dailyenglish.presentation.theme.AppTextSizes


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedWordsScreen(
    state: SavedWordsState,
    onEvent: (SavedWordsEvent) -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        containerColor = AppColors.AppBackground,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Saved Words",
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
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        floatingActionButton = {
            // Hiển thị nút Learn Now nếu danh sách từ không rỗng
            if (!state.savedWords.isEmpty) {
                androidx.compose.material3.ExtendedFloatingActionButton(
                    onClick = { onEvent(SavedWordsEvent.OnFlashCardClick) },
                    containerColor = AppColors.BluePrimary,
                    contentColor = Color.White,
                    icon = { Icon(Icons.Rounded.Quiz, contentDescription = null) },
                    text = { Text("Learn Now") }
                )
            }
        }
    ) { paddingValues ->
        // Bọc nội dung bằng StateWrapper
        StateWrapper(
            uiState = state.savedWords,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            // onRetry = { /* Bạn có thể truyền onEvent tải lại data ở đây nếu cần */ }
        ) { wordsList -> // data trả về từ StateWrapper (List<Word>?)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(AppColors.AppBackground)
            ) {
                if (wordsList.isNullOrEmpty()) {
                    EmptySavedState()
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            items = wordsList,
                            key = { it.word }
                        ) { word ->
                            SavedWordItem(
                                word = word,
                                onPlayAudio = { onEvent(SavedWordsEvent.OnPlayAudio(word)) },
                                onRemove = { onEvent(SavedWordsEvent.OnRemoveClick(word)) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SavedWordItem(
    word: com.codework.dailyenglish.domain.dictionary.model.Word,
    onPlayAudio: () -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .height(IntrinsicSize.Min),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = word.word,
                    fontSize = AppTextSizes.TextSizeLabel,
                    fontWeight = FontWeight.Bold,
                    color = AppColors.TextMain
                )

                Text(
                    text = word.usPhonetic,
                    fontSize = AppTextSizes.TextSizeBody,
                    color = AppColors.BluePrimary,
                    modifier = Modifier.padding(top = 4.dp)
                )

                // Hiển thị định nghĩa đầu tiên nếu có
                word.meaning.let {
                    Text(
                        text = it,
                        fontSize = AppTextSizes.TextSizeBody,
                        color = Color.Gray,
                        maxLines = 1,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }

            Row {
                IconButton(onClick = onPlayAudio) {
                    Icon(
                        imageVector = Icons.Rounded.VolumeUp,
                        contentDescription = "Play Audio",
                        tint = AppColors.BluePrimary
                    )
                }
                IconButton(onClick = onRemove) {
                    Icon(
                        imageVector = Icons.Rounded.Bookmark,
                        contentDescription = "Unsave",
                        tint = AppColors.BluePrimary
                    )
                }
            }
        }
    }
}

@Composable
fun EmptySavedState() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.SearchOff,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = Color.LightGray
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Your library is empty",
            fontSize = AppTextSizes.TextSizeLabel,
            fontWeight = FontWeight.Medium,
            color = Color.Gray
        )
        Text(
            text = "Save words you want to review later!",
            fontSize = AppTextSizes.TextSizeBody,
            color = Color.LightGray
        )
    }
}