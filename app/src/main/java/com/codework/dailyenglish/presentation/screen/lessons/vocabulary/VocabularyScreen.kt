package com.codework.dailyenglish.presentation.screen.lessons.vocabulary

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Đảm bảo import đúng đường dẫn của project bạn
import com.codework.dailyenglish.presentation.components.VocabularyCompleteDialog
import com.codework.dailyenglish.presentation.components.QuizOption
import com.codework.dailyenglish.presentation.screen.lessons.vocabulary.mvi.VocabularyEvent
import com.codework.dailyenglish.presentation.screen.lessons.vocabulary.mvi.VocabularyState
import com.codework.dailyenglish.presentation.theme.AppBackground
import com.codework.dailyenglish.presentation.theme.BluePrimary
import com.codework.dailyenglish.presentation.theme.GrayText
import com.codework.dailyenglish.presentation.theme.TextPrimary

@Composable
fun VocabularyScreen(
    state: VocabularyState, // SỬA: Đổi từ VocabularyData -> VocabularyState
    onEvent: (VocabularyEvent) -> Unit,
    onBackClick: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    var showWarningTrigger by remember { mutableIntStateOf(0) }
    var showCompletionDialog by remember { mutableStateOf(false) }

    // --- XỬ LÝ NÚT BACK VẬT LÝ / VUỐT MÀN HÌNH ---
    BackHandler {
        onEvent(VocabularyEvent.OnGoBack)
    }

//    val progressCurrent = (state as? BaseState.Success)?.data?.progressCurrent ?: 0
//    val progressTotal = (state as? BaseState.Success)?.data?.progressTotal ?: 10

    Scaffold(
        containerColor = AppBackground,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
//            ProgressTopBar(
//                currentStep = progressCurrent,
//                totalSteps = progressTotal,
//                onBackClick = { onEvent(VocabularyEvent.OnGoBack) }
//            )
        },
        bottomBar = {
//            if (state is BaseState.Success) {
//                val data = state.data
//                val isSelected = data.selectedOptionIndex != null
//                val textBtn = if (data.progressCurrent >= data.progressTotal) "Finish" else "Next"
//
//                // Giữ nguyên Box bọc ngoài với padding 32.dp bottom để không bị sát lề
//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(start = 16.dp, end = 16.dp, bottom = 32.dp, top = 16.dp)
//                ) {
//                    PrimaryButton(
//                        text = textBtn,
//                        enabled = isSelected, // Truyền trạng thái để tự đổi màu
//                        onClick = {
//                            if (!isSelected) {
//                                showWarningTrigger++
//                            } else {
//                                if (data.progressCurrent < data.progressTotal) {
//                                    onEvent(VocabularyEvent.OnNextWord)
//                                } else {
//                                    showCompletionDialog = true
//                                }
//                            }
//                        },
//                        modifier = Modifier.fillMaxWidth()
//                    )
//                }
//            }
        }
    ) { paddingValues ->

            val listState = rememberLazyListState()

//            LaunchedEffect(data.word) {
//                listState.scrollToItem(0)
//            }
//
//            LaunchedEffect(data.isMeaningRevealed) {
//                if (data.isMeaningRevealed) {
//                    delay(100)
//                    listState.animateScrollBy(
//                        value = 1550f,
//                        animationSpec = tween(
//                            durationMillis = 500,
//                            easing = FastOutSlowInEasing
//                        )
//                    )
//                }
//            }

            LaunchedEffect(showWarningTrigger) {
                if (showWarningTrigger > 0) {
                    snackbarHostState.currentSnackbarData?.dismiss()
                    snackbarHostState.showSnackbar(
                        message = "Vui lòng chọn đáp án trước khi qua từ mới \uD83D\uDE09",
                        duration = SnackbarDuration.Short,
                        withDismissAction = true
                    )
                }
            }

            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 1. ẢNH MINH HỌA
                item {
                    Spacer(modifier = Modifier.height(16.dp))
//                    AsyncImage(
//                        model = data.imageUrl,
//                        contentDescription = null,
//                        contentScale = ContentScale.Crop,
//                        modifier = Modifier
//                            .size(140.dp)
//                            .clip(RoundedCornerShape(16.dp))
//                    )
                }

//                // 2. BADGES
//                item {
//                    Spacer(modifier = Modifier.height(24.dp))
//                    Row(
//                        horizontalArrangement = Arrangement.Center,
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        Badge(text = data.level, color = BluePrimary, textColor = Color.White)
//                        Spacer(modifier = Modifier.width(8.dp))
//                        Text("•", color = GrayText)
//                        Spacer(modifier = Modifier.width(8.dp))
//                        Badge(
//                            text = data.partOfSpeech,
//                            color = BluePrimary.copy(alpha = 0.1f),
//                            textColor = BluePrimary
//                        )
//                    }
//                }
//
//                // 3. TỪ VỰNG CHÍNH
//                item {
//                    Spacer(modifier = Modifier.height(16.dp))
//                    Text(
//                        text = data.word,
//                        style = MaterialTheme.typography.displaySmall,
//                        fontWeight = FontWeight.Bold,
//                        color = TextPrimary,
//                        textAlign = TextAlign.Center
//                    )
//                }
//
//                // 4. AUDIO
//                item {
//                    Spacer(modifier = Modifier.height(24.dp))
//                    Column(
//                        modifier = Modifier.fillMaxWidth(),
//                        horizontalAlignment = Alignment.CenterHorizontally,
//                        verticalArrangement = Arrangement.spacedBy(16.dp)
//                    ) {
//                        PhoneticGroup(
//                            label = "UK",
//                            phonetic = data.ukPhonetic,
//                            mainColor = Color(0xFFE91E63),
//                            onClick = { onEvent(VocabularyEvent.OnPlayAudio(AudioRegion.UK)) }
//                        )
//                        PhoneticGroup(
//                            label = "US",
//                            phonetic = data.usPhonetic,
//                            mainColor = Color(0xFF3B82F6),
//                            onClick = { onEvent(VocabularyEvent.OnPlayAudio(AudioRegion.US)) }
//                        )
//                    }
//                }

                // 5. GỢI Ý
                item {
                    Spacer(modifier = Modifier.height(24.dp))

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        HorizontalDivider(
                            color = Color(0xFFE5E7EB),
                            thickness = 1.dp,
                            modifier = Modifier.padding(horizontal = 12.dp)
                        )

                        Text(
                            text = "Hints",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = BluePrimary,
                            letterSpacing = 1.sp,
                            modifier = Modifier.padding(top = 14.dp, bottom = 6.dp)
                        )

//                        data.hints.forEach { hint ->
//                            Text(
//                                text = "•  $hint",
//                                style = MaterialTheme.typography.bodyMedium,
//                                color = TextPrimary,
//                                fontSize = 15.sp,
//                                lineHeight = 22.sp,
//                                textAlign = TextAlign.Center,
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                                    .padding(vertical = 4.dp, horizontal = 16.dp)
//                            )
//                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        HorizontalDivider(
                            color = Color(0xFFE5E7EB),
                            thickness = 1.dp,
                            modifier = Modifier.padding(horizontal = 12.dp)
                        )
                    }
                }

                // 6. QUIZ
                item {
                    Text(
                        text = "What does it mean?",
                        fontSize = 17.sp,
                        color = BluePrimary,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(top = 14.dp, bottom = 14.dp),
                        textAlign = TextAlign.Center
                    )

//                    QuizGridSection(
//                        options = data.quizOptions,
//                        selectedIndex = data.selectedOptionIndex,
//                        correctIndex = data.correctAnswerIndex,
//                        onOptionSelected = { index -> onEvent(VocabularyEvent.OnSelectOption(index)) }
//                    )
                }

                // 7. GIẢI THÍCH CHI TIẾT
