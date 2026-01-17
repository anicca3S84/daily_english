package com.codework.daily_english.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun <T> BaseScreen(
    state: UiState<T>,
    // 1. THÊM THAM SỐ NÀY VÀO ĐÂY
    snackbarHostState: SnackbarHostState? = null,
    onRetry: (() -> Unit)? = null,
    loadingType: LoadingType = LoadingType.SHIMMER,
    shimmerContent: @Composable () -> Unit = { DefaultShimmer() },
    content: @Composable (T) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {

        // 1. Hiển thị nội dung chính hoặc Shimmer/Error toàn màn hình
        if (state.data != null) {
            content(state.data)
        } else {
            if (state.isLoading && loadingType == LoadingType.SHIMMER) {
                shimmerContent()
            } else if (state.error != null) {
                ErrorOverlay(message = state.error, onRetry = onRetry)
            }
        }

        // 2. Hiển thị Loading Overlay (xoay tròn) khi cần
        val shouldShowOverlayLoading = state.isLoading && (
                loadingType == LoadingType.OVERLAY || (loadingType == LoadingType.SHIMMER && state.data != null)
                )

        if (shouldShowOverlayLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }

        // 3. XỬ LÝ TODO: Có Data nhưng bị lỗi (Ví dụ: Refresh thất bại)
        // Logic: Khi có error mới xuất hiện, bắn Snackbar thông báo
        if (state.data != null && state.error != null) {
            LaunchedEffect(state.error) {
                snackbarHostState?.showSnackbar(
                    message = state.error,
                    withDismissAction = true
                )
            }
        }
    }
}