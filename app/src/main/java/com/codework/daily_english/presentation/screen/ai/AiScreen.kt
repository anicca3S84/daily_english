package com.codework.daily_english.presentation.screen.ai

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codework.daily_english.presentation.components.AppTopBar
import com.codework.daily_english.presentation.components.BaseScreen
import com.codework.daily_english.presentation.components.UiState
import com.codework.daily_english.presentation.model.ChatMessage
import com.codework.daily_english.presentation.model.ChatSession
import com.codework.daily_english.presentation.theme.AppBackground
import com.codework.daily_english.presentation.theme.BluePrimary
import com.codework.daily_english.presentation.theme.MainGradient
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiScreen(
    state: UiState<AiState>, // SỬA: Đổi AiData thành AiState
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

    if (state is UiState.Success) {
        val data = state.data
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
                HistoryDrawerContent(
                    sessions = state.data.historySessions,
                    onNewChatClick = { onEvent(AiEvent.OnNewChat); scope.launch { drawerState.close() } },
                    onSessionClick = { id -> onEvent(AiEvent.OnLoadSession(id)); scope.launch { drawerState.close() } }
                )
            }
        }
    ) {
        Scaffold(
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            topBar = {
                AppTopBar(
                    onBackClick = onBackClick,
                    onMenuClick = { scope.launch { drawerState.open() } },
                    titleContent = {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .statusBarsPadding()
                                .padding(end = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Surface(shape = CircleShape, color = Color.Transparent, modifier = Modifier.size(36.dp)) {
                                Box(modifier = Modifier.fillMaxSize().background(brush = MainGradient), contentAlignment = Alignment.Center) {
                                    Text("A", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 18.sp)
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Anicca Tutor", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.Black)
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(Color(0xFF4CAF50)))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Always active", fontSize = 13.sp, color = Color.Gray)
                                }
                            }
                        }
                    }
                )
            },
            bottomBar = {
                if (state is UiState.Success) {
                    Column(
                        modifier = Modifier
                            .background(AppBackground)
                            .navigationBarsPadding()
                            .padding(bottom = 18.dp)
                    ) {
                        if (state.data.messages.isEmpty()) {
                            AutoScrollingSuggestionSection(suggestions) { onEvent(AiEvent.OnSuggestionClick(it)) }
                        }

                        ModernInputBar(
                            value = inputText,
                            onValueChange = { inputText = it },
                            onSend = { onEvent(AiEvent.OnSendMessage(inputText)); inputText = "" },
                            isEnabled = !state.data.isTyping,
                            onAttachClick = { onEvent(AiEvent.OnImageClick) },
                            onMicClick = { onEvent(AiEvent.OnMicClick) }
                        )
                    }
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(AppBackground)
                    .padding(bottom = innerPadding.calculateBottomPadding())
            ) {
                BaseScreen(state = state) { data ->
                    Box(modifier = Modifier.fillMaxSize()) {
                        AnimatedVisibility(
                            visible = data.messages.isEmpty(),
                            enter = fadeIn(),
                            exit = fadeOut(),
                            modifier = Modifier.align(Alignment.Center).padding(top = 50.dp)
                        ) {
                            EmptyChatPlaceholder()
                        }

                        LazyColumn(
                            state = listState,
                            modifier = Modifier.fillMaxSize().statusBarsPadding().padding(top = 72.dp),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(data.messages) { msg ->
                                EnhancedMessageBubble(message = msg)
                            }
                            if (data.isTyping) {
                                item { ModernTypingIndicator() }
                            }
                            item { Spacer(modifier = Modifier.height(10.dp)) }
                        }
                    }
                }
            }
        }
    }
}

// --- AUTO SCROLL SUGGESTIONS ---
@Composable
fun AutoScrollingSuggestionSection(suggestions: List<String>, onClick: (String) -> Unit) {
    val loopList = if (suggestions.size < 10) suggestions + suggestions + suggestions else suggestions
    val mid = loopList.size / 2
    val firstRow = loopList.take(mid)
    val secondRow = loopList.drop(mid)

    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        SuggestionRowWithAutoScroll(items = firstRow, reverseLayout = false, onClick = onClick)
        SuggestionRowWithAutoScroll(items = secondRow, reverseLayout = true, onClick = onClick)
    }
}

@Composable
fun SuggestionRowWithAutoScroll(items: List<String>, reverseLayout: Boolean = false, onClick: (String) -> Unit) {
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = if (reverseLayout) 0 else Int.MAX_VALUE / 2)

    LaunchedEffect(Unit) {
        while (true) {
            listState.scrollBy(if (reverseLayout) -1.5f else 1.5f)
            delay(10)
        }
    }

    LazyRow(
        state = listState,
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        userScrollEnabled = true,
        reverseLayout = reverseLayout
    ) {
        items(count = Int.MAX_VALUE) { index ->
            val item = items[index % items.size]
            Surface(
                onClick = { onClick(item) },
                shape = CircleShape,
                color = Color.White,
                border = BorderStroke(1.dp, BluePrimary.copy(alpha = 0.5f))
            ) {
                Text(
                    text = item,
                    modifier = Modifier.padding(horizontal = 18.dp, vertical = 12.dp),
                    fontWeight = FontWeight.Medium,
                    style = TextStyle(brush = MainGradient, fontSize = 13.sp),
                    maxLines = 1
                )
            }
        }
    }
}

