package com.codework.dailyenglish.presentation.screen.ai_speaking

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.codework.dailyenglish.presentation.components.AppConfirmDialog
import com.codework.dailyenglish.presentation.components.AppTopBar
import com.codework.dailyenglish.presentation.components.StateWrapper
import com.codework.dailyenglish.presentation.screen.ai.components.EnhancedMessageBubble
import com.codework.dailyenglish.presentation.screen.ai.components.HistoryDrawerContent
import com.codework.dailyenglish.presentation.screen.ai.components.ModernTypingIndicator
import com.codework.dailyenglish.presentation.screen.ai_speaking.components.SpeakingInputBar
import com.codework.dailyenglish.presentation.screen.ai_speaking.components.SuggestionDropdown
import com.codework.dailyenglish.presentation.screen.ai_speaking.mvi.AiSpeakingEvent
import com.codework.dailyenglish.presentation.screen.ai_speaking.mvi.AiSpeakingState
import com.codework.dailyenglish.presentation.screen.subscription.SubscriptionPopup
import com.codework.dailyenglish.presentation.theme.AppColors
import com.codework.dailyenglish.presentation.theme.AppTextSizes
import kotlinx.coroutines.launch

@Composable
fun AiSpeakingScreen(
    state: AiSpeakingState,
    onEvent: (AiSpeakingEvent) -> Unit,
    onBackClick: () -> Unit
) {
    val listState = rememberLazyListState()
    val context = LocalContext.current

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    DisposableEffect(context) {
        onDispose {
            onEvent(AiSpeakingEvent.OnStopRecording)
        }
    }

    val recordAudioPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            onEvent(AiSpeakingEvent.OnToggleRecording(context))
        } else {
            Toast.makeText(
                context,
                "Microphone permission is required for speaking practice.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    val handleMicClick = {
        val hasPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED

        if (hasPermission) {
            onEvent(AiSpeakingEvent.OnToggleRecording(context))
        } else {
            recordAudioPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }
    }

    if (state.isShowLimitConfirmDialog) {
        AppConfirmDialog(
            title = "Speaking Limit Reached",
            message = "You have used all of your free speaking practice sessions for today. Please upgrade to Premium to continue practicing without limits!",
            confirmText = "Upgrade Now",
            cancelText = "Maybe Later",
            onConfirm = { onEvent(AiSpeakingEvent.OnConfirmLimitUpgrade) },
            onDismiss = { onEvent(AiSpeakingEvent.OnDismissLimitConfirmDialog) }
        )
    }

    if (state.isShowPremiumUpgradeDialog) {
        SubscriptionPopup(
            visible = true,
            onDismiss = { onEvent(AiSpeakingEvent.OnDismissSubscriptionPopup) }
        )
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            HistoryDrawerContent(
                sessions = state.historySessions,
                onNewChatClick = {
                    onEvent(AiSpeakingEvent.OnNewChat)
                    scope.launch { drawerState.close() }
                },
                onSessionClick = { id ->
                    onEvent(AiSpeakingEvent.OnLoadSession(id))
                    scope.launch { drawerState.close() }
                }
            )
        }
    ) {
        Scaffold(
            containerColor = Color(0xFFF8FAFC),
            topBar = {
                AppTopBar(
                    onBackClick = onBackClick,
                    onMenuClick = { scope.launch { drawerState.open() } },
                    titleContent = {
                        Text(
                            text = state.uiState.data?.topicName.orEmpty().ifBlank { "Speaking Partner" },
                            fontWeight = FontWeight.Bold,
                            fontSize = AppTextSizes.TextSizeTitle,
                            textAlign = TextAlign.Center,
                            color = AppColors.TextMain
                        )
                    }
                )
            },
            bottomBar = {
                state.uiState.data?.let { data ->
                    Column(
                        modifier = Modifier
                            .navigationBarsPadding()
                            .fillMaxWidth()
                            .background(Color.Transparent)
                    ) {
                        if (state.isSuggestionsVisible && data.suggestions.isNotEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 24.dp, vertical = 8.dp)
                            ) {
                                SuggestionDropdown(
                                    suggestions = data.suggestions,
                                    onDismiss = { onEvent(AiSpeakingEvent.OnToggleSuggestions) },
                                    onSelect = { selectedText ->
                                        onEvent(AiSpeakingEvent.OnSendMessage(selectedText))
                                    }
                                )
                            }
                        }

                        SpeakingInputBar(
                            isRecording = state.isRecording,
                            onToggleRecording = { handleMicClick() },
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
                    .padding(top = padding.calculateTopPadding()),
                onRetry = { onEvent(AiSpeakingEvent.OnRetryChat) }
            ) { config ->
                val data = config ?: return@StateWrapper

                LaunchedEffect(key1 = data.messages.size, key2 = state.uiState.isRefreshing) {
                    if (data.messages.isNotEmpty()) {
                        val targetIndex = if (state.uiState.isRefreshing) data.messages.size else data.messages.size - 1
                        listState.animateScrollToItem(targetIndex)
                    }
                }

                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        top = 16.dp,
                        bottom = padding.calculateBottomPadding() + 16.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(data.messages) { msg ->
                        val isTranslated = state.activeTranslations.contains(msg.text)
                        val displayedText = if (isTranslated) {
                            state.translatedMessages[msg.text] ?: msg.text
                        } else {
                            msg.text
                        }

                        EnhancedMessageBubble(
                            message = msg,
                            showFeatures = true,
                            isTranslated = isTranslated,
                            displayedText = displayedText,
                            onTranslateClick = {
                                onEvent(AiSpeakingEvent.OnTranslateMessage(msg))
                            },
                            onSpeakClick = {
                                onEvent(AiSpeakingEvent.OnPlayMessageAudio(msg.text))
                            }
                        )
                    }

                    if (state.uiState.isRefreshing) {
                        item { ModernTypingIndicator() }
                    }
                }
            }
        }
    }
}