//                item {
//                    AnimatedVisibility(
//                        visible = data.isMeaningRevealed,
//                        enter = fadeIn() + expandVertically()
//                    ) {
//                        Column(
//                            modifier = Modifier.fillMaxWidth(),
//                            horizontalAlignment = Alignment.CenterHorizontally
//                        ) {
//                            Spacer(modifier = Modifier.height(24.dp))
//                            HorizontalDivider(color = Color.LightGray.copy(0.3f))
//                            Spacer(modifier = Modifier.height(12.dp))
//
//                            Text(
//                                text = "Detailed explanation",
//                                fontSize = 17.sp,
//                                color = BluePrimary,
//                                fontWeight = FontWeight.Bold,
//                                letterSpacing = 1.sp
//                            )
//
//                            Spacer(modifier = Modifier.height(10.dp))
//
//                            Text(
//                                text = data.meaning,
//                                fontSize = 20.sp,
//                                fontWeight = FontWeight.Bold,
//                                color = TextPrimary,
//                                textAlign = TextAlign.Center
//                            )
//
//                            Spacer(modifier = Modifier.height(24.dp))
//
//                            // ====================================================
//                            // KHỐI 1: EXAMPLES (VÍ DỤ)
//                            // ====================================================
//                            Column(
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                                    .clip(RoundedCornerShape(16.dp))
//                                    .background(HintBackGround)
//                                    .padding(20.dp), // Padding đều 4 góc
//                                horizontalAlignment = Alignment.CenterHorizontally
//                            ) {
//                                Text(
//                                    text = "Examples", // Label bên trong nền xanh
//                                    fontWeight = FontWeight.Bold,
//                                    color = TextPrimary, // Đổi màu BluePrimary
//                                    letterSpacing = 0.5.sp,
//                                    fontSize = 16.sp,
//                                    modifier = Modifier.padding(bottom = 6.dp)
//                                )
//
//                                Column(modifier = Modifier.fillMaxWidth()) {
//                                    data.examples.forEach { example ->
//                                        ExampleRow(fullText = example, highlightWord = data.word)
//                                    }
//                                }
//                            }
//
//                            Spacer(modifier = Modifier.height(16.dp))
//
//                            // ====================================================
//                            // KHỐI 2: COMMON PHRASES (CỤM TỪ PHỔ BIẾN) CÓ VÍ DỤ
//                            // ====================================================
//                            Column(
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                                    .clip(RoundedCornerShape(16.dp))
//                                    .background(HintBackGround)
//                                    .padding(20.dp), // Padding đều 4 góc
//                                horizontalAlignment = Alignment.CenterHorizontally
//                            ) {
//                                Text(
//                                    text = "Common phrases", // Label bên trong nền xanh
//                                    fontWeight = FontWeight.Bold,
//                                    color = TextPrimary, // Đổi màu BluePrimary
//                                    letterSpacing = 0.5.sp,
//                                    fontSize = 16.sp,
//                                    modifier = Modifier.padding(bottom = 6.dp)
//                                )
//
//                                Column(modifier = Modifier.fillMaxWidth()) {
//                                    // Dữ liệu mẫu dạng Pair(Cụm từ, Ví dụ)
//                                    val commonPhrases = listOf(
//                                        Pair("find accommodation", "We need to find accommodation before the trip."),
//                                        Pair("provide accommodation", "The company provides accommodation for its employees."),
//                                        Pair("temporary accommodation", "They are living in temporary accommodation at the moment.")
//                                    )
//                                    commonPhrases.forEach { (phrase, example) ->
//                                        CommonPhraseRow(phrase = phrase, example = example)
//                                    }
//                                }
//                            }
//
//                            Spacer(modifier = Modifier.height(32.dp))
//                        }
//                    }
//                }
            }
        }

        if (showCompletionDialog) {
            VocabularyCompleteDialog(
                onConfirm = {
                    showCompletionDialog = false
                    onEvent(VocabularyEvent.OnNavigateToReviewScreen)
                },
                onDismiss = { showCompletionDialog = false }
            )
        }
}

