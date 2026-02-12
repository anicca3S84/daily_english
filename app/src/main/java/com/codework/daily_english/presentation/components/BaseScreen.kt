package com.codework.daily_english.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.codework.daily_english.presentation.theme.AppBackground

@Composable
fun <T> BaseScreen(
    state: UiState<T>,
    snackbarHostState: SnackbarHostState? = null, // Để hiển thị Snackbar nếu cần
    onRetry: (() -> Unit)? = null,
    loadingType: LoadingType = LoadingType.SHIMMER,
    shimmerContent: @Composable () -> Unit = { DefaultShimmer() },
    content: @Composable (T) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()
        .background(AppBackground)) {
        // Sử dụng 'when' để kiểm tra từng trạng thái của Sealed Class
        when (state) {
            is UiState.Loading -> {
                if (loadingType == LoadingType.SHIMMER) {
                    shimmerContent()
                } else {
                    // Nếu là OVERLAY, hiển thị vòng tròn xoay giữa màn hình trống
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }

            is UiState.Success -> {
                // Khi thành công, 'state.data' sẽ khả dụng nhờ Smart Cast
                content(state.data)
            }

            is UiState.Error -> {
                // Hiển thị màn hình lỗi toàn màn hình
                ErrorOverlay(
                    message = state.message, // Trong Sealed Class, field tên là 'message'
                    onRetry = onRetry
                )

                // Nếu muốn bắn Snackbar kèm theo màn hình lỗi
                if (snackbarHostState != null) {
                    LaunchedEffect(state.message) {
                        snackbarHostState.showSnackbar(
                            message = state.message,
                            withDismissAction = true
                        )
                    }
                }
            }
        }
    }
}