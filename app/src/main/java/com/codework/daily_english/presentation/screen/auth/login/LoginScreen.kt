package com.codework.daily_english.presentation.screen.auth.login

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MenuBook
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.codework.daily_english.R
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codework.daily_english.presentation.theme.BluePrimary

@Composable
fun LoginScreen(
    onCancelClick: () -> Unit = {},
    onEmailLoginClick: () -> Unit = {},
    onGoogleLoginClick: () -> Unit = {},
    onAppleLoginClick: () -> Unit = {},
    onSignUpClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 24.dp)
            .systemBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = "Back",
                fontSize = 16.sp,
                color = Color.Black,
                modifier = Modifier
                    .clickable { onCancelClick() }
                    .padding(vertical = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(80.dp))

        Text(
            text = "Sign in to continue",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Sign in to save your learning progress\nand sync across all your devices.",
            fontSize = 15.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(50.dp))


        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(BluePrimary.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.MenuBook,
                contentDescription = "Logo",
                modifier = Modifier.size(40.dp),
                tint = BluePrimary
            )
        }

        Spacer(modifier = Modifier.height(50.dp))


        SocialLoginButton(
            text = "Continue with Google",
            icon = R.drawable.ic_google,
            onClick = onGoogleLoginClick
        )

        Spacer(modifier = Modifier.height(14.dp))

        SocialLoginButton(
            text = "Continue with Apple",
            icon = R.drawable.ic_group,
            onClick = onAppleLoginClick
        )

        Spacer(modifier = Modifier.height(14.dp))


        SocialLoginButton(
            text = "Continue with Email",
            icon = R.drawable.ic_gmail,
            onClick = onEmailLoginClick,
        )




        Spacer(modifier = Modifier.height(4.dp))

        Row(
            modifier = Modifier.padding(bottom = 32.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Don't have an account? ",
                color = Color.Gray,
                fontSize = 15.sp
            )
            Text(
                text = "Sign up",
                color = Color(0xFF1877F2),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .clickable { onSignUpClick() }
                    .padding(4.dp)
            )
        }
    }
}

/**
 * Component Nút đăng nhập có icon canh trái, chữ canh giữa tuyệt đối
 */
@Composable
fun SocialLoginButton(
    text: String,
    icon: Int? = null,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(percent = 50),
        color = Color.White,
        border = BorderStroke(1.2.dp, Color(0xFFDADCE0)),
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Icon luôn cố định ở bên trái
            if (icon != null) {
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 20.dp)
                        .size(22.dp)
                )
            }

            // Text luôn nằm giữa button
            Text(
                text = text,
                color = Color.Black,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    MaterialTheme {
        LoginScreen()
    }
}