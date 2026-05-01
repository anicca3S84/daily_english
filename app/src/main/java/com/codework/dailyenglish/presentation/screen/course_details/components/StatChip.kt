package com.codework.dailyenglish.presentation.screen.course_details.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.codework.dailyenglish.presentation.theme.AppColors
import com.codework.dailyenglish.presentation.theme.AppIcons
import com.codework.dailyenglish.presentation.theme.AppRadius
import com.codework.dailyenglish.presentation.theme.Spaces
import com.codework.dailyenglish.presentation.theme.AppTextSizes

@Composable
fun StatChip(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    iconColor: Color,
    title: String,
    value: String
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(AppRadius.RadiusLarge),
        color = Color.White,
        border = BorderStroke(Spaces.Space1, AppColors.GrayBorder),
        shadowElevation = Spaces.Space1
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(vertical = Spaces.Space12, horizontal = Spaces.Space4),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(AppIcons.IconSizeMedium)
            )

            Spacer(modifier = Modifier.height(Spaces.Space2))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Spaces.Space0)
            ) {
                Text(
                    text = title,
                    fontSize = AppTextSizes.TextSizeSmall,
                    color = AppColors.TextSub,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    lineHeight = AppTextSizes.TextSizeLabel,
                    modifier = Modifier.padding(Spaces.Space4)
                )


                Text(
                    text = value,
                    fontSize = AppTextSizes.TextSizeLabel,
                    color = AppColors.TextMain,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    lineHeight = AppTextSizes.TextSizeMain,
                    modifier = Modifier.padding(Spaces.Space0)
                )
            }
        }
    }
}