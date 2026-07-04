package com.codework.dailyenglish.presentation.screen.translate

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.core.content.ContextCompat
import com.codework.dailyenglish.presentation.components.AppTopBar
import com.codework.dailyenglish.presentation.components.StateWrapper
import com.codework.dailyenglish.presentation.screen.translate.mvi.TranslateEvent
import com.codework.dailyenglish.presentation.screen.translate.mvi.TranslateState
import com.codework.dailyenglish.presentation.screen.translate.components.InputCard
import com.codework.dailyenglish.presentation.screen.translate.components.LanguageSwitchBar
import com.codework.dailyenglish.presentation.screen.translate.components.ResultCard
import com.codework.dailyenglish.R
import com.codework.dailyenglish.presentation.screen.translate.components.AnimatedHistoryItem
import com.codework.dailyenglish.presentation.theme.AppColors
import com.codework.dailyenglish.presentation.theme.Spaces
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import com.codework.dailyenglish.presentation.theme.AppTextSizes

@Composable
fun TranslateScreen(
    state: TranslateState,
    onEvent: (TranslateEvent) -> Unit,
    onBackClick: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val focusManager = LocalFocusManager.current

    var hasMicPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val micPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            hasMicPermission = isGranted
            if (isGranted) {
                onEvent(TranslateEvent.OnMicClick)
            }
        }
    )

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                onEvent(TranslateEvent.OnCameraClick)
            }
        }
    )

    Scaffold(
        containerColor = AppColors.AppBackground,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            AppTopBar(
                onBackClick = onBackClick,
                titleContent = {
                    Text(
                        text = stringResource(R.string.title_translate),
                        fontWeight = FontWeight.Bold,
                        fontSize = AppTextSizes.TextSizeTitle,
                        textAlign = TextAlign.Center,
                        color = AppColors.TextMain
                    )
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(AppColors.AppBackground),
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
                        onPlayAudio = { onEvent(TranslateEvent.OnPlaySourceAudio) },
                        onTranslate = { keyboardController?.hide() },
                        onImageClick = { onEvent(TranslateEvent.OnImageClick) },
                        onCameraClick = {
                            val isCameraGranted = ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.CAMERA
                            ) == PackageManager.PERMISSION_GRANTED

                            if (isCameraGranted) {
                                onEvent(TranslateEvent.OnCameraClick)
                            } else {
                                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                            }
                        },
                        onMicClick = {
                            if (hasMicPermission) {
                                onEvent(TranslateEvent.OnMicClick)
                            } else {
                                micPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                            }
                        }
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
                            isTranslating = state.isTranslating,
                            onCopyClick = {
                                clipboardManager.setText(AnnotatedString(state.targetText))
                                onEvent(TranslateEvent.OnCopySuccess)
                            },
                            onPlayAudio = { onEvent(TranslateEvent.OnPlayTargetAudio) }
                        )
                        Spacer(modifier = Modifier.height(Spaces.Space7))
                    }
                }
            }

            val hasHistoryData = !state.historyState.data.isNullOrEmpty()
            if (hasHistoryData) {
                item {
                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = Spaces.Space16, vertical = Spaces.Space4),
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
                                    color = AppColors.TitleColor
                                )
                            }
                            TextButton(
                                onClick = { onEvent(TranslateEvent.OnClearAllClick) }
                            ) {
                                Text(
                                    text = stringResource(R.string.label_clear_all),
                                    color = AppColors.BluePrimary,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

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
                                    historyList?.forEach { item ->
                                        key(item.id) {
                                            AnimatedHistoryItem(
                                                source = item.sourceText,
                                                target = item.targetText,
                                                onClick = {
                                                    keyboardController?.hide()
                                                    focusManager.clearFocus()
                                                    onEvent(
                                                        TranslateEvent.OnHistoryItemClick(
                                                            source = item.sourceText,
                                                            target = item.targetText
                                                        )
                                                    )
                                                }
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
}