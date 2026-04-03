package com.codework.daily_english.presentation.screen.translate.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.codework.daily_english.presentation.theme.Spaces
@Composable
fun AnimatedHistoryItem(
    source: String,
    target: String,
    onClick: () -> Unit
) {
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isVisible = true
    }



    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(animationSpec = tween(500)) +
                expandVertically(animationSpec = tween(500))
    ){
        Box(
            modifier = Modifier
                .padding(bottom = Spaces.Space2)
        ) {
            HistoryItem(
                source = source,
                target = target,
                onClick = onClick
            )
        }
    }
}