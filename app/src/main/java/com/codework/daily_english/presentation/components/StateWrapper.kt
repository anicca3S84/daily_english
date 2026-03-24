package com.codework.daily_english.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.codework.daily_english.core.base.BaseUiState
import com.codework.daily_english.presentation.theme.AppBackground
import com.codework.daily_english.presentation.theme.BluePrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> StateWrapper(
    uiState: BaseUiState<T>,
    modifier: Modifier = Modifier, // Modifier này cực quan trọng để định vị
    snackbarHostState: SnackbarHostState? = null,
    onRetry: (() -> Unit)? = null,
    enablePullToRefresh: Boolean = false, // Mặc định tắt, chỉ bật ở màn chính
    loadingType: LoadingType = LoadingType.SHIMMER,
    shimmerContent: @Composable () -> Unit = {},
    content: @Composable (T?) -> Unit
) {
    val errorString = uiState.error?.asString()
    val isDataEmpty = uiState.isEmpty
    val isLoading = uiState.isLoading

    // Box ngoài cùng dùng modifier truyền vào (không ép fillMaxSize)
    Box(modifier = modifier) {

        // Chỉ bọc PullToRefresh nếu cần thiết
        if (enablePullToRefresh) {
            PullToRefreshBox(
                isRefreshing = uiState.isRefreshing,
                onRefresh = { onRetry?.invoke() },
                modifier = Modifier.fillMaxWidth(), // Chỉ chiếm ngang
                contentAlignment = Alignment.TopCenter
            ) {
                WrapperInnerContent(uiState, errorString, isDataEmpty, isLoading, loadingType, shimmerContent, snackbarHostState, onRetry, content)
            }
        } else {
            // Nếu không refresh thì render trực tiếp cho nhẹ
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
    Box (
        modifier = Modifier.matchParentSize()
    ){
        // 1. Content chính
        content(uiState.data)

        // 2. Loading Overlay
        if (isLoading && isDataEmpty) {
            Box(modifier = Modifier.matchParentSize()) {
                if (loadingType == LoadingType.SHIMMER) {
                    shimmerContent()
                } else {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = BluePrimary
                    )
                }
            }
        }

        // 3. Error Overlay (ĐÃ FIX Ở ĐÂY)
        errorString?.let { message ->
            if (isDataEmpty) {
                // Xác định errorType dựa trên nội dung message
                val visualType = when {
                    message.contains("mạng", true) || message.contains("connection", true) -> ErrorVisual.NO_INTERNET
                    message.contains("không tìm thấy", true) || message.contains("not found", true) -> ErrorVisual.NOT_FOUND
                    else -> ErrorVisual.SERVER_ERROR
                }

                ErrorOverlay(
                    errorType = visualType, // TRUYỀN THAM SỐ CÒN THIẾU
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