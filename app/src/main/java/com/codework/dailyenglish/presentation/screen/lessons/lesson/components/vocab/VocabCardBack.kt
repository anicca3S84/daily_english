package com.codework.dailyenglish.presentation.screen.lessons.lesson.components.vocab

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.FormatQuote
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codework.dailyenglish.R
import com.codework.dailyenglish.domain.lesson.model.StepData
import com.codework.dailyenglish.presentation.screen.lessons.lesson.components.HighlightTargetWord
import com.codework.dailyenglish.presentation.theme.AppColors
import com.codework.dailyenglish.presentation.theme.AppTextSizes
import com.codework.dailyenglish.presentation.theme.Spaces

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun VocabCardBack(
    data: StepData.Vocab,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 20.dp)
                .background(Color(0xFF84D8FF), RoundedCornerShape(32.dp))
        )

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 8.dp),
            shape = RoundedCornerShape(32.dp),
            color = Color(0xFFDDF4FF),
            border = BorderStroke(2.dp, Color(0xFF84D8FF))
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(Spaces.Space24),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Text(
                        text = data.word,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF1CB0F6),
                        modifier = Modifier.padding(top = Spaces.Space16, bottom = Spaces.Space8),
                        textAlign = TextAlign.Center
                    )

                    if (!data.usPhonetic.isNullOrEmpty()) {
                        Text(
                            text = data.usPhonetic,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1CB0F6).copy(alpha = 0.7f),
                            modifier = Modifier.padding(bottom = Spaces.Space24)
                        )
                    }

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = Spaces.Space16),
                        shape = RoundedCornerShape(Spaces.Space16),
                        color = Color.White,
                        border = BorderStroke(2.dp, Color(0xFF84D8FF))
                    ) {
                        Text(
                            text = data.meaning,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF4B4B4B),
                            modifier = Modifier.padding(Spaces.Space16),
                            textAlign = TextAlign.Center
                        )
                    }

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = Spaces.Space16),
                        shape = RoundedCornerShape(Spaces.Space16),
                        color = Color.White,
                        border = BorderStroke(2.dp, Color(0xFF84D8FF))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(Spaces.Space16),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "\"${data.examples}\"",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF4B4B4B),
                                fontStyle = FontStyle.Italic,
                                modifier = Modifier.padding(bottom = Spaces.Space8),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}