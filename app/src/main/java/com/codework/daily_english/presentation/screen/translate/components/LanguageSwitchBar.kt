package com.codework.daily_english.presentation.screen.translate.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.CompareArrows
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.codework.daily_english.presentation.theme.BluePrimary
import com.codework.daily_english.presentation.theme.Spaces

@Composable
fun LanguageSwitchBar(
    sourceLang: String,
    targetLang: String,
    onSwap: () -> Unit,
    onSourceClick: () -> Unit = {},
    onTargetClick: () -> Unit = {}
) {
    Surface(
        shape = RoundedCornerShape(Spaces.Space16),
        color = Color.White,
        shadowElevation = Spaces.Space2,
        modifier = Modifier.height(Spaces.Space60)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = Spaces.Space8),
            verticalAlignment = Alignment.CenterVertically
        ) {
            LanguageChip(
                text = sourceLang,
                modifier = Modifier.weight(1f),
                isTarget = false,
                onClick = onSourceClick
            )

            Box(
                modifier = Modifier.width(Spaces.Space48),
                contentAlignment = Alignment.Center
            ) {
                IconButton(onClick = onSwap) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.CompareArrows,
                        contentDescription = null,
                        tint = BluePrimary,
                        modifier = Modifier.size(Spaces.Space28)
                    )
                }
            }

            LanguageChip(
                text = targetLang,
                modifier = Modifier.weight(1f),
                isTarget = true,
                onClick = onTargetClick
            )
        }
    }
}