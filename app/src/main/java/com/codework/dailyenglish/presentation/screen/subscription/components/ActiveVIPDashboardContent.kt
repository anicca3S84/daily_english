package com.codework.dailyenglish.presentation.screen.subscription.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp

@Composable
fun ActiveVIPDashboardContent(
    expiryDate: String,
    onBuyMoreClick: () -> Unit,
    onClose: () -> Unit,
    BluePrimary: Color
) {
    val context = LocalContext.current
    val textDark = Color(0xFF1E293B)
    val textLight = Color(0xFF64748B)

    val mySupportPhoneNumber = "0764930000"

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(Color(0xFFFEF3C7), shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.Star,
                contentDescription = "VIP Active",
                tint = Color(0xFFD97706),
                modifier = Modifier.size(44.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "You are a PRO Member! 🎉",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Black,
                color = textDark
            ),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Thank you for supporting DailyEnglish. You have unlocked unlimited features!",
            style = MaterialTheme.typography.bodyMedium.copy(color = textLight),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFFFDF5), shape = RoundedCornerShape(16.dp))
                .border(1.dp, Color(0xFFFEF3C7), RoundedCornerShape(16.dp))
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "EXPIRATION DATE",
                style = MaterialTheme.typography.labelMedium.copy(
                    color = Color(0xFFD97706),
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = expiryDate,
                style = MaterialTheme.typography.titleLarge.copy(
                    color = textDark,
                    fontWeight = FontWeight.ExtraBold
                )
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onBuyMoreClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BluePrimary),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = "Extend / Buy More Packs",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // NÚT 2: Contact Support - Tự động gọi Intent nhảy sang danh mục điện thoại kèm điền sẵn SĐT của bạn [1]
        OutlinedButton(
            onClick = {
                val intent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:$mySupportPhoneNumber")
                }
                context.startActivity(intent)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = textDark)
        ) {
            Icon(
                imageVector = Icons.Rounded.Phone,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = textDark
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Contact Support",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}
