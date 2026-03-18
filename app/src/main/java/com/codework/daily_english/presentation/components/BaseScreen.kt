package com.codework.daily_english.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.codework.daily_english.presentation.theme.AppBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> BaseScreen(
    state: UiState<T>,
    snackbarHostState: SnackbarHostState? = null,
    onRetry: (() -> Unit)? = null,
    loadingType: LoadingType = LoadingType.SHIMMER,
    shimmerContent: @Composable () -> Unit = { DefaultShimmer() },
    content: @Composable (T) -> Unit
) {
    // Trạng thái cho Pull-to-refresh
    val isRefreshing = state is UiState.Loading && loadingType == LoadingType.OVERLAY

    Box(modifier = Modifier
        .fillMaxSize()
        .background(AppBackground)
    ) {
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = { onRetry?.invoke() },
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            when (state) {
                is UiState.Loading -> {
                    if (loadingType == LoadingType.SHIMMER) {
                        shimmerContent()
                    } else if (!isRefreshing) {
                        // Chỉ hiện Indicator nếu không phải đang dùng kéo-để-làm-mới
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                }

                is UiState.Success -> {
                    content(state.data)
                }

                is UiState.Error -> {
                    // Logic phân loại lỗi để hiện UI sinh động
                    val errorType = remember(state) {
                        when {
                            state.message.contains("network", ignoreCase = true) ||
                                    state.message.contains("connect", ignoreCase = true) -> ErrorVisual.NO_INTERNET
                            state.code == 404 -> ErrorVisual.NOT_FOUND
                            else -> ErrorVisual.SERVER_ERROR
                        }
                    }

                    ErrorOverlay(
                        errorType = errorType,
                        message = state.message,
                        onRetry = onRetry
                    )

                    if (snackbarHostState != null) {
                        LaunchedEffect(state.message) {
                            snackbarHostState.showSnackbar(state.message)
                        }
                    }
                }
            }
        }
    }
}

// Enum để định nghĩa loại lỗi và hình ảnh tương ứng
enum class ErrorVisual(val icon: String, val title: String) {
    NO_INTERNET("📶", "No Connection"),
    SERVER_ERROR("⚙️", "Server Error"),
    NOT_FOUND("🔍", "Not Found")
}