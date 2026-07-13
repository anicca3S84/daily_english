package com.codework.dailyenglish.presentation.screen.ai_speaking

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.codework.dailyenglish.presentation.components.StateWrapper
import com.codework.dailyenglish.presentation.screen.ai.components.EnhancedMessageBubble
import com.codework.dailyenglish.presentation.screen.ai.components.ModernTypingIndicator
import com.codework.dailyenglish.presentation.screen.ai_speaking.components.SpeakingInputBar
import com.codework.dailyenglish.presentation.screen.ai_speaking.components.SuggestionDropdown
import com.codework.dailyenglish.presentation.screen.ai_speaking.mvi.AiSpeakingEvent
import com.codework.dailyenglish.presentation.screen.ai_speaking.mvi.AiSpeakingState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiSpeakingScreen(
    state: AiSpeakingState,
    onEvent: (AiSpeakingEvent) -> Unit,
    onBackClick: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val listState = rememberLazyListState()
    val context = LocalContext.current

    DisposableEffect(context) {
        onDispose {
            onEvent(AiSpeakingEvent.OnStopRecording)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color(0xFFF8FAFC),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = state.uiState.data?.topicName.orEmpty().ifBlank { "Speaking Partner" },
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color(0xFF1E293B)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onBackClick() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFF1E293B)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More",
                            tint = Color(0xFF1E293B)
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        bottomBar = {
            state.uiState.data?.let { data ->
                Column(modifier = Modifier.navigationBarsPadding()) {
                    if (state.isSuggestionsVisible && data.suggestions.isNotEmpty()) {
                        SuggestionDropdown(
                            suggestions = data.suggestions,
                            onDismiss = { onEvent(AiSpeakingEvent.OnToggleSuggestions) },
                            onSelect = { selectedText ->
                                onEvent(AiSpeakingEvent.OnSendMessage(selectedText))
                            }
                        )
                    }

                    SpeakingInputBar(
                        isRecording = state.isRecording,
                        onToggleRecording = { onEvent(AiSpeakingEvent.OnToggleRecording(context)) },
                        onShowSuggestions = { onEvent(AiSpeakingEvent.OnToggleSuggestions) },
                        onSendText = { onEvent(AiSpeakingEvent.OnSendMessage(it)) }
                    )
                }
            }
        }
    ) { padding ->
        StateWrapper(
            uiState = state.uiState,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            snackbarHostState = snackbarHostState,
            onRetry = { onEvent(AiSpeakingEvent.OnRetryChat) }
        ) { config ->
            val data = config ?: return@StateWrapper

            LaunchedEffect(key1 = data.messages.size, key2 = state.uiState.isRefreshing) {
                if (data.messages.isNotEmpty()) {
                    // Nếu AI đang gõ (isRefreshing), cuộn hẳn xuống để lộ Typing Indicator
                    val targetIndex = if (state.uiState.isRefreshing) data.messages.size else data.messages.size - 1
                    listState.animateScrollToItem(targetIndex)
                }
            }

            LazyColumn(
                state = listState, // Gán listState vào đây
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(data.messages) { msg ->
                    EnhancedMessageBubble(message = msg)
                }

                if (state.uiState.isRefreshing) {
                    item { ModernTypingIndicator() }
                }
            }
        }
    }
}