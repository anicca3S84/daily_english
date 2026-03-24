package com.codework.daily_english.presentation.screen.translate

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.CompareArrows
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.History
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codework.daily_english.presentation.components.AppTopBar
import com.codework.daily_english.presentation.components.StateWrapper
import com.codework.daily_english.presentation.screen.translate.model.TranslationUiModel
import com.codework.daily_english.presentation.screen.translate.mvi.TranslateEvent
import com.codework.daily_english.presentation.screen.translate.mvi.TranslateState
import com.codework.daily_english.presentation.theme.AppBackground
import com.codework.daily_english.presentation.theme.BluePrimary
import com.codework.daily_english.presentation.theme.MainGradient

@Composable
fun TranslateScreen(
    state: TranslateState,
    onEvent: (TranslateEvent) -> Unit,
    onBackClick: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val scrollState = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        containerColor = AppBackground,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            AppTopBar(
                onBackClick = onBackClick,
                titleContent = {
                    Text(
                        text = "Translate",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF111827)
                    )
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(AppBackground)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // --- PHẦN 1: TĨNH (Luôn hiển thị) ---
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Spacer(modifier = Modifier.height(16.dp))

                LanguageSwitchBar(
                    sourceLang = state.sourceLang,
                    targetLang = state.targetLang,
                    onSwap = { onEvent(TranslateEvent.OnSwapLanguage) }
                )

                Spacer(modifier = Modifier.height(20.dp))

                InputCard(
                    text = state.sourceText,
                    hint = "Type to translate...",
                    onValueChange = { onEvent(TranslateEvent.OnTextChange(it)) },
                    onClear = { onEvent(TranslateEvent.OnClearText) },
                    onTranslate = {
                        keyboardController?.hide()
                        onEvent(TranslateEvent.OnTranslateClick)
                    },
                    onImageClick = { onEvent(TranslateEvent.OnImageClick) },
                    onCameraClick = { onEvent(TranslateEvent.OnCameraClick) },
                    onMicClick = { onEvent(TranslateEvent.OnMicClick) }
                )

                Spacer(modifier = Modifier.height(16.dp))

                AnimatedVisibility(
                    visible = state.targetText.isNotEmpty() || state.isTranslating,
                    enter = fadeIn() + slideInVertically { it / 2 }
                ) {
                    Column {
                        ResultCard(
                            text = state.targetText,
                            isTranslating = state.isTranslating
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .heightIn(min = 300.dp) // Đảm bảo đủ chỗ để hiện Shimmer/Error Overlay
            ) {
                StateWrapper(
                    uiState = state.historyState,
                    snackbarHostState = snackbarHostState,
                    onRetry = { /* onEvent(TranslateEvent.OnRetryHistory) */ }
                ) { historyList ->
                    HistorySection(
                        historyItems = historyList ?: emptyList(),
                        onHistoryItemClick = { /* Handle click */ }
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

// ==========================
// SHARED COMPONENTS
// ==========================

@Composable
fun StandardCircleButton(
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = 40.dp,
    iconColor: Color = Color(0xFF6B7280)
) {
    Surface(
        onClick = onClick,
        shape = CircleShape,
        color = Color.White,
        border = BorderStroke(1.dp, Color(0xFFE5E7EB)),
        modifier = modifier.size(size)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

// ------------------------------------------------------------------------
// CẬP NHẬT: LANGUAGE SWITCH BAR & CHIP (Fix lỗi căn giữa)
// ------------------------------------------------------------------------

@Composable
fun LanguageSwitchBar(
    sourceLang: String,
    targetLang: String,
    onSwap: () -> Unit,
    onSourceClick: () -> Unit = {},
    onTargetClick: () -> Unit = {}
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = 2.dp, // Đổ bóng nhẹ cho cả thanh
        modifier = Modifier.height(60.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 1. Language Source
            LanguageChip(
                text = sourceLang,
                modifier = Modifier.weight(1f),
                isTarget = false,
                onClick = onSourceClick
            )

            // 2. Swap Button
            Box(
                modifier = Modifier.width(48.dp),
                contentAlignment = Alignment.Center
            ) {
                IconButton(onClick = onSwap) {
                    Icon(
                        Icons.AutoMirrored.Filled.CompareArrows,
                        null,
                        tint = BluePrimary,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            // 3. Language Target
            LanguageChip(
                text = targetLang,
                modifier = Modifier.weight(1f),
                isTarget = true,
                onClick = onTargetClick
            )
        }
    }
}

@Composable
fun LanguageChip(
    text: String,
    modifier: Modifier = Modifier,
    isTarget: Boolean = false,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .padding(vertical = 8.dp)
            .clip(RoundedCornerShape(12.dp))
            // ĐÃ BỎ BORDER (KHUNG NGOÀI)
            // Giữ lại nền nhẹ cho Target để phân biệt (hoặc để Transparent hết nếu muốn phẳng hoàn toàn)
            .background(Color.Transparent)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center // Căn giữa tuyệt đối
    ) {
        // Nội dung co cụm lại ở giữa
        Row(
            modifier = Modifier.wrapContentSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = text,
                fontWeight = FontWeight.SemiBold,
                style = if (isTarget) TextStyle(brush = MainGradient, fontSize = 16.sp)
                else TextStyle(color = Color.Gray, fontSize = 16.sp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Spacer(modifier = Modifier.width(4.dp))

            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null,
                tint = if (isTarget) BluePrimary else Color.Gray,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}


@Composable
fun InputCard(
    text: String,
    hint: String,
    onValueChange: (String) -> Unit,
    onClear: () -> Unit,
    onTranslate: () -> Unit,
    onImageClick: () -> Unit, // THÊM
    onCameraClick: () -> Unit, // THÊM
    onMicClick: () -> Unit // THÊM
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(20.dp)
            ) {
                if (text.isEmpty()) {
                    Text(hint, color = Color.LightGray, fontSize = 20.sp)
                }

                BasicTextField(
                    value = text,
                    onValueChange = onValueChange,
                    textStyle = TextStyle(fontSize = 20.sp, color = Color.Black),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(end = 50.dp)
                )

                if (text.isNotEmpty()) {
                    Column(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .width(40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        StandardCircleButton(icon = Icons.Default.Close, onClick = onClear)
                        StandardCircleButton(icon = Icons.AutoMirrored.Filled.VolumeUp, onClick = { /* TTS Input */ })
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StandardCircleButton(icon = Icons.Default.Image, onClick = onImageClick) // SỬA
                    StandardCircleButton(icon = Icons.Default.CameraAlt, onClick = onCameraClick) // SỬA
                    StandardCircleButton(icon = Icons.Default.Mic, onClick = onMicClick) // SỬA
                }

                Box(
                    modifier = Modifier
                        .height(44.dp)
                        .background(BluePrimary, shape = RoundedCornerShape(8.dp))
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onTranslate() }
                        .padding(horizontal = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Translate", fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}

@Composable
fun ResultCard(text: String, isTranslating: Boolean) {
    val clipboardManager = LocalClipboardManager.current

    Card(
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFE5E7EB)),
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            // Header: Text & Copy
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    if (isTranslating) {
                        Column {
                            CircularProgressIndicator(
                                modifier = Modifier.size(30.dp),
                                color = BluePrimary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Translating...", color = Color.Gray)
                        }
                    } else {
                        Text(
                            text = text,
                            style = TextStyle(
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF1F2937),
                                lineHeight = 32.sp
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                StandardCircleButton(
                    icon = Icons.Rounded.ContentCopy,
                    onClick = { clipboardManager.setText(AnnotatedString(text)) },
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Footer: Speaker
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StandardCircleButton(
                    icon = Icons.AutoMirrored.Filled.VolumeUp,
                    onClick = { /* Speak Result */ },
                )
            }
        }
    }
}

@Composable
fun HistorySection(
    historyItems: List<TranslationUiModel>, // Dùng model thật thay vì dummy
    onHistoryItemClick: (TranslationUiModel) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Rounded.History,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Recent History",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF374151)
            )
        }

        if (historyItems.isEmpty()) {
            Text(
                text = "No history yet",
                modifier = Modifier.padding(vertical = 20.dp).align(Alignment.CenterHorizontally),
                color = Color.Gray
            )
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                historyItems.forEach { item ->
                    HistoryItem(
                        source = item.sourceText,
                        target = item.targetText,
                        onClick = { onHistoryItemClick(item) }
                    )
                }
            }
        }
    }
}

@Composable
fun HistoryItem(source: String, target: String, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        border = BorderStroke(1.dp, Color(0xFFF3F4F6)),
        shadowElevation = 1.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = source,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF1F2937),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = target,
                    style = MaterialTheme.typography.bodySmall,
                    color = BluePrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = Color(0xFF9CA3AF),
                modifier = Modifier.size(16.dp)
            )
        }
    }
}