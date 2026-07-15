package com.codework.dailyenglish.presentation.screen.leaderboard.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.codework.dailyenglish.R
import com.codework.dailyenglish.domain.leaderboard.model.LeaderBoardProfile
import com.codework.dailyenglish.presentation.screen.leaderboard.TextDark
import com.codework.dailyenglish.presentation.theme.AppColors

@Composable
fun PodiumAvatar(
    profile: LeaderBoardProfile?,
    rank: Int,
    avatarSize: Dp,
    color: Color,
    hasCrown: Boolean = false,
    isStreak: Boolean = false,
    modifier: Modifier = Modifier
) {
    if (profile == null) return

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom,
        modifier = modifier.padding(bottom = if (rank == 1) 16.dp else 0.dp)
    ) {
        Box(contentAlignment = Alignment.TopCenter) {
            Box(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .size(avatarSize)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .border(3.dp, color, CircleShape)
                        .padding(4.dp)
                        .clip(CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = profile.avatarUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }

                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(color, CircleShape)
                        .align(Alignment.TopStart)
                        .offset(x = (-2).dp, y = (-2).dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = rank.toString(),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.Center)
                            .offset(x = 1.6.dp, y = 1.5.dp),
                        style = TextStyle(
                            platformStyle = PlatformTextStyle(
                                includeFontPadding = false
                            ),
                            letterSpacing = 0.sp
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = if (profile.isCurrentUser) "You" else profile.fullName,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = TextDark,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.widthIn(max = 90.dp),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(4.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = if (isStreak) "${profile.score}" else "${profile.score} Xp",
                style = TextStyle(
                    brush = AppColors.MainGradient,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
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