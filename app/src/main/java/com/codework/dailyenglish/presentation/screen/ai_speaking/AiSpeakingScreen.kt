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
import androidx.core.content.ContextCompat
import com.codework.dailyenglish.presentation.components.AppConfirmDialog
import com.codework.dailyenglish.presentation.components.StateWrapper
import com.codework.dailyenglish.presentation.screen.ai.components.EnhancedMessageBubble
import com.codework.dailyenglish.presentation.screen.ai.components.ModernTypingIndicator
import com.codework.dailyenglish.presentation.screen.ai_speaking.components.SpeakingInputBar
import com.codework.dailyenglish.presentation.screen.ai_speaking.components.SuggestionDropdown
import com.codework.dailyenglish.presentation.screen.ai_speaking.mvi.AiSpeakingEvent
import com.codework.dailyenglish.presentation.screen.ai_speaking.mvi.AiSpeakingState
import com.codework.dailyenglish.presentation.screen.subscription.SubscriptionPopup

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

    // 1. Launcher yêu cầu quyền ghi âm
    val recordAudioPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // Nếu được cấp quyền, bắt đầu/dừng ghi âm
            onEvent(AiSpeakingEvent.OnToggleRecording(context))
        } else {
            // Nếu bị từ chối
            Toast.makeText(
                context,
                "Microphone permission is required for speaking practice.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    // 2. Hàm xử lý click nút Micro
    val handleMicClick = {
        val hasPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED

        if (hasPermission) {
            // Đã có quyền -> chạy luôn
            onEvent(AiSpeakingEvent.OnToggleRecording(context))
        } else {
            // Chưa có quyền -> bật hộp thoại xin quyền
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
                        // 3. Thay thế lệnh gọi trực tiếp bằng hàm handleMicClick
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
            snackbarHostState = snackbarHostState,
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
                    EnhancedMessageBubble(message = msg)
                }

                if (state.uiState.isRefreshing) {
                    item { ModernTypingIndicator() }
                }
            }
        }
    }
}