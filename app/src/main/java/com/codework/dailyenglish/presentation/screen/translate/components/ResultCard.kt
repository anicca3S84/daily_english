package com.codework.dailyenglish.presentation.screen.translate.components

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.codework.dailyenglish.R
import com.codework.dailyenglish.presentation.components.AppCircleButton
import androidx.compose.ui.platform.LocalClipboardManager
import com.codework.dailyenglish.presentation.theme.AppColors
import com.codework.dailyenglish.presentation.theme.Spaces
import com.codework.dailyenglish.presentation.theme.AppTextSizes

@Composable
fun ResultCard(
    text: String,
    isTranslating: Boolean,
    onCopyClick: () -> Unit,  // Thêm mới callback sao chép kết quả
    onPlayAudio: () -> Unit   // Thêm mới callback phát âm thanh đích
) {
    Card(
        shape = RoundedCornerShape(Spaces.Space24),
        elevation = CardDefaults.cardElevation(defaultElevation = Spaces.Space4),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(Spaces.Space1, AppColors.BorderStrokeColor),
        modifier = Modifier
            .fillMaxWidth()
            .height(Spaces.Space200)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Spaces.Space20)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    if (isTranslating) {
                        Column {
                            CircularProgressIndicator(
                                modifier = Modifier.size(Spaces.Space30),
                                color = AppColors.BluePrimary
                            )
                            Spacer(modifier = Modifier.height(Spaces.Space8))
                            Text(
                                text = stringResource(R.string.label_translating),
                                color = Color.Gray
                            )
                        }
                    } else {
                        Text(
                            text = text,
                            style = TextStyle(
                                fontSize = AppTextSizes.TextSizeHeader,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF1F2937),
                                lineHeight = AppTextSizes.TextSize32
                            )
                        )
                    }
                }

                if (text.isNotEmpty() && !isTranslating) {
                    Spacer(modifier = Modifier.width(Spaces.Space16))
                    // Kích hoạt callback truyền chuỗi ra màn hình chính xử lý
                    AppCircleButton(
                        icon = Icons.Rounded.ContentCopy,
                        onClick = onCopyClick
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            if (text.isNotEmpty() && !isTranslating) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AppCircleButton(
                        icon = Icons.AutoMirrored.Filled.VolumeUp,
                        onClick = onPlayAudio,
                    )
                }
            }
        }
    }
}