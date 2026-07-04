package com.codework.dailyenglish.presentation.screen.vocab_list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.RadioButtonUnchecked
import androidx.compose.material.icons.rounded.School
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codework.dailyenglish.presentation.components.StateWrapper
import com.codework.dailyenglish.presentation.screen.dictionary.components.WordResultCard
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
        },

        floatingActionButton = {
            if (state.selectedWordIds.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding(),
                    contentAlignment = Alignment.Center
                ) {
                    Button(
                        onClick = { onEvent(VocabularyListEvent.OnFlashCardClick) },
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AppColors.BluePrimary)
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
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { paddingValues ->
        StateWrapper(
            uiState = state.vocabWords,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) { wordsList ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(AppColors.AppBackground)
            ) {
                if (wordsList == null) {
                    Spacer(modifier = Modifier.fillMaxSize())
                } else if (wordsList.isEmpty()) {
                    if (state.isInitialized) {
                        EmptySavedState()
                    } else {
                        Spacer(modifier = Modifier.fillMaxSize())
                    }
                } else {
                    val isAllSelected = state.selectedWordIds.size == wordsList.size

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
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

                        itemsIndexed(items = wordsList, key = { _, word -> word.id }) { index, word ->
                            val isSelected = state.selectedWordIds.contains(word.id)

                            WordResultCard(
                                word = word,
                                index = index,
                                isSavedMode = state.isSavedMode,
                                isSelected = isSelected,
                                onClick = {
                                    onEvent(VocabularyListEvent.OnSelectWordToggle(word.id))
                                },
                                onPlayAudio = { onEvent(VocabularyListEvent.OnPlayAudio(word)) },
                                onToggleSave = { onEvent(VocabularyListEvent.OnToggleSaveClick(word)) }
                            )
                        }

                        item {
                            Spacer(modifier = Modifier.height(120.dp))
                        }
                    }
                }
            }
        }
    }
}