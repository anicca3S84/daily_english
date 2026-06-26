package com.codework.dailyenglish.core.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner

@Composable
fun AppTimeTracker(
    onSaveSessionTime: (secondsTotal: Int) -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    // Dùng rememberSaveable để sống sót qua xoay màn hình (Configuration Changes)
    var sessionStartTime by rememberSaveable { mutableLongStateOf(0L) }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> {
                    sessionStartTime = System.currentTimeMillis()
                }
                Lifecycle.Event.ON_STOP -> {
                    if (sessionStartTime > 0L) {
                        val sessionEndTime = System.currentTimeMillis()
                        val durationInMillis = sessionEndTime - sessionStartTime

                        // Tính theo GIÂY để không làm mất dữ liệu lẻ
                        val secondsSpent = (durationInMillis / 1000).toInt()

                        if (secondsSpent > 0) {
                            onSaveSessionTime(secondsSpent)
                        }
                        sessionStartTime = 0L
                    }
                }
                else -> {}
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}