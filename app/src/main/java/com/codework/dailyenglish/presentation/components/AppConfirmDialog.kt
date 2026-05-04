package com.codework.dailyenglish.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.codework.dailyenglish.presentation.theme.AppColors
import com.codework.dailyenglish.presentation.theme.AppRadius
import com.codework.dailyenglish.presentation.theme.AppTextSizes
import com.codework.dailyenglish.presentation.theme.Spaces
import com.codework.dailyenglish.R

@Composable
fun AppConfirmDialog(
    title: String,
    message: String,
    confirmText: String = stringResource(R.string.label_confirm),
    cancelText: String = stringResource(R.string.label_cancel),
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .wrapContentHeight()
        ) {

            Surface(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(AppRadius.RadiusLarge),
                color = Color.White,
                shadowElevation = Spaces.Space8
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Spaces.Space20),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = title,
                        fontSize = AppTextSizes.TextSize24,
                        fontWeight = FontWeight.Bold,
                        color = AppColors.Slate800,
                        modifier = Modifier.padding(vertical = Spaces.Space8)
                    )

                    Spacer(modifier = Modifier.height(Spaces.Space16))

                    HorizontalDivider(color = AppColors.Slate100, thickness = 1.dp)

                    Text(
                        text = message,
                        fontSize = AppTextSizes.TextSizeMain,
                        color = AppColors.Slate500,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(
                            vertical = Spaces.Space32,
                            horizontal = Spaces.Space12
                        ),
                        lineHeight = AppTextSizes.TextSize24
                    )

                    HorizontalDivider(color = AppColors.Slate100, thickness = 1.dp)

                    Spacer(modifier = Modifier.height(Spaces.Space32))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(Spaces.Space16)
                    ) {
                        OutlinedButton(
                            onClick = onDismiss,
                            modifier = Modifier
                                .weight(1f)
                                .height(Spaces.Space52),
                            shape = RoundedCornerShape(AppRadius.RadiusFull),
                            border = BorderStroke(Spaces.Space1, AppColors.BluePrimary)
                        ) {
                            Text(
                                text = cancelText,
                                color = AppColors.BluePrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = AppTextSizes.TextSizeMain
                            )
                        }

                        Button(
                            onClick = onConfirm,
                            modifier = Modifier
                                .weight(1f)
                                .height(Spaces.Space52),
                            shape = RoundedCornerShape(AppRadius.RadiusFull),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AppColors.BluePrimary
                            )
                        ) {
                            Text(
                                text = confirmText,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = AppTextSizes.TextSizeMain
                            )
                        }
                    }
                }
            }

            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(Spaces.Space12)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    modifier = Modifier.size(Spaces.Space26)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppConfirmDialogPreview() {
    AppConfirmDialog(
        title = "Delete Session",
        message = "Are you sure you want to delete this task?",
        onConfirm = {},
        onDismiss = {}
    )
}