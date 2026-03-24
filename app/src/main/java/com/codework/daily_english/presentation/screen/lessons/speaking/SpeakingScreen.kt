package com.codework.daily_english.presentation.screen.lessons.speaking

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Keyboard
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codework.daily_english.presentation.components.StateWrapper
import com.codework.daily_english.presentation.components.*
import com.codework.daily_english.presentation.screen.lessons.speaking.model.ChatMessage
import com.codework.daily_english.presentation.screen.lessons.speaking.model.RecordState
import com.codework.daily_english.presentation.screen.lessons.speaking.mvi.SpeakingEvent
import com.codework.daily_english.presentation.screen.lessons.speaking.mvi.SpeakingState
import com.codework.daily_english.presentation.theme.*

@Composable
fun SpeakingScreen(
    state: SpeakingState, // SỬA: SpeakingData -> SpeakingState
    onEvent: (SpeakingEvent) -> Unit,
    onBackClick: () -> Unit
) {
    val listState = rememberLazyListState()

    Scaffold(
        containerColor = Color.White,
        topBar = {
//            if (state is BaseState.Success) {
//                if (state.data.recordState == RecordState.READY_TO_START) {
//                    AppTopBar(onBackClick = onBackClick, titleContent = { Text("Roleplay Challenge", fontSize = 18.sp, fontWeight = FontWeight.Bold) })
//                } else {
//                    ProgressTopBar(currentStep = state.data.progressCurrent, totalSteps = state.data.progressTotal, onBackClick = onBackClick, onCloseClick = null)
//                }
//            } else {
//                AppTopBar(onBackClick = onBackClick, titleContent = {})
//            }
        },
        bottomBar = {
//            if (state is BaseState.Success) {
//                val data = state.data
//                if (data.recordState == RecordState.COMPLETED) {
//                    Box(modifier = Modifier.fillMaxWidth().background(Color.White).padding(24.dp)) {
//                        PrimaryButton("Continue", onClick = { onEvent(SpeakingEvent.OnFinishLesson) }, modifier = Modifier.fillMaxWidth())
//                    }
//                } else if (data.recordState != RecordState.READY_TO_START) {
//                    Surface(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .shadow(
//                                elevation = 25.dp,
//                                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
//                                spotColor = Color.Black.copy(alpha = 0.5f)
//                            ),
//                        color = Color.White,
//                        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
//                    ) {
//                        Column(modifier = Modifier.fillMaxWidth()) {
//                            // --- VÙNG HINT VỚI MÀU NỀN TÁCH BIỆT ---
//                            AnimatedVisibility(
//                                visible = data.isHintSheetOpen,
//                                enter = expandVertically() + fadeIn(),
//                                exit = shrinkVertically() + fadeOut()
//                            ) {
//                                Column(
//                                    modifier = Modifier
//                                        .fillMaxWidth()
//                                        .background(Color.White) // Màu xám xanh nhẹ để phân biệt với AppBackground trắng
//                                        .padding(top = 28.dp, bottom = 12.dp, start = 24.dp, end = 24.dp)
//                                ) {
//                                    // Header
//                                    Row(
//                                        modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp),
//                                        horizontalArrangement = Arrangement.SpaceBetween,
//                                        verticalAlignment = Alignment.CenterVertically
//                                    ) {
//                                        Row(verticalAlignment = Alignment.CenterVertically) {
//                                            Icon(Icons.Filled.Lightbulb, null, tint = Color(0xFFF59E0B), modifier = Modifier.size(22.dp))
//                                            Spacer(modifier = Modifier.width(10.dp))
//                                            Text("Suggested Replies", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
//                                        }
//                                        IconButton(
//                                            onClick = { onEvent(SpeakingEvent.OnToggleHintSheet) },
//                                            modifier = Modifier.background(Color(0xFFE2E8F0), CircleShape).size(28.dp)
//                                        ) {
//                                            Icon(Icons.Default.Close, null, tint = GrayText, modifier = Modifier.size(16.dp))
//                                        }
//                                    }
//
//                                    // Danh sách gợi ý (Card trắng trên nền xám nhẹ)
//                                    data.suggestedHints.forEach { hintText ->
//                                        Surface(
//                                            onClick = { onEvent(SpeakingEvent.OnUseHint(hintText)) },
//                                            shape = RoundedCornerShape(16.dp),
//                                            color = Color.White, // Card trắng sẽ nổi bật trên nền F8FAFC
//                                            border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
//                                            shadowElevation = 1.dp,
//                                            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
//                                        ) {
//                                            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
//                                                Text(hintText, fontSize = 15.sp, color = BluePrimary, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
//                                                Icon(Icons.AutoMirrored.Filled.Send, null, tint = BluePrimary, modifier = Modifier.size(18.dp))
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//
//                            // Đường line mờ để ngăn cách nếu Hint đang mở
//                            if (data.isHintSheetOpen) {
//                                HorizontalDivider(thickness = 1.dp, color = Color(0xFFF1F5F9))
//                            }
//
//                            // Phần Microphone giữ nguyên màu trắng
//                            MicrophoneSection(
//                                recordState = data.recordState,
//                                isHintOpen = data.isHintSheetOpen,
//                                onEvent = onEvent
//                            )
//                        }
//                    }
//                }
//            }
        }
    ) { paddingValues ->
        {
//            LaunchedEffect(data.messages.size) {
//                if (data.messages.isNotEmpty()) {
//                    listState.animateScrollToItem(data.messages.size - 1)
//                }
//            }
        }
            Column(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    color = Color(0xFFF1F5F9), shape = RoundedCornerShape(50),
                    modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Icon(
                            Icons.Default.Flag,
                            null,
                            tint = Color(0xFFF59E0B),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
//                        Text(
//                            text = data.missionTarget,
//                            fontSize = 14.sp,
//                            fontWeight = FontWeight.SemiBold
//                        )
                    }
                }

//                if (data.recordState == RecordState.READY_TO_START && data.messages.isEmpty()) {
//                    Spacer(modifier = Modifier.weight(1f))
//                    VoiceOrbVisualizer(state = RecordState.READY_TO_START)
//                    Spacer(modifier = Modifier.height(32.dp))
//                    Text("Ready to speak?", fontSize = 24.sp, fontWeight = FontWeight.Bold)
//                    Text(
//                        "Tap Start Roleplay to begin.",
//                        fontSize = 16.sp,
//                        color = GrayText,
//                        modifier = Modifier.padding(top = 8.dp)
//                    )
//                    Spacer(modifier = Modifier.weight(1f))
//                    Box(modifier = Modifier.fillMaxWidth().padding(24.dp)) {
//                        PrimaryButton(
//                            "Start Roleplay",
//                            onClick = { onEvent(SpeakingEvent.OnStartRoleplay) },
//                            modifier = Modifier.fillMaxWidth()
//                        )
//                    }
//                } else {
//                    Spacer(modifier = Modifier.height(24.dp))
//                    VoiceOrbVisualizer(state = data.recordState)
//                    Spacer(modifier = Modifier.height(32.dp))
//
//                    LazyColumn(
//                        state = listState,
//                        modifier = Modifier.weight(1f).fillMaxWidth().padding(horizontal = 24.dp),
//                        contentPadding = PaddingValues(bottom = 16.dp),
//                        verticalArrangement = Arrangement.spacedBy(24.dp)
//                    ) {
//                        itemsIndexed(data.messages) { index, message ->
//                            LiveSubtitleItem(
//                                message = message,
//                                isCurrent = index == data.messages.lastIndex,
//                                onEvent = onEvent
//                            )
//                        }
//                    }
//                }
            }
        }
}

