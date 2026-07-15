package com.codework.dailyenglish.presentation.screen.lessons.lesson_result

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codework.dailyenglish.R
import com.codework.dailyenglish.presentation.theme.AppColors
import com.codework.dailyenglish.presentation.theme.AppTextSizes
import com.codework.dailyenglish.presentation.theme.Spaces

@Composable
fun AppCongratulationsScreen(
    scorePercentage: Int,
    starsEarned: Int,
    streak: String? = null,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(Spaces.Space24),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp),
            contentAlignment = Alignment.Center
        ) {
            ConfettiBackground(modifier = Modifier.fillMaxSize())
            HexagonBadge(modifier = Modifier.size(150.dp))

            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = (-16).dp)
                    .background(
                        brush = AppColors.MainGradient,
                        shape = RoundedCornerShape(50)
                    )
                    .padding(horizontal = 28.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "Amazing!",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = AppTextSizes.TextSizeMain
                )
            }
        }

        Spacer(modifier = Modifier.height(Spaces.Space16))

        Text(
            text = "Congratulations!",
            fontSize = AppTextSizes.TextSize24,
            fontWeight = FontWeight.ExtraBold,
            color = AppColors.Slate800
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "You did an excellent job!",
            fontSize = AppTextSizes.TextSizeMain,
            fontWeight = FontWeight.Medium,
            color = AppColors.Slate500
        )
        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .width(40.dp)
                .height(4.dp)
                .background(Color(0xFFFBBF24), shape = RoundedCornerShape(50))
        )

        Spacer(modifier = Modifier.height(Spaces.Space32))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Spaces.Space12, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val cardModifier = Modifier.weight(1f)

            StatCard(
                icon = {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(Color(0xFFDCFCE7), shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = "Accuracy Icon",
                            tint = Color(0xFF22C55E),
                            modifier = Modifier.size(28.dp)
                        )
                    }
                },
                value = "$scorePercentage%",
                label = "Accuracy",
                modifier = cardModifier
            )

            StatCard(
                icon = {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(Color(0xFFFEF9C3), shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Star,
                            contentDescription = "Stars Icon",
                            tint = Color(0xFFFBBF24),
                            modifier = Modifier.size(28.dp)
                        )
                    }
                },
                value = "$starsEarned",
                label = "Stars",
                modifier = cardModifier
            )

            if (streak != null) {
                StatCard(
                    icon = {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(Color(0xFFFFE4E6), shape = CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_streak),
                                contentDescription = "Streak Icon",
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    },
                    value = "$streak",
                    label = "Streak",
                    modifier = cardModifier
                )
            }
        }

        Spacer(modifier = Modifier.height(Spaces.Space32))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F5F9)),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Spaces.Space16),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_congratulation),
                    contentDescription = "Congratulation Icon",
                    modifier = Modifier
                        .size(44.dp) // Size vừa vặn nổi bật ở góc trái
                        .padding(end = Spaces.Space12)
                )

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Great job, keep going!",
                        fontWeight = FontWeight.Bold,
                        fontSize = AppTextSizes.TextSizeMain,
                        color = AppColors.Slate800
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Every day you learn is a step closer to your goal.",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = AppColors.Slate500
                    )
                }

                Image(
                    painter = painterResource(id = R.drawable.ic_owl),
                    contentDescription = "Owl Mascot",
                    modifier = Modifier
                        .size(48.dp) // Kích thước chú cú mascot 48dp cân đối bên góc phải
                        .padding(start = Spaces.Space12)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppCongratulationsScreenWithStreakPreview() {
    AppCongratulationsScreen(
        scorePercentage = 100,
        starsEarned = 3,
        streak = "7"
    )
}

@Preview(showBackground = true)
@Composable
fun AppCongratulationsScreenWithoutStreakPreview() {
    AppCongratulationsScreen(
        scorePercentage = 100,
        starsEarned = 3,
        streak = null
    )
}