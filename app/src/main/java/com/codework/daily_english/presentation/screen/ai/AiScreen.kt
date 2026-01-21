package com.codework.daily_english.presentation.screen.ai

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.codework.daily_english.presentation.components.AppTopBar
import com.codework.daily_english.presentation.components.BaseScreen
import com.codework.daily_english.presentation.components.UiState
import com.codework.daily_english.presentation.model.ChatMessage
import com.codework.daily_english.presentation.model.ChatSession
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiScreen(
    viewModel: AiViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    var inputText by remember { mutableStateOf("") }

    val suggestions = listOf("Correct my grammar ✍️", "Practice Speaking 🗣️", "Translate 🌐", "Explain Idioms 💡")

    if (state is UiState.Success) {
        val data = (state as UiState.Success).data
        LaunchedEffect(data.messages.size, data.isTyping) {
            if (data.messages.isNotEmpty()) {
                listState.animateScrollToItem(data.messages.size)
            }
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            if (state is UiState.Success) {
                val data = (state as UiState.Success).data
                HistoryDrawerContent(
                    sessions = data.historySessions,
                    onNewChatClick = {
                        viewModel.onEvent(AiEvent.OnNewChat)
                        scope.launch { drawerState.close() }
                    },
                    onSessionClick = { id ->
                        viewModel.onEvent(AiEvent.OnLoadSession(id))
                        scope.launch { drawerState.close() }
                    }
                )
            }
        }
    ) {
        Scaffold(
            // Vẫn giữ 0 để tránh xung đột với các màn hình khác như bạn mong muốn
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            topBar = {
                AppTopBar(
                    onBackClick = onBackClick,
                    onMenuClick = { scope.launch { drawerState.open() } },
                    titleContent = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.wrapContentWidth()
                                .statusBarsPadding()
                        ) {
                            // Avatar (Sử dụng Box cẩn thận)
                            Box(
                                modifier = Modifier.size(40.dp), // Chỉnh nhỏ lại 1 chút
                                contentAlignment = Alignment.Center
                            ) {
                                // Code avatar của bạn ở đây
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column {
                                Text(
                                    "Gemini Tutor",
                                    fontWeight = FontWeight.Black,
                                    fontSize = 16.sp, // Đừng để 18sp nếu title quá dài
                                    maxLines = 1
                                )
                                Text("Always active", fontSize = 10.sp, color = Color.Gray)
                            }
                        }
                    }
                )
            },
            bottomBar = {
                if (state is UiState.Success) {
                    val data = (state as UiState.Success).data
                    Column(
                        modifier = Modifier
                            .background(Color.White)
                            .navigationBarsPadding() // Tránh nút điều hướng Android
                            .imePadding() // Đẩy cụm này lên khi hiện phím
                    ) {
                        if (data.messages.isEmpty()) {
                            SuggestionRow(suggestions) { text ->
                                viewModel.onEvent(AiEvent.OnSendMessage(text))
                            }
                        }

                        ModernInputBar(
                            value = inputText,
                            onValueChange = { inputText = it },
                            onSend = {
                                viewModel.onEvent(AiEvent.OnSendMessage(inputText))
                                inputText = ""
                            },
                            isEnabled = !data.isTyping
                        )
                    }
                }
            }
        ) { innerPadding -> // Bỏ paddingValues vì ta sẽ tự quản lý khoảng cách
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = innerPadding.calculateBottomPadding())
                    .background(Color(0xFFF8F9FD))
            ) {
                // Background trang trí
                Canvas(modifier = Modifier.fillMaxSize().blur(50.dp)) {
                    drawCircle(Color(0xFF8E2DE2).copy(alpha = 0.03f), radius = 600f, center = center.copy(x = 0f))
                    drawCircle(Color(0xFF4FACFE).copy(alpha = 0.03f), radius = 600f, center = center.copy(x = size.width))
                }

                BaseScreen(state = state) { data ->
                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .fillMaxSize()
                            // SỬA TẠI ĐÂY: Cách TopBar 72dp + Status Bar
                            .statusBarsPadding()
                            .padding(top = 72.dp),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        if (data.messages.isEmpty()) {
                            item { EmptyChatPlaceholder() }
                        }

                        items(data.messages) { msg ->
                            EnhancedMessageBubble(message = msg)
                        }

                        if (data.isTyping) {
                            item { ModernTypingIndicator() }
                        }

                        // Thêm một khoảng trống ảo cuối danh sách để tin nhắn không bị che
                        item { Spacer(modifier = Modifier.height(10.dp)) }
                    }
                }
            }
        }
    }
}

