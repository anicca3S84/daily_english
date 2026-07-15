package com.codework.dailyenglish.presentation.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.codework.dailyenglish.core.base.BaseUiState
import com.codework.dailyenglish.presentation.theme.AppColors

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
                WrapperInnerContent(
                    uiState = uiState,
                    errorString = errorString,
                    isDataEmpty = isDataEmpty,
                    isLoading = isLoading,
                    loadingType = loadingType,
                    shimmerContent = shimmerContent,
                    snackbarHostState = snackbarHostState,
                    onRetry = onRetry,
                    content = content
                )
            }
        } else {
            WrapperInnerContent(
                uiState = uiState,
                errorString = errorString,
                isDataEmpty = isDataEmpty,
                isLoading = isLoading,
                loadingType = loadingType,
                shimmerContent = shimmerContent,
                snackbarHostState = snackbarHostState,
                onRetry = onRetry,
                content = content
            )
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
    val context = LocalContext.current

    // 1. Hiển thị nội dung chính khi có dữ liệu
    if (!(isLoading && isDataEmpty)) {
        content(uiState.data)
    }

    // 2. Màn hình Loading khi chưa có dữ liệu (Đã sửa lỗi căn giữa)
    if (isLoading && isDataEmpty) {
        Box(
            modifier = Modifier
                .matchParentSize() // Khớp trực tiếp với kích thước của Box ngoài cùng (vốn có minHeight = 200.dp)
                .zIndex(10f)
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(36.dp),
                color = AppColors.BluePrimary,
                strokeWidth = 3.dp
            )
        }
    }

    // 3. Xử lý hiển thị lỗi
    errorString?.let { message ->
        LaunchedEffect(message) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }

        if (isDataEmpty) {
            Box(
                modifier = Modifier
                    .matchParentSize() // Khớp trực tiếp kích thước giúp căn giữa nút "Try again"
                    .zIndex(10f)
                    .background(Color(0xFFF8FAFC)),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = { onRetry?.invoke() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AppColors.BluePrimary
                    ),
                    shape = RoundedCornerShape(50)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Retry Icon",
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "Try again",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        } else {
            if (snackbarHostState != null) {
                LaunchedEffect(message) {
                    snackbarHostState.showSnackbar(message = message)
                }
            }
        }
    }
}