// --- SUB-COMPONENTS ---

@Composable
fun BulletPoint(modifier: Modifier = Modifier, color: Color = TextPrimary) {
    Box(
        modifier = modifier
            .size(5.dp)
            .clip(CircleShape)
            .background(color)
    )
}

// Đã nâng cấp CommonPhraseRow để nhận vào và hiển thị cả cụm từ và ví dụ
@Composable
fun CommonPhraseRow(phrase: String, example: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        // Cụm từ được in đậm
        Row(verticalAlignment = Alignment.Top) {
            BulletPoint(modifier = Modifier.padding(top = 9.dp, end = 12.dp), color = GrayText)
            Text(
                text = phrase,
                fontWeight = FontWeight.Bold,
                color = TextPrimary, // Từ vựng chính màu tối/đậm để nổi bật
                fontSize = 15.sp,
                lineHeight = 24.sp
            )
        }
        // Câu ví dụ nằm phía dưới, thụt lề bằng với chữ
        Text(
            text = example,
            style = MaterialTheme.typography.bodyMedium,
            color = GrayText, // Màu xám cho câu ví dụ
            fontSize = 15.sp,
            lineHeight = 24.sp,
            modifier = Modifier.padding(top = 4.dp, start = 17.dp) // 17dp = 5dp (bullet) + 12dp (khoảng cách)
        )
    }
}

