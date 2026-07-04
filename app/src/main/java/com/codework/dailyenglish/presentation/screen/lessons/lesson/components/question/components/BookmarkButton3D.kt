package com.codework.dailyenglish.presentation.screen.lessons.lesson.components.question.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun BookmarkButton3D(isSaved: Boolean, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val yOffset by animateDpAsState(targetValue = if (isPressed) 4.dp else 0.dp, label = "yOffset")

    val bgColor by animateColorAsState(targetValue = if (isSaved) Color(0xFFFEF3C7) else Color.White, label = "bg")
    val borderColor by animateColorAsState(targetValue = if (isSaved) Color(0xFFF4B844) else Color(0xFFE5E5E5), label = "border")
    val iconColor by animateColorAsState(targetValue = if (isSaved) Color(0xFFF4B844) else Color(0xFF9CA3AF), label = "icon")

    Box(
        modifier = Modifier
            .size(64.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .offset(y = 4.dp)
                .background(borderColor, CircleShape)
        )
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .offset(y = yOffset),
            shape = CircleShape,
            color = bgColor,
            border = BorderStroke(2.dp, borderColor)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = if (isSaved) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                    contentDescription = "Lưu từ",
                    tint = iconColor,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}