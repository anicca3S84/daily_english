package com.codework.dailyenglish.presentation.screen.lessons.lesson.components.vocab

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.codework.dailyenglish.R
import com.codework.dailyenglish.domain.lesson.model.StepData
import com.codework.dailyenglish.presentation.theme.AppColors
import com.codework.dailyenglish.presentation.theme.AppIcons
import com.codework.dailyenglish.presentation.theme.AppTextSizes
import com.codework.dailyenglish.presentation.theme.Spaces

@Composable
fun VocabCardFront(
    data: StepData.Vocab,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onPlayAudio: (String, Float) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Spaces.Space20),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = Spaces.Space10),
            horizontalArrangement = Arrangement.Absolute.Right,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { onToggleFavorite() },
                modifier = Modifier.size(AppIcons.IconSizeLarge)
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Filled.Star else Icons.Outlined.StarBorder,
                    contentDescription = "",
                    tint = if (isFavorite) Color(0xFFFFC107) else AppColors.Slate300,
                    modifier = Modifier.size(Spaces.Space28)
                )
            }
        }

        Spacer(modifier = Modifier.height(Spaces.Space16))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(Spaces.Space300)
                .padding(horizontal = Spaces.Space18)
                .clip(RoundedCornerShape(Spaces.Space20))
                .background(AppColors.Slate50),
            contentAlignment = Alignment.Center
        ) {
            if (data.imageUrl.isNullOrEmpty()) {
                Icon(
                    imageVector = Icons.Default.Image,
                    contentDescription = null,
                    tint = AppColors.Slate100,
                    modifier = Modifier.size(AppIcons.IconSizeExtraLarge)
                )
            } else {
                // TODO: Sử dụng AsyncImage ở đây
                Box(modifier = Modifier.fillMaxSize().background(AppColors.Indigo50))
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(Spaces.Space32))
            Text(
                text = data.word,
                fontSize = AppTextSizes.TextSize32,
                fontWeight = FontWeight.W500,
                color = Color.Black,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(Spaces.Space10))

            if (!data.usPhonetic.isNullOrEmpty()) {
                Text(
                    text = data.usPhonetic,
                    fontSize = AppTextSizes.TextSizeTitle,
                    color = AppColors.Slate500,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(Spaces.Space20))
        }

        Row(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.TouchApp,
                contentDescription = null,
                tint = AppColors.Slate400,
                modifier = Modifier.size(Spaces.Space14)
            )

            Spacer(modifier = Modifier.width(Spaces.Space6))

            Text(
                text = stringResource(R.string.label_tap_to_flip),
                fontSize = AppTextSizes.TextSizeLabel,
                color = AppColors.Slate400
            )
        }
    }
}