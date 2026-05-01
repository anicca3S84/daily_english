package com.codework.dailyenglish.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.codework.dailyenglish.presentation.theme.AppColors
import com.codework.dailyenglish.presentation.theme.AppIcons
import com.codework.dailyenglish.presentation.theme.AppTextSizes
import com.codework.dailyenglish.presentation.theme.Spaces


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppProgressTopBar(
    progress: Float,
    progressText: String,
    onBackClick: () -> Unit,
    onCloseClick: (() -> Unit)? = null
) {
    Surface(
        color = AppColors.AppBackground,
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = Spaces.Space0
    ) {
        Row(
            modifier = Modifier
                .statusBarsPadding()
                .fillMaxWidth()
                .padding(horizontal = Spaces.Space16, vertical = Spaces.Space12),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                onClick = onBackClick,
                shape = CircleShape,
                color = Color.White,
                border = BorderStroke(Spaces.Space1, Color(0xFFE5E7EB)),
                modifier = Modifier.size(Spaces.Space40)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color(0xFF374151),
                        modifier = Modifier.size(AppIcons.IconSizeSmall)
                    )
                }
            }

            Spacer(modifier = Modifier.width(Spaces.Space12))

            AppProgressBar(
                progress = progress,
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(Spaces.Space12))

            if (onCloseClick != null) {
                IconButton(onClick = onCloseClick, modifier = Modifier.size(AppIcons.IconSizeMedium)) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null,
                        tint = AppColors.GrayText
                    )
                }
            } else {
                Text(
                    text = progressText,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = AppTextSizes.TextSizeBody
                    ),
                    color = AppColors.GrayText,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}