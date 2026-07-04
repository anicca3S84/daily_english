package com.codework.dailyenglish.presentation.screen.ai

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.codework.dailyenglish.presentation.components.AppTopBar
import com.codework.dailyenglish.presentation.screen.ai.components.AutoScrollingSuggestionSection
import com.codework.dailyenglish.presentation.screen.ai.components.EmptyChatPlaceholder
import com.codework.dailyenglish.presentation.screen.ai.components.EnhancedMessageBubble
import com.codework.dailyenglish.presentation.screen.ai.components.HistoryDrawerContent
import com.codework.dailyenglish.presentation.screen.ai.components.ModernInputBar
import com.codework.dailyenglish.presentation.screen.ai.components.ModernTypingIndicator
import com.codework.dailyenglish.presentation.screen.ai.mvi.AiEvent
import com.codework.dailyenglish.presentation.screen.ai.mvi.AiState
import com.codework.dailyenglish.presentation.theme.AppColors
import com.codework.dailyenglish.presentation.theme.AppTextSizes
import kotlinx.coroutines.launch
@Composable
fun AiScreen(
    state: AiState,
    onEvent: (AiEvent) -> Unit,
    onBackClick: () -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    var inputText by remember { mutableStateOf("") }

    val suggestions = listOf(
        "Correct my grammar ✍️", "Practice Speaking 🗣️", "Translate to English 🌐",
        "Explain Idioms 💡", "Write an email 📧", "IELTS Writing tips 📝",
        "Daily conversation 👋", "Vocabulary quiz 🧠", "Roleplay interview 👔"
    )

    LaunchedEffect(state.messages.size, state.isTyping) {
        if (state.messages.isNotEmpty()) {
            listState.animateScrollToItem(state.messages.size)
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            HistoryDrawerContent(
                sessions = state.historySessions,
                onNewChatClick = { onEvent(AiEvent.OnNewChat); scope.launch { drawerState.close() } },
                onSessionClick = { id -> onEvent(AiEvent.OnLoadSession(id)); scope.launch { drawerState.close() } }
            )
        }
    ) {
        Scaffold(
            topBar = {
                AppTopBar(
                    onBackClick = onBackClick,
                    onMenuClick = { scope.launch { drawerState.open() } },
                    titleContent = {
                            Text(
                                text = "Anicca AI",
                                fontWeight = FontWeight.Bold,
                                fontSize = AppTextSizes.TextSizeTitle,
                                textAlign = TextAlign.Center,
                                color = AppColors.TextMain
                            )
                    }
                )
            },
            bottomBar = {
                Column(
                    modifier = Modifier
                        .background(AppColors.AppBackground)
                        .navigationBarsPadding()
                        .imePadding()
                        .padding(bottom = 24.dp)
                ) {
                    if (state.messages.isEmpty()) {
                        AutoScrollingSuggestionSection(suggestions) { onEvent(AiEvent.OnSuggestionClick(it)) }
                    }

                    ModernInputBar(
                        value = inputText,
                        onValueChange = { inputText = it },
                        onSend = { onEvent(AiEvent.OnSendMessage(inputText)); inputText = "" },
                        isEnabled = !state.isTyping,
                        onAttachClick = { onEvent(AiEvent.OnImageClick) },
                        onMicClick = { onEvent(AiEvent.OnMicClick) }
                    )
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(AppColors.AppBackground)
                    .padding(innerPadding)
            ) {
                AnimatedVisibility(
                    visible = state.messages.isEmpty(),
                    enter = fadeIn(),
                    exit = fadeOut(),
                    modifier = Modifier.align(Alignment.Center)
                ) {
                    EmptyChatPlaceholder()
                }

                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(state.messages) { msg ->
                        EnhancedMessageBubble(message = msg)
                    }
                    if (state.isTyping) {
                        item { ModernTypingIndicator() }
                    }
                    item { Spacer(modifier = Modifier.height(10.dp)) }
                }
            }
        }
    }
}
