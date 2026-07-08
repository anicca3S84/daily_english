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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
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
import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

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

    // Khởi tạo bộ Launcher để xin quyền ghi âm
    val recordAudioPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // Nếu người dùng đồng ý cấp quyền, kích hoạt bật mic
            onEvent(AiEvent.OnMicToggle)
        } else {
            // Nếu từ chối, hiển thị thông báo nhắc nhở
            Toast.makeText(context, "Vui lòng cấp quyền ghi âm để sử dụng tính năng này", Toast.LENGTH_SHORT).show()
        }
    }

    // Hàm kiểm tra quyền trước khi toggle mic
    val handleMicClick = {
        val hasPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED

        if (hasPermission) {
            onEvent(AiEvent.OnMicToggle)
        } else {
            // Nếu chưa có quyền, tiến hành xin quyền
            recordAudioPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }
    }

    // Đồng bộ chữ thu âm lên khung nhập liệu
    LaunchedEffect(state.voiceCapturedText) {
        if (state.voiceCapturedText.isNotBlank()) {
            inputText = state.voiceCapturedText
        }
    }

    // Tự động cuộn và ẩn bàn phím khi có tin nhắn mới
    LaunchedEffect(state.messages.size) {
        if (state.messages.isNotEmpty()) {
            if (state.messages.last().isUser) {
                inputText = ""
                keyboardController?.hide()
            }
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
                        Text(text = "Anicca AI", fontWeight = FontWeight.Bold, color = AppColors.TextMain)
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
                        AutoScrollingSuggestionSection(suggestions = listOf(
                            "Correct my grammar ✍️", "Practice Speaking 🗣️", "Translate to English 🌐"
                        )) { onEvent(AiEvent.OnSuggestionClick(it)) }
                    }

                    ModernInputBar(
                        value = inputText,
                        onValueChange = { inputText = it },
                        onSend = {
                            if (state.isVoiceRecording) {
                                handleMicClick()
                            } else if (inputText.isNotBlank()) {
                                onEvent(AiEvent.OnSendMessage(inputText))
                            } else {
                                handleMicClick() // Nếu ô nhập trống thì bấm nút gửi sẽ kích hoạt xin quyền/bật Mic
                            }
                        },
                        isEnabled = !state.isTyping,
                        isVoiceRecording = state.isVoiceRecording,
                        onAttachClick = { onEvent(AiEvent.OnImageClick) },
                        onMicClick = { handleMicClick() } // Gọi hàm kiểm tra quyền ở đây thay vì onMicToggle trực tiếp
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
}
