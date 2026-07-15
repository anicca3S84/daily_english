package com.codework.dailyenglish.presentation.screen.leaderboard.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.codework.dailyenglish.R
import com.codework.dailyenglish.domain.leaderboard.model.LeaderBoardProfile
import com.codework.dailyenglish.presentation.screen.leaderboard.TextDark
import com.codework.dailyenglish.presentation.theme.AppColors

@Composable
fun LeaderboardRowItem(
    user: LeaderBoardProfile,
    isStreak: Boolean
) {
    if (user.isCurrentUser) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F5FF)),
            border = BorderStroke(1.dp, AppColors.MainGradient),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = user.rank.toString(),
                    style = TextStyle(
                        brush = AppColors.MainGradient,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black
                    ),
                    modifier = Modifier.width(36.dp),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.width(16.dp))

                AsyncImage(
                    model = user.avatarUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = "You",
                    style = TextStyle(
                        brush = AppColors.MainGradient,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black
                    ),
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                // Đã tối ưu cho RowItem của CurrentUser: Tách text + Image thay cho emoji thô
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = if (isStreak) "${user.score}" else "${user.score} Xp",
                        style = TextStyle(
                            brush = AppColors.MainGradient,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black
                        )
                    )
                    if (isStreak) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_streak),
                            contentDescription = "Streak Icon",
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    } else {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = user.rank.toString(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark,
                    modifier = Modifier.width(28.dp),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.width(8.dp))

                AsyncImage(
                    model = user.avatarUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = user.fullName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark,
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                // Đã tối ưu cho RowItem của các User khác: Thay thế emoji sang ic_streak sạch đẹp
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = if (isStreak) "${user.score}" else "${user.score} Xp",
                        style = TextStyle(
                            brush = AppColors.MainGradient,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    if (isStreak) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_streak),
                            contentDescription = "Streak Icon",
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
            HorizontalDivider(color = Color(0xFFF1F5F9), thickness = 1.dp)
        }
    }
}