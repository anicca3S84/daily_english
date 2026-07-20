package com.codework.dailyenglish.presentation.screen.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.rounded.MilitaryTech // Sử dụng biểu tượng vương miện/huy chương có sẵn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.codework.dailyenglish.presentation.theme.AppColors

@Composable
fun HomeHeader(
    name: String,
    avatarUrl: String?,
    isPremium: Boolean,
    onPremiumClick: () -> Unit,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = avatarUrl ?: "",
            contentDescription = "User Avatar",
            modifier = Modifier
                .size(46.dp)
                .clip(CircleShape)
                .clickable{
                    onClick()
                },
            contentScale = ContentScale.Crop,
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium,
                    color = AppColors.TitleColor,
                    fontWeight = FontWeight.Bold
                )
                if (isPremium) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFFFD700), RoundedCornerShape(4.dp))
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                        Text("PRO", fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color.Black)
                    }
                }
            }
            Text(
                text = "Are you ready for focus?",
                style = MaterialTheme.typography.bodyMedium,
                color = AppColors.TextSub
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(
                        if (isPremium) Color(0xFFFFF9E6) else AppColors.Slate100
                    )
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        onPremiumClick()
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = CrownIcon,
                    contentDescription = "Premium Status",
                    tint = if (isPremium) Color(0xFFFFB300) else Color.Gray,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}


val CrownIcon: ImageVector
    get() = ImageVector.Builder(
        name = "Crown",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).path(fill = SolidColor(Color.White)) {
        moveTo(2f, 17f)
        lineTo(4f, 9f)
        lineTo(9f, 13f)
        lineTo(12f, 5f)
        lineTo(15f, 13f)
        lineTo(20f, 9f)
        lineTo(22f, 17f)
        close()

        moveTo(2f, 19f)
        lineTo(22f, 19f)
        lineTo(22f, 20.5f)
        lineTo(2f, 20.5f)
        close()

        moveTo(12f, 2f)
        lineTo(13.5f, 3.5f)
        lineTo(12f, 5f)
        lineTo(10.5f, 3.5f)
        close()

        moveTo(4f, 6f)
        lineTo(5.5f, 7.5f)
        lineTo(4f, 9f)
        lineTo(2.5f, 7.5f)
        close()

        moveTo(20f, 6f)
        lineTo(21.5f, 7.5f)
        lineTo(20f, 9f)
        lineTo(18.5f, 7.5f)
        close()
    }.build()