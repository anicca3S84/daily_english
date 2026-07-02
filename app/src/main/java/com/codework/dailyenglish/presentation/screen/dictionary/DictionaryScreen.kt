package com.codework.dailyenglish.presentation.screen.dictionary

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.codework.dailyenglish.presentation.components.StateWrapper
import com.codework.dailyenglish.presentation.screen.dictionary.components.*
import com.codework.dailyenglish.presentation.screen.dictionary.mvi.DictionaryEvent
import com.codework.dailyenglish.presentation.screen.dictionary.mvi.DictionaryState
import com.codework.dailyenglish.presentation.theme.AppColors
import com.codework.dailyenglish.presentation.theme.AppTextSizes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DictionaryScreen(
    state: DictionaryState,
    onEvent: (DictionaryEvent) -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        containerColor = AppColors.AppBackground,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Dictionary",
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
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White.copy(alpha = 0.9f)
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(AppColors.AppBackground)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Spacer(modifier = Modifier.height(26.dp))
                DictionarySearchBar(
                    query = state.query,
                    onQueryChange = { onEvent(DictionaryEvent.OnSearchQueryChange(it)) },
                    onClear = { onEvent(DictionaryEvent.OnClearSearch) }
                )

                StateWrapper(
                    uiState = state.searchResults,
                    modifier = Modifier.fillMaxSize()
                ) { data ->

                    val currentList = data ?: emptyList()

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                    ) {

                        if (state.query.isNotEmpty()) {

                            if (currentList.isEmpty()) {
                                item {
                                    EmptySearchResult(state.query)
                                }
                            } else {
                                items(currentList) { word ->
                                    WordResultCard(
                                        word = word,
                                        onClick = {
                                            onEvent(DictionaryEvent.OnWordClick(word))
                                        },
                                        onPlayAudioUk = {
                                            onEvent(DictionaryEvent.OnPlayAudio(word.word, "UK"))
                                        },
                                        onPlayAudioUs = {
                                            onEvent(DictionaryEvent.OnPlayAudio(word.word, "US"))
                                        },
                                        onToggleSave = {
                                            onEvent(DictionaryEvent.OnToggleSaveWord(word))
                                        }
                                    )
                                }
                            }

                        } else {
                            if (state.recentSearches.isNotEmpty()) {
                                item {
                                    SectionTitle("Recent Searches")
                                    Spacer(modifier = Modifier.height(8.dp))
                                    RecentSearchList(state.recentSearches)
                                }
                            }

                            item {
                                SectionTitle("Explore Topics")
                                Spacer(modifier = Modifier.height(12.dp))

                                val allTopics = state.topicsState.data ?: emptyList()

                                val displayList = allTopics.take(5)

                                Column {
                                    TopicList(
                                        topics = displayList,
                                        onEvent = onEvent
                                    )

                                    if (allTopics.size > 5) {
                                        TextButton(
                                            onClick = { onEvent(DictionaryEvent.OnExploreTopicsClick) },
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text("More Topics", fontWeight = FontWeight.Bold)
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
                                        }
                                    }
                                }
                            }

                            item {
                                Spacer(modifier = Modifier.height(8.dp))
                                DailyQuoteCard()
                            }
                        }
                        item {
                            Spacer(modifier = Modifier.height(80.dp))
                        }
                    }
                }
            }
        }
    }
}