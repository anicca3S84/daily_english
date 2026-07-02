package com.codework.dailyenglish.presentation.screen.topic

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.codework.dailyenglish.presentation.components.StateWrapper
import com.codework.dailyenglish.presentation.screen.topic.components.TopicItemCard
import com.codework.dailyenglish.presentation.screen.topic.mvi.TopicEvent
import com.codework.dailyenglish.presentation.screen.topic.mvi.TopicState
import com.codework.dailyenglish.presentation.theme.AppColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopicScreen(
    state: TopicState,
    onEvent: (TopicEvent) -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        containerColor = AppColors.AppBackground,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Choose Topic", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { onBackClick() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                }
            )
        }
    ) { padding ->
        StateWrapper(uiState = state.topics, modifier = Modifier.padding(padding)) { topics ->
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(topics ?: emptyList()) { topic ->
                    TopicItemCard(topic = topic, onClick = { onEvent(TopicEvent.OnTopicClick(topic)) })
                }
            }
        }
    }
}