// ==========================================
// CÁC COMPONENT GIAO DIỆN (UI COMPONENTS)
// ==========================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiTopBarPro(onBackClick: () -> Unit, onMenuClick: () -> Unit) {
    Surface(
        color = Color.White.copy(alpha = 0.98f), // Màu trắng hơi trong suốt nhẹ
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = 0.dp // Tắt shadow mặc định để dùng Divider tuỳ chỉnh
    ) {
        Column {
            TopAppBar(
                windowInsets = WindowInsets(0), // Giữ nguyên theo yêu cầu của bạn
                modifier = Modifier.height(72.dp),
                title = {
                    Box(
                        modifier = Modifier.fillMaxHeight(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // --- AVATAR PRO (Hiệu ứng 2 vòng viền) ---
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(46.dp) // Tăng nhẹ kích thước
                                    .border(1.5.dp, Color(0xFFF3F4F6), CircleShape) // Viền ngoài xám nhạt
                                    .padding(3.dp) // Khoảng cách giữa 2 viền
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(
                                            brush = Brush.linearGradient(
                                                colors = listOf(Color(0xFF4FACFE), Color(0xFF8E2DE2))
                                            ),
                                            shape = CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AutoAwesome,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }

                                // Chấm xanh báo Online đè lên góc avatar (Giống Messenger/Insta)
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.BottomEnd)
                                        .size(12.dp)
                                        .background(Color.White, CircleShape)
                                        .padding(2.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(Color(0xFF4CAF50), CircleShape)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            // --- TEXT CONTENT ---
                            Column(verticalArrangement = Arrangement.Center) {
                                Text(
                                    text = "Gemini Tutor",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.ExtraBold, // Đậm hơn
                                    color = Color(0xFF111827), // Màu đen than (Dark Charcoal) sang trọng
                                    letterSpacing = (-0.5).sp
                                )
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "Always active",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color(0xFF6B7280), // Màu xám trung tính
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    // Badge PRO nhỏ gọn
                                    Surface(
                                        color = Color(0xFFEFF6FF), // Xanh dương siêu nhạt
                                        shape = RoundedCornerShape(4.dp),
                                        border = BorderStroke(0.5.dp, Color(0xFFBFDBFE))
                                    ) {
                                        Text(
                                            text = "PRO",
                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp),
                                            style = MaterialTheme.typography.labelSmall,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF3B82F6)
                                        )
                                    }
                                }
                            }
                        }
                    }
                },
                navigationIcon = {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(64.dp), // Tăng vùng bấm
                        contentAlignment = Alignment.Center
                    ) {
                        // Nút Back tròn, có viền mờ
                        Surface(
                            onClick = onBackClick,
                            shape = CircleShape,
                            color = Color.White,
                            border = BorderStroke(1.dp, Color(0xFFE5E7EB)), // Viền xám rất mờ
                            modifier = Modifier.size(40.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back",
                                    tint = Color(0xFF374151), // Xám đậm
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                },
                actions = {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(end = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        // Nút Menu tròn, có viền mờ
                        Surface(
                            onClick = onMenuClick,
                            shape = CircleShape,
                            color = Color.White,
                            border = BorderStroke(1.dp, Color(0xFFE5E7EB)),
                            modifier = Modifier.size(40.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Menu,
                                    contentDescription = "Menu",
                                    tint = Color(0xFF374151),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )

            // Đường kẻ Gradient mờ dần 2 đầu (Tinh tế hơn đường kẻ đen đặc)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color(0xFFE5E7EB), // Xám nhạt ở giữa
                                Color.Transparent
                            )
                        )
                    )
            )
        }
    }
}

