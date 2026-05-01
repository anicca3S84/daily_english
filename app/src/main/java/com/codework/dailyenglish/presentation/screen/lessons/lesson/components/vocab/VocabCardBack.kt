package com.codework.dailyenglish.presentation.screen.lessons.lesson.components.vocab

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.codework.dailyenglish.domain.lesson.model.StepData
import com.codework.dailyenglish.presentation.theme.AppColors
import com.codework.dailyenglish.presentation.theme.AppIcons
import com.codework.dailyenglish.presentation.theme.AppRadius
import com.codework.dailyenglish.presentation.theme.AppTextSizes
import com.codework.dailyenglish.presentation.theme.Spaces
import com.codework.dailyenglish.R
import com.codework.dailyenglish.presentation.screen.lessons.lesson.components.HighlightTargetWord

@Composable
fun VocabCardBack(
    data: StepData.Vocab,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = Spaces.Space20, vertical = Spaces.Space16)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(
                onClick = onToggleFavorite,
                modifier = Modifier.size(AppIcons.IconSizeLarge)
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Filled.Star else Icons.Outlined.StarBorder,
                    contentDescription = null,
                    tint = if (isFavorite) Color(0xFFFFC107) else AppColors.Slate300
                )
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // WORD
            Text(
                text = data.word,
                fontSize = AppTextSizes.TextSize32,
                fontWeight = FontWeight.ExtraBold,
                color = AppColors.Indigo600
            )

            Spacer(modifier = Modifier.height(Spaces.Space6))

            if (!data.usPhonetic.isNullOrEmpty()) {
                Text(
                    text = data.usPhonetic!!,
                    fontSize = AppTextSizes.TextSizeBody,
                    color = AppColors.Slate400
                )
            }

            Spacer(modifier = Modifier.height(Spaces.Space24))

            Surface(
                color = AppColors.Indigo600.copy(alpha = 0.08f),
                shape = RoundedCornerShape(AppRadius.RadiusLarge)
            ) {
                Text(
                    text = data.meaning,
                    fontSize = AppTextSizes.TextSize20,
                    fontWeight = FontWeight.SemiBold,
                    color = AppColors.Slate800,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(Spaces.Space16)
                )
            }

            Spacer(modifier = Modifier.height(Spaces.Space24))

            if (!data.examples.isNullOrEmpty()) {
                val example = data.examples.first()

                Text(
                    text = HighlightTargetWord(example, data.word),
                    fontSize = AppTextSizes.TextSizeMain,
                    color = AppColors.Slate700,
                    textAlign = TextAlign.Center,
                    lineHeight = Spaces.Space26.value.sp,
                    modifier = Modifier.padding(horizontal = Spaces.Space12)
                )

                Spacer(modifier = Modifier.height(Spaces.Space10))

                Text(
                    text = "",
                    fontSize = AppTextSizes.TextSizeBody,
                    color = AppColors.Slate500,
                    textAlign = TextAlign.Center
                )
            }
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