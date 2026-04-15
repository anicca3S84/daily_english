package com.codework.dailyenglish.presentation.screen.translate

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.History
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.codework.dailyenglish.presentation.components.AppTopBar
import com.codework.dailyenglish.presentation.components.StateWrapper
import com.codework.dailyenglish.presentation.screen.translate.mvi.TranslateEvent
import com.codework.dailyenglish.presentation.screen.translate.mvi.TranslateState
import com.codework.dailyenglish.presentation.theme.AppBackground
import com.codework.dailyenglish.presentation.screen.translate.components.InputCard
import com.codework.dailyenglish.presentation.screen.translate.components.LanguageSwitchBar
import com.codework.dailyenglish.presentation.screen.translate.components.ResultCard
import com.codework.dailyenglish.presentation.theme.BluePrimary
import com.codework.dailyenglish.R
import com.codework.dailyenglish.presentation.screen.translate.components.AnimatedHistoryItem
import com.codework.dailyenglish.presentation.theme.Colors
import com.codework.dailyenglish.presentation.theme.Spaces

@Composable
fun TranslateScreen(
    state: TranslateState,
    onEvent: (TranslateEvent) -> Unit,
    onBackClick: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        containerColor = AppBackground,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            AppTopBar(
                onBackClick = onBackClick,
                titleContent = {
                    Text(
                        text = stringResource(R.string.title_translate),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Colors.TitleColor
                    )
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(AppBackground),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Column(modifier = Modifier.padding(horizontal = Spaces.Space16)) {
                    Spacer(modifier = Modifier.height(Spaces.Space16))

                    LanguageSwitchBar(
                        sourceLang = state.sourceLang,
                        targetLang = state.targetLang,
                        onSwap = { onEvent(TranslateEvent.OnSwapLanguage) }
                    )

                    Spacer(modifier = Modifier.height(Spaces.Space20))

                    InputCard(
                        text = state.sourceText,
                        hint = stringResource(R.string.hint_type_to_translate),
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

                    Spacer(modifier = Modifier.height(Spaces.Space16))
                }
            }

            item {
                AnimatedVisibility(
                    visible = state.targetText.isNotEmpty() || state.isTranslating,
                    enter = fadeIn() + slideInVertically { it / 2 },
                    modifier = Modifier.padding(horizontal = Spaces.Space16)
                ) {
                    Column {
                        ResultCard(
                            text = state.targetText,
                            isTranslating = state.isTranslating
                        )
                        Spacer(modifier = Modifier.height(Spaces.Space8))
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Spaces.Space16, vertical = Spaces.Space8),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Rounded.History,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(Spaces.Space20)
                        )
                        Spacer(modifier = Modifier.width(Spaces.Space8))
                        Text(
                            text = stringResource(R.string.label_recent_history),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Colors.TitleColor
                        )
                    }

                    TextButton(
                        onClick = { onEvent(TranslateEvent.OnSeeAllClick) }
                    ) {
                        Text(
                            text = stringResource(R.string.label_see_all),
                            color = BluePrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Spaces.Space16)
                ) {
                    StateWrapper(
                        uiState = state.historyState,
                        snackbarHostState = snackbarHostState,
                        onRetry = { }
                    ) { historyList ->
                        Column(
                            verticalArrangement = Arrangement.spacedBy(Spaces.Space10),
                            modifier = Modifier.animateContentSize()
                        ) {
                            if (historyList.isNullOrEmpty()) {
                                Text(
                                    text = stringResource(R.string.label_no_history),
                                    modifier = Modifier
                                        .padding(vertical = Spaces.Space20)
                                        .align(Alignment.CenterHorizontally),
                                    color = Color.Gray
                                )
                            } else {
                                historyList.forEach { item ->
                                    key(item.id) {
                                        AnimatedHistoryItem(
                                            source = item.sourceText,
                                            target = item.targetText,
                                            onClick = { }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(Spaces.Space32))
            }
        }
    }
}


