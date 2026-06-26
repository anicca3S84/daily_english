package com.codework.dailyenglish.presentation.screen.ai.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codework.dailyenglish.presentation.theme.AppColors
import kotlinx.coroutines.delay

@Composable
fun SuggestionRowWithAutoScroll(
    items: List<String>,
    reverseDirection: Boolean = false,
    onClick: (String) -> Unit
) {
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = Int.MAX_VALUE / 2)

    LaunchedEffect(Unit) {
        while (true) {
            listState.scrollBy(if (reverseDirection) -1.5f else 1.5f)
            delay(10)
        }
    }

    LazyRow(
        state = listState,
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        userScrollEnabled = true,
        // BỎ reverseLayout = reverseLayout ở đây
    ) {
        items(count = Int.MAX_VALUE) { index ->
            // Sử dụng % items.size để vòng lặp nội dung vô tận
            val item = items[index % items.size]
            Surface(
                onClick = { onClick(item) },
                shape = CircleShape,
                color = Color.White,
                border = BorderStroke(1.dp, AppColors.BluePrimary.copy(alpha = 0.5f))
            ) {
                Text(
                    text = item,
                    modifier = Modifier.padding(horizontal = 18.dp, vertical = 12.dp),
                    fontWeight = FontWeight.Medium,
                    style = TextStyle(brush = AppColors.MainGradient, fontSize = 13.sp),
                    maxLines = 1
                )
            }
        }
    }
}