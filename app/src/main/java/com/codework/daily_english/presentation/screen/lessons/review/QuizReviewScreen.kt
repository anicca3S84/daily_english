package com.codework.daily_english.presentation.screen.lessons.review

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codework.daily_english.presentation.components.BaseScreen
import com.codework.daily_english.presentation.components.PrimaryButton
import com.codework.daily_english.presentation.components.ProgressTopBar
import com.codework.daily_english.presentation.components.QuizOption
import com.codework.daily_english.presentation.components.QuizVocabularyCompleteDialog
import com.codework.daily_english.presentation.components.UiState
import com.codework.daily_english.presentation.model.QuizType
import com.codework.daily_english.presentation.theme.AppBackground
import com.codework.daily_english.presentation.theme.BluePrimary
import com.codework.daily_english.presentation.theme.HintBackGround
import com.codework.daily_english.presentation.theme.MainGradient
import com.codework.daily_english.presentation.theme.TextPrimary

@Composable
fun QuizReviewScreen(
    state: UiState<QuizReviewState>, // SỬA: Đổi QuizReviewData thành QuizReviewState
    onEvent: (QuizReviewEvent) -> Unit,
    onBackClick: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    var showWarningTrigger by remember { mutableIntStateOf(0) }
    var showCompletionDialog by remember { mutableStateOf(false) }

    BackHandler { onEvent(QuizReviewEvent.OnGoBack) }

    val progressCurrent = (state as? UiState.Success)?.data?.progressCurrent ?: 0
    val progressTotal = (state as? UiState.Success)?.data?.progressTotal ?: 10

    Scaffold(
        containerColor = AppBackground,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            ProgressTopBar(
                currentStep = progressCurrent,
                totalSteps = progressTotal,
                onBackClick = { onEvent(QuizReviewEvent.OnGoBack) }
            )
        },
        bottomBar = {
            if (state is UiState.Success) {
                val data = state.data
                val isSelected = data.selectedOptionIndex != null
                val textBtn = if (data.progressCurrent >= data.progressTotal) "Finish" else "Next"

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, bottom = 32.dp, top = 16.dp)
                ) {
                    PrimaryButton(
                        text = textBtn,
                        enabled = isSelected,
                        onClick = {
                            if (!isSelected) {
                                showWarningTrigger++
                            } else {
                                if (data.progressCurrent < data.progressTotal) {
                                    onEvent(QuizReviewEvent.OnNextQuestion)
                                } else {
                                    showCompletionDialog = true
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
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
                        message = "Vui lòng chọn 1 đáp án để tiếp tục nhé! 💪",
                        duration = SnackbarDuration.Short,
                        withDismissAction = true
                    )
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // ==========================================
                // BADGE
                // ==========================================
                item {
                    Spacer(modifier = Modifier.height(16.dp))

                    val badgeIcon = if (data.type == QuizType.VOCABULARY) "🧠" else "🎧"
                    val badgeText = if (data.type == QuizType.VOCABULARY) "REVIEW PHASE" else "COMPREHENSION"

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(HintBackGround)
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(text = badgeIcon, fontSize = 16.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = badgeText,
                            color = BluePrimary,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 14.sp,
                            letterSpacing = 1.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                }

                // ==========================================
                // QUESTION & ANSWERS CARD
                // ==========================================
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(HintBackGround)
                            .padding(horizontal = 20.dp, vertical = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(20.dp))

                        val headerText = if (data.type == QuizType.VOCABULARY) "Fill in the blank" else "Choose the correct answer"

                        Text(
                            text = headerText,
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            letterSpacing = 0.5.sp
                        )
                        Spacer(modifier = Modifier.height(24.dp))

                        // QUESTION
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            color = Color.White,
                            shadowElevation = 4.dp,
                            border = BorderStroke(1.5.dp, BluePrimary)
                        ) {
                            Box(
                                modifier = Modifier.padding(20.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = formatBlankSentence(
                                        sentence = data.sentence,
                                        isRevealed = data.selectedOptionIndex != null,
                                        correctWord = data.options.getOrNull(data.correctAnswerIndex) ?: ""
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

                        // TRANSLATION
                        AnimatedVisibility(visible = data.selectedOptionIndex != null) {
                            Text(
                                text = data.translatedSentence,
                                color = Color(0xFF64748B),
                                fontSize = 16.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .padding(horizontal = 8.dp)
                                    .padding(bottom = 24.dp)
                            )
                        }

                        // OPTIONS GRID
                        if (data.type == QuizType.VOCABULARY) {
                            val rows = data.options.chunked(2)
                            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                rows.forEach { rowItems ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        rowItems.forEachIndexed { _, optionText ->
                                            val currentIndexInOptions = data.options.indexOf(optionText)
                                            Box(modifier = Modifier.weight(1f)) {
                                                QuizOption(
                                                    text = optionText,
                                                    index = currentIndexInOptions,
                                                    selectedIndex = data.selectedOptionIndex,
                                                    correctIndex = data.correctAnswerIndex,
                                                    onClick = { index ->
                                                        onEvent(QuizReviewEvent.OnSelectOption(index))
                                                    }
                                                )
                                            }
                                        }
                                        if (rowItems.size < 2) Spacer(modifier = Modifier.weight(1f))
                                    }
                                }
                            }
                        } else {
                            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                data.options.forEachIndexed { currentIndexInOptions, optionText ->
                                    Box(modifier = Modifier.fillMaxWidth()) {
                                        QuizOption(
                                            text = optionText,
                                            index = currentIndexInOptions,
                                            selectedIndex = data.selectedOptionIndex,
                                            correctIndex = data.correctAnswerIndex,
                                            onClick = { index ->
                                                onEvent(QuizReviewEvent.OnSelectOption(index))
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }
            }

            if (showCompletionDialog) {
                // Xác định hướng đi tiếp theo dựa trên loại bài kiểm tra
                val isListening = data.type == QuizType.LISTENING
                val nextButtonLabel = if (isListening) "Continue to Grammar" else "Continue to Listening"

                QuizVocabularyCompleteDialog(
                    score = 7,
                    total = 10,
                    // Truyền nhãn nút bấm
                    nextButtonText = nextButtonLabel,
                    // Xử lý logic chuyển màn hình
                    onContinue = {
                        showCompletionDialog = false
                        if (isListening) {
                            onEvent(QuizReviewEvent.OnNavigateToGrammarScreen)
                        } else {
                            onEvent(QuizReviewEvent.OnNavigateToListeningScreen)
                        }
                    },
                    onRetryQuiz = { showCompletionDialog = false },
                    onRelearnWords = { showCompletionDialog = false },
                    onDismiss = { showCompletionDialog = false }
                )
            }
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