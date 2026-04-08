package com.codework.daily_english.presentation.screen.auth.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MenuBook
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.codework.daily_english.BuildConfig
import com.codework.daily_english.R
import com.codework.daily_english.core.utils.GoogleAuthHelper
import com.codework.daily_english.presentation.components.LoadingType
import com.codework.daily_english.presentation.components.StateWrapper
import com.codework.daily_english.presentation.theme.BluePrimary
import com.codework.daily_english.presentation.screen.auth.login.components.SocialLoginButton
import com.codework.daily_english.presentation.screen.auth.login.mvi.LoginEvent
import com.codework.daily_english.presentation.screen.auth.login.mvi.LoginState
import com.codework.daily_english.presentation.theme.IconSizeLarge
import com.codework.daily_english.presentation.theme.Spaces
import com.codework.daily_english.presentation.theme.TextSizes
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    state: LoginState,
    onEvent: (LoginEvent) -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    StateWrapper(
        uiState = state,
        loadingType = LoadingType.CIRCULAR
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(horizontal = Spaces.Space24)
                .systemBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Spaces.Space16),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = stringResource(R.string.label_back),
                    fontSize = TextSizes.TextSizeMain,
                    color = Color.Black,
                    modifier = Modifier
                        .clickable { onEvent(LoginEvent.OnCancelClick) }
                        .padding(vertical = Spaces.Space8)
                )
            }

            Spacer(modifier = Modifier.height(Spaces.Space80))

            Text(
                text = stringResource(R.string.login_sign_in_title),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(Spaces.Space20))

            Text(
                text = stringResource(R.string.login_description),
                fontSize = TextSizes.TextSizeMain,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(Spaces.Space50))

            Box(
                modifier = Modifier
                    .size(Spaces.Space100)
                    .clip(CircleShape)
                    .background(BluePrimary.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.MenuBook,
                    contentDescription = "Logo",
                    modifier = Modifier.size(IconSizeLarge),
                    tint = BluePrimary
                )
            }

            Spacer(modifier = Modifier.height(Spaces.Space50))

            SocialLoginButton(
                text = stringResource(R.string.login_google),
                icon = R.drawable.ic_google,
                onClick = {
                    coroutineScope.launch {
                        val idToken = GoogleAuthHelper.signIn(
                            context = context,
                            serverClientId = BuildConfig.WEB_CLIENT_ID
                        )
                        if (idToken != null) {
                            onEvent(LoginEvent.LoginWithGoogle(idToken))
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(Spaces.Space14))

            SocialLoginButton(
                text = stringResource(R.string.login_apple),
                icon = R.drawable.ic_group,
                onClick = { onEvent(LoginEvent.OnAppleLoginClick) }
            )

            Spacer(modifier = Modifier.height(Spaces.Space14))

            SocialLoginButton(
                text = stringResource(R.string.login_email),
                icon = R.drawable.ic_gmail,
                onClick = { onEvent(LoginEvent.OnEmailLoginClick) }
            )

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier.padding(bottom = Spaces.Space32),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.login_no_account_full),
                    color = Color.Gray,
                    fontSize = TextSizes.TextSizeMain
                )
                Text(
                    text = stringResource(R.string.login_sign_up),
                    color = BluePrimary,
                    fontSize = TextSizes.TextSizeMain,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clickable { onEvent(LoginEvent.OnSignUpClick) }
                        .padding(Spaces.Space4)
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    MaterialTheme {
        LoginScreen(
            state = LoginState(),
            onEvent = {}
        )
    }
}