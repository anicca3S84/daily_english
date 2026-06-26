package com.codework.dailyenglish.presentation.screen.ai.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AutoScrollingSuggestionSection(suggestions: List<String>, onClick: (String) -> Unit) {
    // Vẫn giữ logic tạo loopList để cuộn mượt
    val loopList = if (suggestions.size < 10) suggestions + suggestions + suggestions else suggestions
    val mid = loopList.size / 2
    val firstRow = loopList.take(mid)
    val secondRow = loopList.drop(mid)

    Column(
        modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Hàng 1: Cuộn xuôi (reverseDirection = false)
        SuggestionRowWithAutoScroll(
            items = firstRow,
            reverseDirection = false,
            onClick = onClick
        )

        // Hàng 2: Cuộn ngược (reverseDirection = true)
        SuggestionRowWithAutoScroll(
            items = secondRow,
            reverseDirection = true,
            onClick = onClick
        )
    }
}