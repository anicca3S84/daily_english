package com.codework.dailyenglish.presentation.screen.ai

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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.codework.dailyenglish.presentation.components.AppConfirmDialog
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
import com.codework.dailyenglish.presentation.screen.subscription.SubscriptionPopup
import kotlinx.coroutines.launch

@Composable
fun AiScreen(
    state: AiState,
    onEvent: (AiEvent) -> Unit,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    var inputText by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    if (state.isShowLimitConfirmDialog) {
        AppConfirmDialog(
            title = "Message Limit Reached",
            message = "You have used all of your free messages for today. Please upgrade to Premium to continue chatting without limits!",
            confirmText = "Upgrade Now",
            cancelText = "Maybe Later",
            onConfirm = { onEvent(AiEvent.OnConfirmLimitUpgrade) },
            onDismiss = { onEvent(AiEvent.OnDismissLimitConfirmDialog) }
        )
    }

    val recordAudioPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            onEvent(AiEvent.OnMicToggle(context))
        } else {
            Toast.makeText(
                context,
                "Please grant microphone permission to use this feature.",
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
            onEvent(AiEvent.OnMicToggle(context))
        } else {
            recordAudioPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }
    }

    LaunchedEffect(state.voiceCapturedText) {
        if (state.voiceCapturedText.isNotBlank()) {
            inputText = state.voiceCapturedText.take(state.maxCharLimit)
        }
    }

    LaunchedEffect(state.messages) {
        if (state.messages.isEmpty()) {
            inputText = ""
        } else if (state.messages.last().isUser) {
            inputText = ""
            keyboardController?.hide()
        }
        if (state.messages.isNotEmpty()) {
            listState.animateScrollToItem(state.messages.size)
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            HistoryDrawerContent(
                sessions = state.historySessions,
                onNewChatClick = {
                    onEvent(AiEvent.OnNewChat)
                    scope.launch { drawerState.close() }
                },
                onSessionClick = { id ->
                    onEvent(AiEvent.OnLoadSession(id))
                    scope.launch { drawerState.close() }
                }
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
                        AutoScrollingSuggestionSection(
                            suggestions = listOf(
                                "Practice daily conversation",
                                "Correct my grammar",
                                "Improve my writing",
                                "Learn 5 new words today",
                                "Prepare for an IELTS speaking test"
                            )
                        ) { onEvent(AiEvent.OnSuggestionClick(it)) }
                    }

                    ModernInputBar(
                        value = inputText,
                        onValueChange = { newValue ->
                            inputText = newValue.take(state.maxCharLimit)
                        },
                        onSend = {
                            if (state.isVoiceRecording) {
                                handleMicClick()
                            } else if (inputText.isNotBlank()) {
                                onEvent(AiEvent.OnSendMessage(inputText))
                            } else {
                                handleMicClick()
                            }
                        },
                        isEnabled = !state.isTyping,
                        isVoiceRecording = state.isVoiceRecording,
                        onAttachClick = { onEvent(AiEvent.OnImageClick) },
                        onMicClick = { handleMicClick() }
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
                if (state.messages.isEmpty()) {
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

    if (state.isShowPremiumUpgradeDialog) {
        SubscriptionPopup(
            visible = true,
            onDismiss = { onEvent(AiEvent.OnDismissSubscriptionPopup) }
        )
    }
}