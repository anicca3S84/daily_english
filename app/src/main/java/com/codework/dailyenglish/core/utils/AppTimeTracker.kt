package com.codework.dailyenglish.core.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

@Composable
fun AppTimeTracker(
    onSaveSessionTime: (Int) -> Unit
) {
    // Biến lưu trữ thời gian tạm thời trong bộ nhớ RAM
    var accumulatedSeconds by remember { mutableStateOf(0) }

    // 1. Chạy hiệu ứng đếm giây
    LaunchedEffect(Unit) {
        while (isActive) {
            delay(1000L) // Đếm mỗi 1 giây
            accumulatedSeconds++
        }
    }

    // 2. TỰ ĐỘNG LƯU ĐỊNH KỲ (Ví dụ: Cứ sau mỗi 60 giây thì lưu xuống Local một lần)
    LaunchedEffect(Unit) {
        while (isActive) {
            delay(60000L) // 60 giây (bạn có thể đổi thành 180000L cho 3 phút)
            if (accumulatedSeconds > 0) {
                onSaveSessionTime(accumulatedSeconds)
                accumulatedSeconds = 0 // Reset lại bộ đếm RAM sau khi đã ghi xuống Local
            }
        }
    }

    // 3. LƯU LẦN CUỐI KHI DISPOSE (Tắt app, Logout)
    DisposableEffect(Unit) {
        onDispose {
            if (accumulatedSeconds > 0) {
                onSaveSessionTime(accumulatedSeconds)
            }
        }
    }
}