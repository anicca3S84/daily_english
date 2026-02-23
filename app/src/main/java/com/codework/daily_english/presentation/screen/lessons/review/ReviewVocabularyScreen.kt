package com.codework.daily_english.presentation.screen.lessons.review

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codework.daily_english.presentation.components.BaseScreen
import com.codework.daily_english.presentation.components.ProgressTopBar
import com.codework.daily_english.presentation.components.QuizOption
import com.codework.daily_english.presentation.components.QuizVocabularyCompleteDialog
import com.codework.daily_english.presentation.components.UiState
import com.codework.daily_english.presentation.theme.AppBackground
import com.codework.daily_english.presentation.theme.BluePrimary
import com.codework.daily_english.presentation.theme.DisableBtn
import com.codework.daily_english.presentation.theme.HintBackGround
import com.codework.daily_english.presentation.theme.MainGradient
import com.codework.daily_english.presentation.theme.TextPrimary

@Composable
fun ReviewVocabularyScreen(
    state: UiState<ReviewVocabularyData>,
    onEvent: (ReviewVocabularyEvent) -> Unit,
    onBackClick: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    var showWarningTrigger by remember { mutableIntStateOf(0) }
    var showCompletionDialog by remember { mutableStateOf(false) }

    // Xử lý nút Back vật lý
    BackHandler { onEvent(ReviewVocabularyEvent.OnGoBack) }

    val progressCurrent = (state as? UiState.Success)?.data?.progressCurrent ?: 0
    val progressTotal = (state as? UiState.Success)?.data?.progressTotal ?: 10

    Scaffold(
        containerColor = AppBackground,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            ProgressTopBar(
                currentStep = progressCurrent,
                totalSteps = progressTotal,
                onBackClick = { onEvent(ReviewVocabularyEvent.OnGoBack) }
            )
        },
        bottomBar = {
            if (state is UiState.Success) {
                val data = state.data
                var textBtn = ""
                if (data.progressCurrent >= data.progressTotal) {
                    textBtn = "Finish"
                } else {
                    textBtn = "Next"
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, bottom = 32.dp, top = 16.dp)
                        .height(50.dp)
                        .clip(RoundedCornerShape(28.dp))
                        .background(
                            brush = if (data.selectedOptionIndex != null) MainGradient else SolidColor(DisableBtn)
                        )
                        .clickable {
                            if (data.selectedOptionIndex == null) {
                                showWarningTrigger++
                            } else {
                                if (data.progressCurrent < data.progressTotal) {
                                    onEvent(ReviewVocabularyEvent.OnNextQuestion)
                                } else {
                                    showCompletionDialog = true
                                }
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = textBtn,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    ) { paddingValues ->
        BaseScreen(state = state) { data ->

            LaunchedEffect(showWarningTrigger) {
                if (showWarningTrigger > 0) {
                    snackbarHostState.currentSnackbarData?.dismiss()
                    snackbarHostState.showSnackbar(
                        message = "Vui lòng chọn 1 đáp án để tiếp tục nhé! \uD83D\uDCAA",
                        duration = SnackbarDuration.Short,
                        withDismissAction = true
                    )
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 20.dp), // Trả lại padding 2 bên màn hình
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
//                item {
//                    Spacer(modifier = Modifier.height(16.dp))
//
//                    // --- BADGE "REVIEW PHASE" ---
//                    Row(
//                        verticalAlignment = Alignment.CenterVertically,
//                        horizontalArrangement = Arrangement.Center,
//                        modifier = Modifier
//                            .clip(RoundedCornerShape(50))
//                            .background(Color.Transparent)
//                            .padding(horizontal = 16.dp, vertical = 10.dp)
//                    ) {
//                        Text(
//                            text = "REVIEW PHASE",
//                            style = TextStyle(
//                                brush = MainGradient,
//                            ),
//                            fontWeight = FontWeight.ExtraBold,
//                            fontSize = 20.sp,
//                            letterSpacing = 1.5.sp
//                        )
//                    }
//
//                    Spacer(modifier = Modifier.height(20.dp))
//                }

                // ==========================================
                // KHỐI BACKGROUND LỚN CHỨA TOÀN BỘ CÂU HỎI VÀ ĐÁP ÁN
                // ==========================================
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp)) // Bo đều 4 góc và bo ít đi (16.dp)
                            .background(HintBackGround)
                            .padding(horizontal = 20.dp, vertical = 24.dp), // Padding trong vừa phải
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // 1. TIÊU ĐỀ
                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Fill in the blank",
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            letterSpacing = 0.5.sp
                        )
                        Spacer(modifier = Modifier.height(24.dp))

                        // 2. THẺ CÂU HỎI
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp), // Giảm độ bo góc ô điền từ (12.dp)
                            color = Color.White,
                            shadowElevation = 4.dp,
                            border = BorderStroke(1.5.dp, MainGradient)
                        ) {
                            Box(
                                modifier = Modifier.padding(20.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = formatBlankSentence(
                                        sentence = data.sentence,
                                        isRevealed = data.selectedOptionIndex != null,
                                        correctWord = data.options[data.correctAnswerIndex]
                                    ),
                                    style = TextStyle(
                                        color = TextPrimary,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Normal,
                                        textAlign = TextAlign.Center,
                                        lineHeight = 32.sp
                                    )
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(28.dp))

                        // 3. DỊCH NGHĨA (HIỆN KHI CHỌN)
                        AnimatedVisibility(visible = data.selectedOptionIndex != null) {
                            Text(
                                text = data.translatedSentence,
                                color = Color(0xFF64748B),
                                fontSize = 16.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 8.dp).padding(bottom = 24.dp)
                            )
                        }

                        // 4. GRID ĐÁP ÁN
                        val rows = data.options.chunked(2)
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            rows.forEach { rowItems ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    rowItems.forEachIndexed { columnIndex, optionText ->
                                        val currentIndexInOptions = data.options.indexOf(optionText)

                                        Box(modifier = Modifier.weight(1f)) {
                                            QuizOption(
                                                text = optionText,
                                                index = currentIndexInOptions,
                                                selectedIndex = data.selectedOptionIndex,
                                                correctIndex = data.correctAnswerIndex,
                                                onClick = { index ->
                                                    onEvent(ReviewVocabularyEvent.OnSelectOption(index))
                                                }
                                            )
                                        }
                                    }
                                    if (rowItems.size < 2) Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(20.dp))

                    }

                    // Thêm một Spacer nhỏ ở ngoài thẻ xanh để cách Bottom Bar một chút nếu cần thiết
                }
            }
        }

        if (showCompletionDialog) {
            QuizVocabularyCompleteDialog(
                onContinue = {
                    showCompletionDialog = false
                    onEvent(ReviewVocabularyEvent.OnNavigateToGrammarScreen)
                },
                onRetryQuiz = { showCompletionDialog = false },
                onRelearnWords = { showCompletionDialog = false },
                onDismiss = { showCompletionDialog = false },
                score = 7,
                total = 10
            )
        }
    }
}

@Composable
fun formatBlankSentence(
    sentence: String,
    isRevealed: Boolean,
    correctWord: String
): androidx.compose.ui.text.AnnotatedString {
    return buildAnnotatedString {
        if (!isRevealed) {
            append(sentence)
        } else {
            val parts = sentence.split("________")
            if (parts.size >= 2) {
                append(parts[0])

                withStyle(
                    SpanStyle(
                        brush = MainGradient,
                        fontWeight = FontWeight.Bold
                    )
                ) {
                    append(correctWord)
                }

                append(parts.getOrNull(1) ?: "")
            } else {
                append(sentence)
            }
        }
    }
}