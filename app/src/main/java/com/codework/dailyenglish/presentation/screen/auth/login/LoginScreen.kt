package com.codework.dailyenglish.presentation.screen.auth.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
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
import com.codework.dailyenglish.BuildConfig
import com.codework.dailyenglish.R
import com.codework.dailyenglish.core.utils.GoogleAuthHelper
import com.codework.dailyenglish.presentation.components.LoadingType
import com.codework.dailyenglish.presentation.components.StateWrapper
import com.codework.dailyenglish.presentation.screen.auth.login.components.SocialLoginButton
import com.codework.dailyenglish.presentation.screen.auth.login.mvi.LoginEvent
import com.codework.dailyenglish.presentation.screen.auth.login.mvi.LoginState
import com.codework.dailyenglish.presentation.theme.AppColors
import com.codework.dailyenglish.presentation.theme.AppIcons
import com.codework.dailyenglish.presentation.theme.Spaces
import com.codework.dailyenglish.presentation.theme.AppTextSizes
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    state: LoginState,
    onEvent: (LoginEvent) -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    StateWrapper(
        uiState = state,
        loadingType = LoadingType.CIRCULAR
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .systemBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spaces.Space24)
                    .padding(top = Spaces.Space16),
                contentAlignment = Alignment.CenterStart
            ){
                Text(
                    text = stringResource(R.string.label_back),
                    fontSize = AppTextSizes.TextSizeMain,
                    color = Color.Black,
                    modifier = Modifier
                        .clickable { onEvent(LoginEvent.OnCancelClick) }
                        .padding(vertical = Spaces.Space8)
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
                    .padding(horizontal = Spaces.Space24),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.height(Spaces.Space40))

                Text(
                    text = stringResource(R.string.login_sign_in_title),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(Spaces.Space16))

                Text(
                    text = stringResource(R.string.login_description),
                    fontSize = AppTextSizes.TextSizeMain,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(Spaces.Space40))

                Box(
                    modifier = Modifier
                        .size(Spaces.Space100)
                        .clip(CircleShape)
                        .background(AppColors.BluePrimary.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.MenuBook,
                        contentDescription = "Logo",
                        modifier = Modifier.size(AppIcons.IconSizeLarge),
                        tint = AppColors.BluePrimary
                    )
                }

                Spacer(modifier = Modifier.height(Spaces.Space40))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(Spaces.Space14)
                ) {
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

                    SocialLoginButton(
                        text = stringResource(R.string.login_apple),
                        icon = R.drawable.ic_group,
                        onClick = { onEvent(LoginEvent.OnAppleLoginClick) }
                    )

                    SocialLoginButton(
                        text = stringResource(R.string.login_email),
                        icon = R.drawable.ic_gmail,
                        onClick = { onEvent(LoginEvent.OnEmailLoginClick) }
                    )
                }

                Spacer(modifier = Modifier.height(Spaces.Space40))
            }

            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.White
            ) {
                Row(
                    modifier = Modifier
                        .padding(vertical = Spaces.Space24)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(R.string.login_no_account_full),
                        color = Color.Gray,
                        fontSize = AppTextSizes.TextSizeMain
                    )
                    Text(
                        text = stringResource(R.string.login_sign_up),
                        color = AppColors.BluePrimary,
                        fontSize = AppTextSizes.TextSizeMain,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .clickable { onEvent(LoginEvent.OnSignUpClick) }
                            .padding(Spaces.Space4)
                    )
                }
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