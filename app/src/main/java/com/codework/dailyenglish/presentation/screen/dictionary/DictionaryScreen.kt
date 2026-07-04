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
import androidx.compose.ui.platform.LocalFocusManager
import com.codework.dailyenglish.domain.dictionary.model.RecentWord
import com.codework.dailyenglish.domain.vocabulary.model.VocabTopic
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.codework.dailyenglish.domain.dictionary.model.Word
import com.codework.dailyenglish.presentation.theme.AppColors
import com.codework.dailyenglish.presentation.theme.AppTextSizes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DictionaryScreen(
    state: DictionaryState,
    onEvent: (DictionaryEvent) -> Unit,
    onBackClick: () -> Unit
) {
    val focusManager = LocalFocusManager.current

    Scaffold(
        containerColor = Color(0xFFF8FAFC), // Nền xám nhạt nhẹ giống thiết kế thực tế
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
                            tint = Color(0xFF1E293B)
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Spacer(modifier = Modifier.height(16.dp))

                // Thanh tìm kiếm
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
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        if (state.query.isNotEmpty()) {
                            // Khi đang GÕ TÌM KIẾM
                            if (currentList.isEmpty()) {
                                item {
                                    EmptySearchResult(state.query)
                                }
                            } else {
                                itemsIndexed(currentList) { index, word ->
                                    WordResultCard(
                                        word = word,
                                        index = index,
                                        onClick = {
                                            focusManager.clearFocus()
                                            onEvent(DictionaryEvent.OnWordClick(word))
                                        },
                                        onPlayAudioUk = {

                                        },
                                        onPlayAudioUs = {
                                            onEvent(DictionaryEvent.OnPlayAudio(word, "US"))
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
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Recent Searches",
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                        Text(
                                            text = "Clear all",
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 14.sp,
                                            color = Color(0xFF3B82F6),
                                            modifier = Modifier.clickable {
                                                onEvent(DictionaryEvent.OnClearRecentSearches)
                                            }
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))

                                    RecentSearchList(
                                        items = state.recentSearches,
                                        onItemClick = { recentWord ->
                                            onEvent(DictionaryEvent.OnRecentSearchClick(recentWord))
                                        },
                                        onDeleteClick = { wordId ->
                                            onEvent(DictionaryEvent.OnDeleteRecentSearch(wordId))
                                        }
                                    )
                                }
                            }

                            item {
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Explore Topics",
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Row(
                                        modifier = Modifier.clickable {
                                            onEvent(DictionaryEvent.OnExploreTopicsClick)
                                        },
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "See All Topics",
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 14.sp,
                                            color = Color(0xFF3B82F6)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                            contentDescription = null,
                                            tint = Color(0xFF3B82F6),
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(12.dp))

                                val allTopics = state.topicsState.data ?: emptyList()
                                val displayList = allTopics.take(6)

                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                    contentPadding = PaddingValues(horizontal = 4.dp)
                                ) {
                                    items(displayList) { topic ->
                                        TopicCard(
                                            topic = topic,
                                            onClick = {
                                                onEvent(DictionaryEvent.OnTopicClick(topic.id, topic.name))
                                            }
                                        )
                                    }
                                }
                            }


                            item {
//                                WordOfTheDayCard(
//                                    word = "Serendipity",
//                                    ipa = "/,ser.ən'dɪp.ə.ti/",
//                                    definition = "n. The occurrence of events by chance in a happy or beneficial way.",
//                                    onPlayAudio = {
//                                        onEvent(DictionaryEvent.OnPlayAudio(Word(), "US"))
//                                    },
//                                    onLearnMoreClick = {
//                                        onEvent(DictionaryEvent.OnWordOfTheDayClick("Serendipity"))
//                                    }
//                                )
                            }

                            // 4. Daily Inspiration (Trước đây là DailyQuoteCard)
                            item {
                                DailyInspirationCard(
                                    quote = "Learning never exhausts the mind.",
                                    author = "Leonardo da Vinci"
                                )
                            }
                        }
                        item {
                            Spacer(modifier = Modifier.height(60.dp))
                        }
                    }
                }
            }
        }
    }
}