// --- MODERN INPUT BAR ---
@Composable
fun ModernInputBar(
    value: String,
    onValueChange: (String) -> Unit,
    onSend: () -> Unit,
    isEnabled: Boolean,
    onAttachClick: () -> Unit,
    onMicClick: () -> Unit
) {
    val inputBackgroundColor = Color(0xFFF2F4F7)
    val isTyping = value.isNotBlank()

    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        // Nút đính kèm ảnh
        Box(
            modifier = Modifier.size(48.dp).clip(CircleShape).background(inputBackgroundColor).clickable { onAttachClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Add, "Add", tint = Color.Gray, modifier = Modifier.size(24.dp))
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Khung nhập liệu
        Surface(
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(24.dp),
            color = inputBackgroundColor,
            border = BorderStroke(1.dp, Color(0xFFE0E0E0)),
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                textStyle = TextStyle(fontSize = 16.sp, color = Color(0xFF263238), lineHeight = 24.sp),
                maxLines = 5,
                decorationBox = { innerTextField ->
                    Box(
                        contentAlignment = Alignment.CenterStart,
                        modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp).padding(horizontal = 16.dp, vertical = 4.dp)
                    ) {
                        if (value.isEmpty()) { Text("Message Anicca...", color = Color.Gray, fontSize = 16.sp) }
                        innerTextField()
                    }
                }
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Nút Send / Mic
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(brush = if (isTyping) MainGradient else Brush.linearGradient(listOf(Color(0xFF212121), Color(0xFF212121))))
                .clickable(enabled = isEnabled, onClick = { if (isTyping) onSend() else onMicClick() }),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (isTyping) Icons.AutoMirrored.Filled.Send else Icons.Default.Mic,
                contentDescription = if (isTyping) "Send" else "Voice",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

// --- CÁC COMPONENT PHỤ GIỮ NGUYÊN ---
@Composable
fun EmptyChatPlaceholder() {
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center, modifier = Modifier.padding(32.dp)) {
        Box(modifier = Modifier.size(120.dp).background(brush = MainGradient, shape = CircleShape, alpha = 0.1f), contentAlignment = Alignment.Center) {
            Icon(Icons.Rounded.AutoAwesome, null, modifier = Modifier.size(70.dp), tint = BluePrimary)
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(text = "Anicca English Tutor", style = MaterialTheme.typography.headlineSmall.copy(brush = MainGradient, fontWeight = FontWeight.ExtraBold, fontSize = 24.sp, textAlign = TextAlign.Center), modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = "Your intelligent assistant for everything.\nAsk me anything, from learning languages to solving complex problems!", style = MaterialTheme.typography.bodyLarge, color = Color.Gray, textAlign = TextAlign.Center, lineHeight = 22.sp)
    }
}

@Composable
fun EnhancedMessageBubble(message: ChatMessage) {
    val isUser = message.isUser
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = if (isUser) Alignment.End else Alignment.Start) {
        Surface(shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp, bottomStart = if (isUser) 20.dp else 4.dp, bottomEnd = if (isUser) 4.dp else 20.dp), color = Color.Transparent, shadowElevation = if (isUser) 4.dp else 1.dp) {
            Box(modifier = Modifier.background(brush = if (isUser) MainGradient else Brush.linearGradient(listOf(Color.White, Color.White)))) {
                Text(text = message.text, modifier = Modifier.padding(12.dp), color = if (isUser) Color.White else Color(0xFF333333), style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
fun ModernTypingIndicator() {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(start = 4.dp)) {
        CircularProgressIndicator(modifier = Modifier.size(12.dp), strokeWidth = 2.dp, color = BluePrimary)
        Spacer(modifier = Modifier.width(8.dp))
        Text("Typing...", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
    }
}

@Composable
fun HistoryDrawerContent(sessions: List<ChatSession>, onNewChatClick: () -> Unit, onSessionClick: (String) -> Unit) {
    ModalDrawerSheet(drawerContainerColor = AppBackground) {
        Column(modifier = Modifier.padding(20.dp).fillMaxHeight()) {
            Text("Chat History", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black, color = Color(0xFF263238))
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onNewChatClick, modifier = Modifier.fillMaxWidth().background(brush = MainGradient, shape = RoundedCornerShape(12.dp)), colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent), contentPadding = PaddingValues(0.dp)) {
                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Add, null, tint = Color.White)
                    Text("New Conversation", modifier = Modifier.padding(start = 8.dp), color = Color.White)
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            LazyColumn {
                items(sessions) { session ->
                    NavigationDrawerItem(label = { Text(session.title, maxLines = 1) }, selected = false, onClick = { onSessionClick(session.id) }, icon = { Icon(Icons.Default.History, null, tint = Color.Gray) }, shape = RoundedCornerShape(12.dp), colors = NavigationDrawerItemDefaults.colors(unselectedContainerColor = Color.White), modifier = Modifier.padding(vertical = 4.dp))
                }
            }
        }
    }
}