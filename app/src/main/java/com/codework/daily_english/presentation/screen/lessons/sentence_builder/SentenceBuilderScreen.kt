package com.codework.daily_english.presentation.screen.lessons.sentence_builder

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codework.daily_english.core.base.BaseState
import com.codework.daily_english.presentation.components.*
import com.codework.daily_english.presentation.screen.lessons.sentence_builder.model.SentenceBuilderQuestion
import com.codework.daily_english.presentation.screen.lessons.sentence_builder.model.WordChip
import com.codework.daily_english.presentation.screen.lessons.sentence_builder.mvi.SentenceBuilderEvent
import com.codework.daily_english.presentation.screen.lessons.sentence_builder.mvi.SentenceBuilderState
import com.codework.daily_english.presentation.theme.*

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SentenceBuilderScreen(
    state: BaseState<SentenceBuilderState>, // SỬA: Data -> State
    onEvent: (SentenceBuilderEvent) -> Unit,
    onBackClick: () -> Unit
) {
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scrollState = rememberScrollState()

    // --- STANDARD PADDING ---
    val standardPadding = 20.dp

    if (state is BaseState.Success && state.data.isTheoryVisible) {
        ModalBottomSheet(
            onDismissRequest = { onEvent(SentenceBuilderEvent.OnToggleTheory) },
            sheetState = bottomSheetState,
            containerColor = Color.White,
            dragHandle = { BottomSheetDefaults.DragHandle() },
        ) {
            state.data.currentQuestion?.let { q ->
                TheoryContent(
                    title = q.grammarTitle,
                    content = q.grammarContent,
                    example = q.grammarExample,
                    onGotIt = { onEvent(SentenceBuilderEvent.OnToggleTheory) }
                )
            }
        }
    }

    Scaffold(
        containerColor = AppBackground,
        topBar = {
            if (state is BaseState.Success) {
                ProgressTopBar(
                    currentStep = state.data.progressCurrent,
                    totalSteps = state.data.progressTotal,
                    onBackClick = onBackClick,
                    onCloseClick = null
                )
            } else {
                AppTopBar(onBackClick = onBackClick, titleContent = {})
            }
        },
        bottomBar = {
            if (state is BaseState.Success) {
                val data = state.data

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Transparent)
                ) {
                    // --- FEEDBACK SECTION ---
                    AnimatedVisibility(
                        visible = data.isCorrect != null,
                        enter = fadeIn() + slideInVertically { it / 2 }
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = standardPadding)
                        ) {
                            FeedbackSection(
                                isCorrect = data.isCorrect == true,
                                correctSentence = data.currentQuestion?.correctSentence?.joinToString(" ") ?: ""
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(if (data.isCorrect != null) 24.dp else 16.dp))

                    // --- ACTION BUTTON ---
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = standardPadding)
                            .padding(bottom = 32.dp)
                    ) {
                        // Xác định xem đây có phải câu hỏi cuối cùng không
                        val isLastQuestion = data.progressCurrent == data.progressTotal

                        // Logic Text Button:
                        // - Chưa trả lời hoặc Sai: "Check Answer"
                        // - Đúng và CHƯA PHẢI câu cuối: "Continue"
                        // - Đúng và LÀ câu cuối: "Finish"
                        val buttonText = if (data.isCorrect == true) {
                            if (isLastQuestion) "Finish" else "Continue"
                        } else {
                            "Check Answer"
                        }

                        PrimaryButton(
                            text = buttonText,
                            enabled = data.selectedWords.isNotEmpty(),
                            onClick = {
                                if (data.isCorrect == true)
                                    onEvent(SentenceBuilderEvent.OnNextQuestion)
                                else
                                    onEvent(SentenceBuilderEvent.OnCheckAnswer)
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        BaseScreen(state = state) { data ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(scrollState)
                    .padding(horizontal = standardPadding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // --- HEADER: HINT BUTTON ---
                Box(modifier = Modifier.fillMaxWidth().padding(top = 8.dp), contentAlignment = Alignment.CenterEnd) {
                    Surface(
                        onClick = { onEvent(SentenceBuilderEvent.OnToggleTheory) },
                        shape = CircleShape,
                        color = Color(0xFFFFF7ED),
                        border = BorderStroke(1.dp, Color(0xFFFED7AA)),
                        modifier = Modifier.size(44.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Outlined.Lightbulb, contentDescription = "Hint", tint = Color(0xFFF97316))
                        }
                    }
                }

                // --- 1. QUESTION ZONE ---
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "English please!",
                    fontSize = 18.sp,
                    color = BluePrimary,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = data.currentQuestion?.vietnamese ?: "",
                    fontSize = 20.sp,
                    color = TextPrimary,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(32.dp))

                // --- 2. ANSWER ZONE ---
                val isAnswerEmpty = data.selectedWords.isEmpty()
                val isCorrect = data.isCorrect

                val borderColor = when (isCorrect) {
                    true -> SuccessGreen
                    false -> USRed
                    else -> BluePrimary.copy(alpha = 0.3f)
                }

                val backgroundColor = when (isCorrect) {
                    true -> Color(0xFFF0FDF4)
                    false -> Color(0xFFFEF2F2)
                    else -> Color.White
                }

                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Your Answer",
                        fontSize = 15.sp,
                        color = GrayText,
                        modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .defaultMinSize(minHeight = 150.dp)
                            .shadow(2.dp, RoundedCornerShape(20.dp), spotColor = Color.LightGray)
                            .clip(RoundedCornerShape(20.dp))
                            .background(backgroundColor)
                            .then(
                                if (isAnswerEmpty) {
                                    Modifier.drawBehind {
                                        val stroke = Stroke(
                                            width = 4f,
                                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(25f, 25f), 0f)
                                        )
                                        drawRoundRect(
                                            color = Color(0xFFCBD5E1),
                                            style = stroke,
                                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(20.dp.toPx())
                                        )
                                    }
                                } else {
                                    Modifier.border(BorderStroke(1.dp, borderColor), RoundedCornerShape(20.dp))
                                }
                            )
                            .padding(20.dp),
                        contentAlignment = if (isAnswerEmpty) Alignment.Center else Alignment.TopCenter
                    ) {
                        if (isAnswerEmpty) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Default.School, contentDescription = null, tint = Color(0xFFCBD5E1), modifier = Modifier.size(32.dp))
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("Tap words below to arrange", color = Color(0xFF94A3B8), fontWeight = FontWeight.Medium)
                            }
                        } else {
                            FlowRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                data.selectedWords.forEach { wordChip ->
                                    BigWordChip(
                                        text = wordChip.text,
                                        isSelected = true,
                                        onClick = { onEvent(SentenceBuilderEvent.OnDeselectWord(wordChip)) }
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))

                // --- 3. POOL ZONE ---
                if (data.availableWords.isNotEmpty()) {
                    Text(
                        text = "Choose the words",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = BluePrimary,
                        letterSpacing = 1.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider(
                        color = Color(0xFFE5E7EB),
                        thickness = 1.dp,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    FlowRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        data.availableWords.forEach { wordChip ->
                            BigWordChip(
                                text = wordChip.text,
                                isSelected = false,
                                onClick = { onEvent(SentenceBuilderEvent.OnSelectWord(wordChip)) }
                            )
                        }
                    }
                } else {
                    // --- THÊM PHẦN ALL WORDS SELECTED Ở ĐÂY ---
                    Text(
                        text = "Choose the words",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = BluePrimary.copy(alpha = 0.5f), // Mờ đi khi đã chọn hết
                        letterSpacing = 1.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider(
                        color = Color(0xFFE5E7EB),
                        thickness = 1.dp,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .drawBehind {
                                val stroke = Stroke(
                                    width = 4f,
                                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 20f), 0f)
                                )
                                drawRoundRect(
                                    color = Color(0xFFE2E8F0),
                                    style = stroke,
                                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(16.dp.toPx())
                                )
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Default.Check,
                                contentDescription = null,
                                tint = SuccessGreen.copy(alpha = 0.5f),
                                modifier = Modifier.size(36.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "All words selected",
                                color = Color(0xFF94A3B8),
                                fontWeight = FontWeight.Medium,
                                fontSize = 15.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(200.dp))
            }
        }
    }
}

