package com.codework.dailyenglish.presentation.screen.topic

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codework.dailyenglish.presentation.components.StateWrapper
import com.codework.dailyenglish.presentation.screen.dictionary.components.DictionarySearchBar
import com.codework.dailyenglish.presentation.screen.topic.components.TopicItemCard
import com.codework.dailyenglish.presentation.screen.topic.mvi.TopicEvent
import com.codework.dailyenglish.presentation.screen.topic.mvi.TopicState
import com.codework.dailyenglish.presentation.theme.AppColors
import com.codework.dailyenglish.presentation.theme.AppTextSizes
import com.codework.dailyenglish.presentation.screen.practice.components.AiTopicCard


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopicScreen(
    state: TopicState,
    onEvent: (TopicEvent) -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        containerColor = Color(0xFFFAFAFA),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Choose Topic",
                        fontWeight = FontWeight.Bold,
                        fontSize = AppTextSizes.TextSizeTitle,
                        color = AppColors.TextMain
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onBackClick() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = AppColors.TextMain)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            DictionarySearchBar(
                query = state.searchQuery,
                onQueryChange = { query -> onEvent(TopicEvent.OnSearchQueryChange(query)) },
                onClear = { onEvent(TopicEvent.OnSearchQueryChange("")) }
            )

            StateWrapper(
                uiState = state.topics,
                modifier = Modifier.weight(1f)
            ) { topics ->
                val filteredTopics = remember(topics, state.searchQuery) {
                    topics?.filter { topic ->
                        topic.name.contains(state.searchQuery, ignoreCase = true)
                    } ?: emptyList()
                }

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(filteredTopics) { topic ->
                        if (state.isDictionary) {
                            TopicItemCard(
                                topic = topic,
                                showWordCount = true,
                                onClick = { onEvent(TopicEvent.OnTopicClick(topic)) }
                            )
                        } else {
                            AiTopicCard(
                                topic = topic,
                                onClick = { onEvent(TopicEvent.OnTopicClick(topic)) }
                            )
                        }
                    }
                }
            }
        }
    }
}