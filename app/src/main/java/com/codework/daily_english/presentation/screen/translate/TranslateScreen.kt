package com.codework.daily_english.presentation.screen.translate

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.CompareArrows
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.codework.daily_english.presentation.components.BaseScreen
import com.codework.daily_english.presentation.components.UiState

@Composable
fun TranslateScreen(
    state: UiState<TranslateData>,
    onEvent: (TranslateEvent) -> Unit,
    onBackClick: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        containerColor = Color(0xFFF8F9FD),
        topBar = {
            TranslateTopBar(onBackClick)
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            BaseScreen(state = state) { data ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    // 1. THANH CHUYỂN ĐỔI NGÔN NGỮ
                    LanguageSwitchBar(
                        sourceLang = data.sourceLang,
                        targetLang = data.targetLang,
                        onSwap = { onEvent(TranslateEvent.OnSwapLanguage) }
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // 2. KHU VỰC NHẬP LIỆU (SOURCE)
                    InputCard(
                        text = data.sourceText,
                        hint = "Enter text to translate...",
                        onValueChange = { onEvent(TranslateEvent.OnTextChange(it)) },
                        onClear = { onEvent(TranslateEvent.OnClearText) },
                        onTranslate = {
                            keyboardController?.hide()
                            onEvent(TranslateEvent.OnTranslateClick)
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // 3. KHU VỰC KẾT QUẢ (TARGET) - Có Animation
                    AnimatedVisibility(
                        visible = data.targetText.isNotEmpty() || data.isTranslating,
                        enter = fadeIn() + slideInVertically { it / 2 }
                    ) {
                        ResultCard(
                            text = data.targetText,
                            isLoading = data.isTranslating,
                            targetLang = data.targetLang
                        )
                    }
                }
            }
        }
    }
}

// ==========================
// COMPONENTS
// ==========================


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TranslateTopBar(onBackClick: () -> Unit) {
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
fun LanguageSwitchBar(sourceLang: String, targetLang: String, onSwap: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(50),
        color = Color.White,
        shadowElevation = 4.dp,
        modifier = Modifier.height(56.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Nút nguồn
            LanguageChip(text = sourceLang, modifier = Modifier.weight(1f))

            // Nút đảo chiều
            IconButton(onClick = onSwap) {
                Icon(
                    Icons.AutoMirrored.Filled.CompareArrows,
                    null,
                    tint = Color(0xFF2563EB),
                    modifier = Modifier.size(24.dp)
                )
            }

            // Nút đích
            LanguageChip(text = targetLang, modifier = Modifier.weight(1f), isTarget = true)
        }
    }
}

@Composable
fun LanguageChip(text: String, modifier: Modifier = Modifier, isTarget: Boolean = false) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(50))
            .clickable { /* Mở dialog chọn ngôn ngữ */ },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.SemiBold,
            color = if (isTarget) Color(0xFF2563EB) else Color.Gray
        )
    }
}

@Composable
fun InputCard(
    text: String,
    hint: String,
    onValueChange: (String) -> Unit,
    onClear: () -> Unit,
    onTranslate: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth().height(200.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.weight(1f).padding(20.dp)) {
                if (text.isEmpty()) {
                    Text(hint, color = Color.LightGray, fontSize = 20.sp)
                }
                BasicTextField(
                    value = text,
                    onValueChange = onValueChange,
                    textStyle = TextStyle(fontSize = 20.sp, color = Color.Black),
                    modifier = Modifier.fillMaxSize()
                )

                if (text.isNotEmpty()) {
                    IconButton(
                        onClick = onClear,
                        modifier = Modifier.align(Alignment.TopEnd).offset(x = 12.dp, y = (-12).dp)
                    ) {
                        Icon(Icons.Default.Close, null, tint = Color.LightGray)
                    }
                }
            }

            // Thanh công cụ bên dưới thẻ Input
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Các nút tính năng phụ (Mic, Camera)
                Row {
                    IconButton(onClick = { /* Voice Input */ }) {
                        Icon(Icons.Default.Mic, null, tint = Color.Gray)
                    }
                    IconButton(onClick = { /* Camera Input */ }) {
                        Icon(Icons.Default.CameraAlt, null, tint = Color.Gray)
                    }
                }

                // Nút Dịch (Nổi bật)
                Button(
                    onClick = onTranslate,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2563EB) // Blue Premium
                    ),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 24.dp)
                ) {
                    Text("Translate", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun ResultCard(text: String, isLoading: Boolean, targetLang: String) {
    val clipboardManager = LocalClipboardManager.current

    Card(
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF2563EB), Color(0xFF1E40AF)) // Gradient Xanh đậm
                    )
                )
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                // Header của Result
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        targetLang,
                        color = Color.White.copy(0.7f),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Icon(Icons.Rounded.StarBorder, null, tint = Color.White.copy(0.7f))
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (isLoading) {
                    // Hiệu ứng Loading
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(30.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Translating...", color = Color.White.copy(0.8f))
                } else {
                    // Kết quả dịch
                    Text(
                        text = text,
                        style = TextStyle(
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White,
                            lineHeight = 32.sp
                        )
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Action Row (Copy, Speak)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { /* Text To Speech */ }) {
                        Icon(Icons.AutoMirrored.Filled.VolumeUp, null, tint = Color.White)
                    }
                    IconButton(
                        onClick = { clipboardManager.setText(AnnotatedString(text)) }
                    ) {
                        Icon(Icons.Rounded.ContentCopy, null, tint = Color.White)
                    }
                }
            }
        }
    }
}