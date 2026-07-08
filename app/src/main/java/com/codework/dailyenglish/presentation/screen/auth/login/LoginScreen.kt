package com.codework.dailyenglish.presentation.screen.auth.login

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.MenuBook
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codework.dailyenglish.BuildConfig
import com.codework.dailyenglish.R
import com.codework.dailyenglish.core.utils.GoogleAuthHelper
import com.codework.dailyenglish.presentation.components.LoadingType
import com.codework.dailyenglish.presentation.components.StateWrapper
import com.codework.dailyenglish.presentation.screen.auth.login.mvi.LoginEvent
import com.codework.dailyenglish.presentation.screen.auth.login.mvi.LoginState
import com.codework.dailyenglish.presentation.theme.Spaces
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .align(Alignment.TopEnd)
            ) {
                val width = size.width
                val height = size.height
                val path = Path().apply {
                    moveTo(width, 0f)
                    lineTo(width * 0.55f, 0f)
                    quadraticBezierTo(
                        width * 0.75f, height * 0.4f,
                        width, height
                    )
                    close()
                }
                drawPath(path, color = Color(0xFFEFF6FF))
            }

            // 2. Các họa tiết trang trí xung quanh (Vòng tròn và Ngôi sao bốn cánh)
            // Vòng tròn nét đứt/nhạt góc trên bên trái
            OutlineCircle(
                color = Color(0xFFDBEAFE),
                modifier = Modifier
                    .padding(top = 110.dp, start = 60.dp)
                    .size(22.dp)
            )

            // Ngôi sao vàng góc trên bên phải
            SparkleIcon(
                color = Color(0xFFFBBF24),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 130.dp, end = 80.dp)
                    .size(16.dp)
            )

            // Ngôi sao xanh dương bên trái giữa màn hình
            SparkleIcon(
                color = Color(0xFF60A5FA),
                modifier = Modifier
                    .padding(top = 280.dp, start = 40.dp)
                    .size(20.dp)
            )

            // Vòng tròn nhạt bên phải trên ảnh minh họa
            OutlineCircle(
                color = Color(0xFFDBEAFE),
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(top = 8.dp, end = 100.dp)
                    .size(14.dp)
            )

            // Ngôi sao xanh dương nhỏ bên phải giữa màn hình
            SparkleIcon(
                color = Color(0xFF60A5FA),
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(top = 140.dp, end = 50.dp)
                    .size(12.dp)
            )

            // Nội dung chính của màn hình
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .navigationBarsPadding()
                    .verticalScroll(scrollState)
                    .padding(horizontal = Spaces.Space24),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Image(
                    painter = painterResource(id = R.drawable.ic_app_logo),
                    contentDescription = "App Logo",
                    modifier = Modifier
                        .size(140.dp)
                        .clip(RoundedCornerShape(20.dp)),
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.height(Spaces.Space24))

                Text(
                    text = "Welcome to\nDaily English",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A),
                    textAlign = TextAlign.Center,
                    lineHeight = 38.sp
                )

                Spacer(modifier = Modifier.height(Spaces.Space12))

                // Mô tả phụ
                Text(
                    text = "Learn English every day,\nimprove step by step.",
                    fontSize = 16.sp,
                    color = Color(0xFF64748B),
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(Spaces.Space24))

                Image(
                    painter = painterResource(id = R.drawable.img_welcome_illustration),
                    contentDescription = "Welcome Illustration",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp),
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.height(Spaces.Space40))

                // Nút "Continue with Google"
                OutlinedButton(
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
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.White
                    ),
                    border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_google),
                            contentDescription = "Google Logo",
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Continue with Google",
                            color = Color(0xFF0F172A),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(Spaces.Space24))

                // Cam kết bảo mật phía cuối cùng
                val inlineContent = mapOf(
                    "lock" to InlineTextContent(
                        Placeholder(
                            width = 12.sp,
                            height = 12.sp,
                            placeholderVerticalAlign = PlaceholderVerticalAlign.Center
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Lock,
                            contentDescription = null,
                            tint = Color(0xFF94A3B8)
                        )
                    }
                )

                Text(
                    text = buildAnnotatedString {
                        appendInlineContent("lock", "[lock]")
                        append(" We never post without your permission.\nYour data is safe with us.")
                    },
                    inlineContent = inlineContent,
                    fontSize = 13.sp,
                    color = Color(0xFF94A3B8),
                    textAlign = TextAlign.Center,
                    lineHeight = 16.sp
                )
            }
        }
    }
}

// Composable bổ trợ vẽ ngôi sao lấp lánh bằng Canvas
@Composable
fun SparkleIcon(color: Color, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val path = Path().apply {
            moveTo(size.width / 2, 0f)
            quadraticBezierTo(size.width / 2, size.height / 2, size.width, size.height / 2)
            quadraticBezierTo(size.width / 2, size.height / 2, size.width / 2, size.height)
            quadraticBezierTo(size.width / 2, size.height / 2, 0f, size.height / 2)
            quadraticBezierTo(size.width / 2, size.height / 2, size.width / 2, 0f)
        }
        drawPath(path, color)
    }
}

// Composable bổ trợ vẽ vòng tròn rỗng
@Composable
fun OutlineCircle(color: Color, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        drawCircle(
            color = color,
            radius = size.minDimension / 2,
            style = Stroke(width = 2.5.dp.toPx())
        )
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