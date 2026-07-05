package com.codework.dailyenglish.presentation.screen.lessons.lesson.components.question.components

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun MicRippleAnimation(micSize: Dp = 76.dp) {
    val infiniteTransition = rememberInfiniteTransition(label = "Ripple")

    val ripple1 = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ripple1"
    )
    val ripple2 = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, delayMillis = 500, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ripple2"
    )
    val ripple3 = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, delayMillis = 1000, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ripple3"
    )

    val waveColor = Color(0xFF3B82F6)

    Box(modifier = Modifier.size(micSize * 2f), contentAlignment = Alignment.Center) {
        // Vòng sóng 1
        Box(
            modifier = Modifier
                .size(micSize)
                .scale(1f + ripple1.value * 1.3f)
                .alpha((1f - ripple1.value).coerceIn(0f, 1f))
                .background(waveColor.copy(alpha = 0.15f), CircleShape)
        )
        // Vòng sóng 2
        Box(
            modifier = Modifier
                .size(micSize)
                .scale(1f + ripple2.value * 1.3f)
                .alpha((1f - ripple2.value).coerceIn(0f, 1f))
                .background(waveColor.copy(alpha = 0.1f), CircleShape)
        )
        // Vòng sóng 3
        Box(
            modifier = Modifier
                .size(micSize)
                .scale(1f + ripple3.value * 1.3f)
                .alpha((1f - ripple3.value).coerceIn(0f, 1f))
                .background(waveColor.copy(alpha = 0.05f), CircleShape)
        )
    }
}