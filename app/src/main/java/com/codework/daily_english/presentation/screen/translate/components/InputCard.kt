package com.codework.daily_english.presentation.screen.translate.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.codework.daily_english.R
import com.codework.daily_english.presentation.components.StandardCircleButton
import com.codework.daily_english.presentation.theme.BluePrimary
import com.codework.daily_english.presentation.theme.BorderStrokeColor
import com.codework.daily_english.presentation.theme.Spaces
import com.codework.daily_english.presentation.theme.TextSizes

@Composable
fun InputCard(
    text: String,
    hint: String,
    onValueChange: (String) -> Unit,
    onClear: () -> Unit,
    onTranslate: () -> Unit,
    onImageClick: () -> Unit,
    onCameraClick: () -> Unit,
    onMicClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(Spaces.Space24),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = Spaces.Space4),
        border = BorderStroke(Spaces.Space1, BorderStrokeColor),
        modifier = Modifier
            .fillMaxWidth()
            .height(Spaces.Space200)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(Spaces.Space20)
            ) {
                if (text.isEmpty()) {
                    Text(
                        text = hint,
                        color = Color.LightGray,
                        fontSize = TextSizes.TextSize20
                    )
                }

                BasicTextField(
                    value = text,
                    onValueChange = onValueChange,
                    textStyle = TextStyle(fontSize = TextSizes.TextSize20, color = Color.Black),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(end = Spaces.Space50)
                )

                if (text.isNotEmpty()) {
                    Column(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .width(Spaces.Space40),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(Spaces.Space8)
                    ) {
                        StandardCircleButton(icon = Icons.Default.Close, onClick = onClear)
                        StandardCircleButton(icon = Icons.AutoMirrored.Filled.VolumeUp, onClick = { })
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spaces.Space16, vertical = Spaces.Space12),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Spaces.Space12)
                ) {
                    StandardCircleButton(icon = Icons.Default.Image, onClick = onImageClick)
                    StandardCircleButton(icon = Icons.Default.CameraAlt, onClick = onCameraClick)
                    StandardCircleButton(icon = Icons.Default.Mic, onClick = onMicClick)
                }

                Box(
                    modifier = Modifier
                        .height(Spaces.Space44)
                        .background(BluePrimary, shape = RoundedCornerShape(Spaces.Space8))
                        .clip(RoundedCornerShape(Spaces.Space8))
                        .clickable { onTranslate() }
                        .padding(horizontal = Spaces.Space24),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.label_translate),
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}