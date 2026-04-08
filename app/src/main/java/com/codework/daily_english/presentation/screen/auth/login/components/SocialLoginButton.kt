package com.codework.daily_english.presentation.screen.auth.login.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.codework.daily_english.presentation.theme.ButtonHeightLarge
import com.codework.daily_english.presentation.theme.Colors
import com.codework.daily_english.presentation.theme.IconSizeMedium
import com.codework.daily_english.presentation.theme.IconSizeSmall
import com.codework.daily_english.presentation.theme.RadiusFull
import com.codework.daily_english.presentation.theme.Spaces
import com.codework.daily_english.presentation.theme.TextSizes

@Composable
fun SocialLoginButton(
    text: String,
    icon: Int? = null,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(RadiusFull),
        color = Color.White,
        border = BorderStroke(Spaces.Space1_2, Colors.LoginButtonColor),
        modifier = Modifier
            .fillMaxWidth()
            .height(ButtonHeightLarge)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            icon?.let {
                Icon(
                    painter = painterResource(id = it),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = Spaces.Space20)
                        .size(IconSizeSmall)
                )
            }

            Text(
                text = text,
                color = Color.Black,
                fontSize = TextSizes.TextSizeMain,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}