@Composable
fun ExampleRow(fullText: String, highlightWord: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.Top
    ) {
        BulletPoint(modifier = Modifier.padding(top = 9.dp, end = 12.dp), color = GrayText)

        val parts = fullText.split(" ")
        val annotatedString = buildAnnotatedString {
            parts.forEach { word ->
                val cleanWord = word.filter { it.isLetter() }
                val isHighlight = cleanWord.equals(highlightWord, ignoreCase = true) ||
                        highlightWord.contains(cleanWord, ignoreCase = true)
                if (isHighlight) {
                    // Từ được bôi đậm dùng TextPrimary
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = TextPrimary)) {
                        append("$word ")
                    }
                } else {
                    // Chữ bình thường dùng GrayText
                    withStyle(SpanStyle(color = GrayText)) {
                        append("$word ")
                    }
                }
            }
        }

        Text(
            text = annotatedString,
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 15.sp,
            lineHeight = 24.sp,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun PhoneticGroup(
    label: String,
    phonetic: String,
    mainColor: Color,
    onClick: () -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Surface(
            onClick = onClick,
            shape = CircleShape,
            color = Color.White,
            border = BorderStroke(1.dp, mainColor.copy(alpha = 0.3f)),
            modifier = Modifier.size(32.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Default.VolumeUp,
                    contentDescription = null,
                    tint = mainColor,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "$label:",
                style = MaterialTheme.typography.labelSmall,
                color = mainColor,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = phonetic.ifEmpty { "/.../" },
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF475569),
                fontWeight = FontWeight.Normal,
                fontSize = 15.sp
            )
        }
    }
}

@Composable
fun QuizGridSection(
    options: List<String>,
    selectedIndex: Int?,
    correctIndex: Int,
    onOptionSelected: (Int) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            if (options.isNotEmpty()) Box(Modifier.weight(1f)) {
                QuizOption(options[0], 0, selectedIndex, correctIndex, onOptionSelected)
            }
            if (options.size > 1) Box(Modifier.weight(1f)) {
                QuizOption(options[1], 1, selectedIndex, correctIndex, onOptionSelected)
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            if (options.size > 2) Box(Modifier.weight(1f)) {
                QuizOption(options[2], 2, selectedIndex, correctIndex, onOptionSelected)
            }
            if (options.size > 3) Box(Modifier.weight(1f)) {
                QuizOption(options[3], 3, selectedIndex, correctIndex, onOptionSelected)
            }
        }
    }
}

@Composable
fun Badge(text: String, color: Color, textColor: Color) {
    Surface(color = color, shape = RoundedCornerShape(6.dp)) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = textColor,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}