// --- FIX 1: HINT CÓ BÓNG ĐỔ VÀ NỔI BẬT KHỎI NỀN TRẮNG ---
@Composable
fun CustomHintSection(hints: List<String>, onEvent: (SpeakingEvent) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 20.dp,
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                spotColor = Color.Black.copy(alpha = 0.5f)
            )
            .background(Color.White, RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
            // TĂNG PADDING: Top 32.dp và Bottom 40.dp để tạo khoảng thoáng
            .padding(top = 32.dp, bottom = 40.dp, start = 24.dp, end = 24.dp)
    ) {
        // --- HEADER CỦA HINT ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                // Tăng khoảng cách dưới tiêu đề lên 24.dp
                .padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Hiệu ứng đổ bóng nhẹ cho icon bóng đèn
                Surface(
                    shape = CircleShape,
                    color = Color(0xFFFFF7ED),
                    modifier = Modifier.size(36.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Filled.Lightbulb,
                            contentDescription = null,
                            tint = Color(0xFFF59E0B),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Suggested Replies",
                    fontSize = 18.sp, // Tăng size tiêu đề
                    fontWeight = FontWeight.ExtraBold,
                    color = TextPrimary,
                    letterSpacing = 0.5.sp
                )
            }

            // Nút tắt được bọc trong vòng tròn mờ để dễ bấm hơn
            Surface(
                onClick = { onEvent(SpeakingEvent.OnToggleHintSheet) },
                shape = CircleShape,
                color = Color(0xFFF1F5F9),
                modifier = Modifier.size(32.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Close",
                        tint = GrayText,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        // --- DANH SÁCH GỢI Ý ---
        if (hints.isEmpty()) {
            Text(
                "No hints available right now.",
                color = GrayText,
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        } else {
            hints.forEach { hintText ->
                Surface(
                    onClick = { onEvent(SpeakingEvent.OnUseHint(hintText)) },
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White,
                    border = BorderStroke(1.dp, BluePrimary.copy(alpha = 0.1f)),
                    shadowElevation = 4.dp, // Tăng shadow cho từng card gợi ý
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 18.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = hintText,
                            fontSize = 16.sp,
                            color = BluePrimary,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f)
                        )
                        Icon(
                            Icons.AutoMirrored.Filled.Send,
                            null,
                            tint = BluePrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

// --- FIX 2: VÙNG CLICK RỘNG HƠN & HIỂN THỊ LẠI NÚT DỊCH ---
@Composable
fun LiveSubtitleItem(message: ChatMessage, isCurrent: Boolean, onEvent: (SpeakingEvent) -> Unit) {
    val isUser = message.isFromUser
    val displayText = if (message.isTranslated && message.translatedText != null) message.translatedText else message.text
    val alpha = if (isCurrent) 1f else 0.4f
    val align = if (isUser) TextAlign.End else TextAlign.Start

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(alpha),
        horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                // CHỈ BẤM ĐƯỢC VÀO CHỮ CỦA AI VÀ KHI CÓ BẢN DỊCH
                .clickable(enabled = !isUser && message.translatedText != null) {
                    onEvent(SpeakingEvent.OnToggleTranslate(message.id))
                }
                .padding(horizontal = 4.dp, vertical = 2.dp) // Padding nhẹ để click dễ trúng hơn
        ) {
            AnimatedContent(targetState = displayText, label = "text_trans") { text ->
                Text(
                    text = text,
                    color = if (isUser) BluePrimary else TextPrimary,
                    fontSize = if (isCurrent) 22.sp else 18.sp,
                    fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Medium,
                    textAlign = align,
                    lineHeight = 32.sp
                )
            }
        }

        // CÁC NÚT ACTION CHO CÂU HIỆN TẠI (LOA & DỊCH)
        if (!isUser && isCurrent && message.translatedText != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { onEvent(SpeakingEvent.OnPlayAudio(message.id)) }, modifier = Modifier.size(24.dp)) {
                    Icon(Icons.AutoMirrored.Filled.VolumeUp, null, tint = Color(0xFF94A3B8), modifier = Modifier.size(20.dp))
                }
                Spacer(modifier = Modifier.width(16.dp))
                IconButton(onClick = { onEvent(SpeakingEvent.OnToggleTranslate(message.id)) }, modifier = Modifier.size(24.dp)) {
                    val tint = if (message.isTranslated) BluePrimary else Color(0xFF94A3B8)
                    Icon(Icons.Default.Translate, null, tint = tint, modifier = Modifier.size(20.dp))
                }
            }
        }
    }
}

@Composable
fun VoiceOrbVisualizer(state: RecordState) {
    val infiniteTransition = rememberInfiniteTransition(label = "orb_pulse")
    val orbColor = when (state) {
        RecordState.RECORDING -> USRed
        RecordState.AI_SPEAKING -> BluePrimary
        RecordState.PROCESSING -> Color(0xFF94A3B8)
        RecordState.COMPLETED -> SuccessGreen
        else -> BluePrimary.copy(alpha = 0.5f)
    }

    val isAnimating = state == RecordState.RECORDING || state == RecordState.AI_SPEAKING
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.8f, targetValue = if (isAnimating) 1.5f else 1f,
        animationSpec = infiniteRepeatable(animation = tween(800, easing = FastOutSlowInEasing), repeatMode = RepeatMode.Reverse), label = "scale"
    )

    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(120.dp)) {
        if (isAnimating) {
            Box(modifier = Modifier.size(80.dp).scale(pulseScale).background(orbColor.copy(alpha = 0.2f), CircleShape))
            Box(modifier = Modifier.size(80.dp).scale(pulseScale * 0.8f).background(orbColor.copy(alpha = 0.3f), CircleShape))
        }
        Box(modifier = Modifier.size(80.dp).background(orbColor, CircleShape).shadow(8.dp, CircleShape)) {
            val icon = when (state) {
                RecordState.COMPLETED -> Icons.Default.Check
                RecordState.PAUSED -> Icons.Default.MicOff
                RecordState.RECORDING -> Icons.Default.Mic
                else -> Icons.Default.GraphicEq
            }
            Icon(imageVector = icon, contentDescription = null, tint = Color.White, modifier = Modifier.align(Alignment.Center).size(36.dp))
        }
    }
}

