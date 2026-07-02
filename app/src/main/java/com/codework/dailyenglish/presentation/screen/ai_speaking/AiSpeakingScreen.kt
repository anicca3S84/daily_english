package com.codework.dailyenglish.presentation.screen.ai_speaking
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.codework.dailyenglish.presentation.screen.ai.components.EnhancedMessageBubble
import com.codework.dailyenglish.presentation.screen.ai.components.ModernTypingIndicator
import com.codework.dailyenglish.presentation.screen.ai_speaking.components.SpeakingInputBar
import com.codework.dailyenglish.presentation.screen.ai_speaking.components.SuggestionDropdown
import com.codework.dailyenglish.presentation.screen.ai_speaking.mvi.AiSpeakingEvent
import com.codework.dailyenglish.presentation.screen.ai_speaking.mvi.AiSpeakingState
import com.codework.dailyenglish.presentation.theme.AppColors
import com.codework.dailyenglish.presentation.theme.AppTextSizes
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiSpeakingScreen(
    state: AiSpeakingState,
    onEvent: (AiSpeakingEvent) -> Unit,
    onBackClick: () -> Unit
) {
    var showSuggestions by remember { mutableStateOf(false) }
    Scaffold(
        containerColor = Color(0xFFF8FAFC),
        topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "Speaking",
                            fontWeight = FontWeight.Bold,
                            fontSize = AppTextSizes.TextSizeTitle,
                            textAlign = TextAlign.Center,
                            color = AppColors.TextMain
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { onBackClick() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = AppColors.TextMain
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "More",
                                tint = AppColors.TextMain
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.White.copy(alpha = 0.9f)
                    )
                )
        },
        bottomBar = {
            Column {
                // Dropdown hiện ra khi bấm bóng đèn
                if (showSuggestions) {
                    SuggestionDropdown(
                        onDismiss = { showSuggestions = false },
                        onSelect = {
                            onEvent(AiSpeakingEvent.OnSendMessage(it))
                            showSuggestions = false
                        }
                    )
                }

                SpeakingInputBar(
                    isRecording = state.isRecording,
                    onToggleRecording = { onEvent(AiSpeakingEvent.OnToggleRecording) },
                    onShowSuggestions = { showSuggestions = !showSuggestions }, // Mở gợi ý
                    onSendText = { onEvent(AiSpeakingEvent.OnSendMessage(it)) }
                )
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(state.messages) { msg ->
                EnhancedMessageBubble(message = msg)
            }
            if (state.isTyping) {
                item { ModernTypingIndicator() }
            }
        }
    }
}