// --- EXTRACTED COMPONENTS ---

@Composable
fun FeedbackSection(isCorrect: Boolean, correctSentence: String) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- A. AUDIO CONTROLS ---
        if (isCorrect) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    onClick = { /* TODO: Play Normal Audio */ },
                    shape = CircleShape,
                    color = BluePrimary,
                    shadowElevation = 4.dp,
                    modifier = Modifier.size(50.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(Icons.AutoMirrored.Filled.VolumeUp, null, tint = Color.White)
                    }
                }

                Spacer(modifier = Modifier.width(32.dp))

                Surface(
                    onClick = { /* TODO: Play Slow Audio */ },
                    shape = CircleShape,
                    color = Color(0xFFE0F2FE),
                    border = BorderStroke(1.dp, BluePrimary),
                    modifier = Modifier.size(50.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(text = "🐌", fontSize = 24.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        // --- B. FEEDBACK CARD ---
        val containerColor = if (isCorrect) SuccessGreen.copy(alpha = 0.1f) else USRed.copy(alpha = 0.1f)
        val contentColor = if (isCorrect) SuccessGreen else USRed
        val emoji = if (isCorrect) "😎" else "😭"
        val title = if (isCorrect) "Excellent!" else "Incorrect!"

        val feedbackText = if (isCorrect) correctSentence else "Correct: $correctSentence"

        Card(
            colors = CardDefaults.cardColors(containerColor = containerColor),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = emoji, fontSize = 42.sp)

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = contentColor
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = feedbackText,
                        style = MaterialTheme.typography.bodyMedium,
                        color = contentColor.copy(alpha = 0.9f)
                    )
                }
            }
        }
    }
}


