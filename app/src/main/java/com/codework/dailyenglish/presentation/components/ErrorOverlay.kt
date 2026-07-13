package com.codework.dailyenglish.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.codework.dailyenglish.presentation.theme.AppColors

@Composable
fun ErrorOverlay(
    errorType: ErrorVisual,
    message: String,
    onRetry: (() -> Unit)?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(16.dp), // Để Column tự ôm khít nội dung thay vì bắt buộc fillMaxSize
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Chỉ vẽ Icon và Spacer nếu icon không rỗng
        if (errorType.icon.isNotBlank()) {
            Text(text = errorType.icon, style = MaterialTheme.typography.displayMedium)
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Chỉ vẽ Title và Spacer nếu title không rỗng
        if (errorType.title.isNotBlank()) {
            Text(text = errorType.title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
        }

        // Chỉ vẽ Message nếu message không rỗng
        if (message.isNotBlank()) {
            Text(text = message, textAlign = TextAlign.Center)
        }

        // Chỉ vẽ Button nếu có hàm onRetry
        if (onRetry != null) {
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { onRetry.invoke() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = AppColors.BluePrimary,
                    contentColor = androidx.compose.ui.graphics.Color.White
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 4.dp)
                )
                Text(text = "Try again")
            }
        }
    }
}