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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.codework.daily_english.presentation.theme.AppBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseScreen(
    isLoading: Boolean,
    error: String? = null,
    // THÊM BIẾN NÀY: Để BaseScreen biết trên màn hình đã có dữ liệu chưa
    isDataEmpty: Boolean = false,
    snackbarHostState: SnackbarHostState? = null,
    onRetry: (() -> Unit)? = null,
    loadingType: LoadingType = LoadingType.SHIMMER,
    shimmerContent: @Composable () -> Unit = { /* DefaultShimmer() */ },
    // KHÔNG CẦN TRUYỀN T NỮA. Content bây giờ chỉ là một khối UI.
    content: @Composable () -> Unit
) {
    // Kéo-để-làm-mới CHỈ kích hoạt khi đang loading VÀ màn hình ĐÃ CÓ dữ liệu
    val isRefreshing = isLoading && !isDataEmpty

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

            // 1. LUÔN LUÔN RENDER CONTENT (Dữ liệu cũ vẫn nằm đó không bị mất)
            content()

            // 2. XỬ LÝ LOADING
            if (isLoading && isDataEmpty) {
                // Nếu chưa có data gì cả -> Che toàn màn hình bằng Shimmer hoặc Spinner
                if (loadingType == LoadingType.SHIMMER) {
                    shimmerContent()
                } else {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
            }

            // 3. XỬ LÝ ERROR
            if (error != null) {
                if (isDataEmpty) {
                    // Nếu màn hình chả có gì mà bị lỗi -> Hiện bảng báo lỗi chà bá ở giữa màn hình
                    val errorType = if (error.contains("mạng", ignoreCase = true) || error.contains("network", ignoreCase = true)) {
                        ErrorVisual.NO_INTERNET
                    } else {
                        ErrorVisual.SERVER_ERROR
                    }

                    ErrorOverlay(
                        errorType = errorType,
                        message = error,
                        onRetry = onRetry
                    )
                } else {
                    // CỰC XỊN: Nếu đã có dữ liệu cũ mà bấm Load More bị rớt mạng
                    // -> KHÔNG che dữ liệu của user đi, chỉ bắn Snackbar lên báo lỗi!
                    if (snackbarHostState != null) {
                        LaunchedEffect(error) {
                            snackbarHostState.showSnackbar(message = error)
                        }
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