@Composable
fun BigWordChip(text: String, isSelected: Boolean, onClick: () -> Unit) {
    val containerColor = Color.White
    val borderColor = if (isSelected) BluePrimary else Color(0xFFE5E7EB)
    val shadowElevation = 2.dp
    val shape = RoundedCornerShape(12.dp)

    Surface(
        onClick = onClick,
        shape = shape,
        color = containerColor,
        border = BorderStroke(1.dp, if (isSelected) BluePrimary.copy(alpha = 0.5f) else Color(0xFFE2E8F0)),
        shadowElevation = shadowElevation,
        modifier = Modifier
            .height(48.dp)
            .defaultMinSize(minWidth = 50.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(horizontal = 12.dp)
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) BluePrimary else TextPrimary
            )
        }
    }
}

@Composable
fun TheoryContent(title: String, content: String, example: String, onGotIt: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(shape = CircleShape, color = HintBackGround, modifier = Modifier.size(60.dp)) {
            Box(contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Lightbulb, null, tint = Color(0xFFF59E0B), modifier = Modifier.size(30.dp))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = TextPrimary)
        Spacer(modifier = Modifier.height(12.dp))
        Text(content, style = MaterialTheme.typography.bodyLarge, color = GrayText, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(16.dp))
        Surface(shape = RoundedCornerShape(12.dp), color = AppBackground, modifier = Modifier.fillMaxWidth()) {
            Row(modifier = Modifier.padding(16.dp)) {
                Text("Example: ", fontWeight = FontWeight.Bold, color = BluePrimary)
                Text(example, color = TextPrimary, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
        PrimaryButton(text = "Got it!", onClick = onGotIt, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(24.dp))
    }
}

// --- PREVIEW SECTION ---
@Preview(showBackground = true, name = "Start State")
@Composable
fun PreviewSentenceBuilder() {
    val mockData = SentenceBuilderState(
        currentQuestion = SentenceBuilderQuestion(vietnamese = "Tôi muốn một tách trà.", correctSentence = listOf("I", "'d", "like", "a", "cup", "of", "tea")),
        availableWords = listOf(WordChip("1", "I"), WordChip("2", "'d"), WordChip("3", "like"), WordChip("4", "a"), WordChip("5", "cup"), WordChip("6", "of"), WordChip("7", "tea")),
        progressTotal = 3, progressCurrent = 1
    )
    SentenceBuilderScreen(state = BaseState.Success(mockData), onEvent = {}, onBackClick = {})
}

@Preview(showBackground = true, name = "Correct State")
@Composable
fun PreviewSentenceBuilderCorrect() {
    val mockData = SentenceBuilderState(
        currentQuestion = SentenceBuilderQuestion(
            vietnamese = "Tôi muốn một tách trà.",
            correctSentence = listOf("I", "'d", "like", "a", "cup", "of", "tea")
        ),
        selectedWords = listOf(WordChip("1", "I"), WordChip("2", "'d"), WordChip("3", "like"), WordChip("4", "a"), WordChip("5", "cup"), WordChip("6", "of"), WordChip("7", "tea")),
        isCorrect = true, progressTotal = 3, progressCurrent = 1
    )
    SentenceBuilderScreen(state = BaseState.Success(mockData), onEvent = {}, onBackClick = {})
}