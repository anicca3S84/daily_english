package com.codework.dailyenglish.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.zIndex
import com.codework.dailyenglish.core.base.BaseUiState
import com.codework.dailyenglish.presentation.theme.BluePrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> StateWrapper(
    uiState: BaseUiState<T>,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState? = null,
    onRetry: (() -> Unit)? = null,
    enablePullToRefresh: Boolean = false,
    loadingType: LoadingType = LoadingType.SHIMMER,
    shimmerContent: @Composable () -> Unit = {},
    content: @Composable (T?) -> Unit
) {
    val errorString = uiState.error?.asString()
    val isDataEmpty = uiState.isEmpty
    val isLoading = uiState.isLoading

    Box(modifier = modifier) {

        if (enablePullToRefresh) {
            PullToRefreshBox(
                isRefreshing = uiState.isRefreshing,
                onRefresh = { onRetry?.invoke() },
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.TopCenter
            ) {
                WrapperInnerContent(uiState, errorString, isDataEmpty, isLoading, loadingType, shimmerContent, snackbarHostState, onRetry, content)
            }
        }else {
            WrapperInnerContent(uiState, errorString, isDataEmpty, isLoading, loadingType, shimmerContent, snackbarHostState, onRetry, content)
        }
    }
}

@Composable
private fun <T> BoxScope.WrapperInnerContent(
    uiState: BaseUiState<T>,
    errorString: String?,
    isDataEmpty: Boolean,
    isLoading: Boolean,
    loadingType: LoadingType,
    shimmerContent: @Composable () -> Unit,
    snackbarHostState: SnackbarHostState?,
    onRetry: (() -> Unit)?,
    content: @Composable (T?) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {

        if (!(isLoading && isDataEmpty)) {
            content(uiState.data)
        }

        if (isLoading && isDataEmpty) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .zIndex(10f)
                    .background(Color.White)
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = BluePrimary
                )
            }
        }

        errorString?.let { message ->
            if (isDataEmpty) {
                val visualType = when {
                    message.contains("mạng", true) || message.contains("connection", true) -> ErrorVisual.NO_INTERNET
                    message.contains("không tìm thấy", true) || message.contains("not found", true) -> ErrorVisual.NOT_FOUND
                    else -> ErrorVisual.SERVER_ERROR
                }

                ErrorOverlay(
                    errorType = visualType,
                    message = message,
                    onRetry = onRetry,
                )
            } else {
                if (snackbarHostState != null) {
                    LaunchedEffect(message) {
                        snackbarHostState.showSnackbar(message = message)
                    }
                }
            }
        }
    }
}

enum class ErrorVisual(val icon: String, val title: String) {
    NO_INTERNET("📶", "No Connection"),
    SERVER_ERROR("⚙️", "Server Error"),
    NOT_FOUND("🔍", "Not Found")
}