@Composable
fun ModernInputBar(
    value: String,
    onValueChange: (String) -> Unit,
    onSend: () -> Unit,
    isEnabled: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Khung nhập liệu
        Surface(
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(24.dp),
            color = Color(0xFFF2F4F7),
            border = BorderStroke(1.dp, Color(0xFFE4E7EC))
        ) {
            TextField(
                value = value,
                onValueChange = onValueChange,
                placeholder = { Text("Type a message...", color = Color.Gray) },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                maxLines = 4,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Nút gửi
        val canSend = value.isNotBlank() && isEnabled
        IconButton(
            onClick = onSend,
            enabled = canSend,
            modifier = Modifier
                .size(48.dp)
                .background(
                    brush = if (canSend)
                        Brush.linearGradient(listOf(Color(0xFF4FACFE), Color(0xFF8E2DE2)))
                    else
                        Brush.linearGradient(listOf(Color.LightGray, Color.LightGray)),
                    shape = CircleShape
                )
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Send,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

@Composable
fun EnhancedMessageBubble(message: ChatMessage) {
    val isUser = message.isUser
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
    ) {
        Surface(
            shape = RoundedCornerShape(
                topStart = 20.dp, topEnd = 20.dp,
                bottomStart = if (isUser) 20.dp else 4.dp,
                bottomEnd = if (isUser) 4.dp else 20.dp
            ),
            color = if (isUser) Color.Transparent else Color.White,
            shadowElevation = if (isUser) 0.dp else 1.dp
        ) {
            Box(
                modifier = if (isUser) Modifier.background(
                    brush = Brush.linearGradient(listOf(Color(0xFF4FACFE), Color(0xFF8E2DE2)))
                ) else Modifier.background(Color.White)
            ) {
                Text(
                    text = message.text,
                    modifier = Modifier.padding(12.dp),
                    color = if (isUser) Color.White else Color(0xFF333333),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun SuggestionRow(suggestions: List<String>, onClick: (String) -> Unit) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(suggestions) { text ->
            Surface(
                onClick = { onClick(text) },
                shape = RoundedCornerShape(12.dp),
                color = Color.White,
                border = BorderStroke(1.dp, Color(0xFFEEEEEE))
            ) {
                Text(
                    text = text,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.DarkGray
                )
            }
        }
    }
}

@Composable
fun EmptyChatPlaceholder() {
    Column(
        modifier = Modifier.fillMaxWidth().padding(top = 80.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.AutoAwesome,
            contentDescription = null,
            modifier = Modifier
                .size(64.dp)
                .graphicsLayer { alpha = 0.15f },
            tint = Color(0xFF8E2DE2)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text("Gemini English Tutor", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
        Text("I can help you practice and correct grammar", color = Color.Gray, fontSize = 14.sp)
    }
}

@Composable
fun ModernTypingIndicator() {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(start = 4.dp)) {
        CircularProgressIndicator(modifier = Modifier.size(12.dp), strokeWidth = 2.dp, color = Color(0xFF8E2DE2))
        Spacer(modifier = Modifier.width(8.dp))
        Text("Typing...", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
    }
}

@Composable
fun HistoryDrawerContent(
    sessions: List<ChatSession>,
    onNewChatClick: () -> Unit,
    onSessionClick: (String) -> Unit
) {
    ModalDrawerSheet(drawerContainerColor = Color.White) {
        Column(modifier = Modifier.padding(20.dp).fillMaxHeight()) {
            Text("Chat History", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onNewChatClick,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
            ) {
                Icon(Icons.Default.Add, null)
                Text("New Conversation", modifier = Modifier.padding(start = 8.dp))
            }
            Spacer(modifier = Modifier.height(20.dp))
            LazyColumn {
                items(sessions) { session ->
                    NavigationDrawerItem(
                        label = { Text(session.title, maxLines = 1) },
                        selected = false,
                        onClick = { onSessionClick(session.id) },
                        icon = { Icon(Icons.Default.History, null) },
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }
        }
    }
}