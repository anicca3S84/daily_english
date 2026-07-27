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
    var accumulatedSeconds by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        while (isActive) {
            delay(1000L)
            accumulatedSeconds++
        }
    }

    LaunchedEffect(Unit) {
        while (isActive) {
            delay(60000L)
            if (accumulatedSeconds > 0) {
                onSaveSessionTime(accumulatedSeconds)
                accumulatedSeconds = 0
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            if (accumulatedSeconds > 0) {
                onSaveSessionTime(accumulatedSeconds)
            }
        }
    }
}