@Composable
fun MicrophoneSection(recordState: RecordState, isHintOpen: Boolean, onEvent: (SpeakingEvent) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(top = 16.dp, bottom = 32.dp, start = 32.dp, end = 32.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val hintBgColor = if (isHintOpen) Color(0xFFF59E0B) else Color(0xFFFFF7ED)
        val hintIconColor = if (isHintOpen) Color.White else Color(0xFFF97316)

        IconButton(
            onClick = { onEvent(SpeakingEvent.OnToggleHintSheet) },
            modifier = Modifier.background(hintBgColor, CircleShape).size(48.dp)
        ) {
            Icon(Icons.Outlined.Lightbulb, contentDescription = "Hint", tint = hintIconColor)
        }

        Surface(
            onClick = { onEvent(SpeakingEvent.OnToggleRecord) },
            shape = CircleShape,
            color = if (recordState == RecordState.RECORDING) USRed else Color(0xFFE2E8F0),
            modifier = Modifier.size(64.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                when (recordState) {
                    RecordState.PROCESSING -> CircularProgressIndicator(color = BluePrimary, modifier = Modifier.size(24.dp), strokeWidth = 3.dp)
                    RecordState.RECORDING -> Box(modifier = Modifier.size(20.dp).background(Color.White, RoundedCornerShape(4.dp)))
                    RecordState.PAUSED -> Icon(Icons.Default.PlayArrow, null, tint = BluePrimary, modifier = Modifier.size(32.dp))
                    else -> Icon(Icons.Default.Mic, null, tint = Color(0xFF94A3B8), modifier = Modifier.size(28.dp))
                }
            }
        }

        IconButton(onClick = { onEvent(SpeakingEvent.OnSwitchToTyping) }, modifier = Modifier.background(Color(0xFFF1F5F9), CircleShape).size(48.dp)) {
            Icon(Icons.Outlined.Keyboard, null, tint = Color(0xFF64748B))
        }
    }
}

@Preview(showBackground = true, name = "With Custom Hint in BottomBar")
@Composable
fun PreviewLiveCallWithHint() {
    val mockMessages = listOf(
        ChatMessage(text = "Hello! Welcome to Star Coffee. What can I get for you today?", isFromUser = false, translatedText = "Xin chào! Bạn muốn gọi gì?")
    )
    val mockData = SpeakingState(
        missionTarget = "Order a drink and pay",
        messages = mockMessages,
        recordState = RecordState.RECORDING,
        isHintSheetOpen = true,
        suggestedHints = listOf("I'd like a medium cappuccino.", "Can I get a hot coffee, please?")
    )
//    SpeakingScreen(state = BaseState.Success(mockData), onEvent = {}, onBackClick = {})
}