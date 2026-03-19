package com.codework.daily_english.presentation.screen.auth.login

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codework.daily_english.presentation.components.BaseScreen
import com.codework.daily_english.core.base.BaseState

@Composable
fun LoginScreen(
    state: BaseState<LoginState>, // SỬA: LoginData -> LoginState
    onEvent: (LoginEvent) -> Unit
) {
    val textMain = Color(0xFF1E293B)
    val textSub = Color(0xFF64748B)

    Scaffold(
        containerColor = Color.White
    ) { paddingValues ->
        // BỌC TẤT CẢ VÀO BASESCREEN ĐỂ QUẢN LÝ LOADING/ERROR TỰ ĐỘNG
        BaseScreen(state = state) { data ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // --- PHẦN 1: HÌNH ẢNH & LỜI CHÀO ---
                Spacer(modifier = Modifier.weight(1f))

                Surface(
                    shape = CircleShape,
                    color = Color(0xFFEFF6FF),
                    modifier = Modifier.size(140.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("🚀", fontSize = 64.sp)
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))

                Text(
                    text = "Master English,\nUnlock the World.",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = textMain,
                    textAlign = TextAlign.Center,
                    lineHeight = 40.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Join millions of learners and start speaking fluently today.",
                    fontSize = 16.sp,
                    color = textSub,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.weight(1f))

                // --- PHẦN 2: CÁC NÚT ĐĂNG NHẬP 1 CHẠM ---

                SocialLoginButton(
                    text = "Continue with Google",
                    iconText = "G",
                    bgColor = Color.White,
                    textColor = textMain,
                    borderColor = Color(0xFFE2E8F0),
                    // SỬ DỤNG BIẾN 'data' ĐƯỢC BASESCREEN TRẢ VỀ
                    isLoading = data.isLoading && data.loadingProvider == "Google",
                    onClick = { onEvent(LoginEvent.OnContinueWithGoogle) }
                )

                Spacer(modifier = Modifier.height(16.dp))

                SocialLoginButton(
                    text = "Continue with Apple",
                    iconText = "",
                    bgColor = Color.Black,
                    textColor = Color.White,
                    borderColor = Color.Black,
                    isLoading = data.isLoading && data.loadingProvider == "Apple",
                    onClick = { onEvent(LoginEvent.OnContinueWithApple) }
                )

                Spacer(modifier = Modifier.height(24.dp))

                TextButton(
                    onClick = { onEvent(LoginEvent.OnContinueWithEmail) },
                    modifier = Modifier.fillMaxWidth().height(56.dp)
                ) {
                    Text(
                        text = "Continue with Email or Phone",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF3B82F6)
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // --- PHẦN 3: ĐIỀU KHOẢN ---
                val termsText = buildAnnotatedString {
                    append("By continuing, you agree to our ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = textMain)) {
                        append("Terms")
                    }
                    append(" and ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = textMain)) {
                        append("Privacy Policy")
                    }
                    append(".")
                }

                Text(
                    text = termsText,
                    fontSize = 13.sp,
                    color = textSub,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 32.dp, start = 16.dp, end = 16.dp)
                )
            }
        }
    }
}

@Composable
fun SocialLoginButton(
    text: String,
    iconText: String,
    bgColor: Color,
    textColor: Color,
    borderColor: Color,
    isLoading: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = bgColor,
        border = BorderStroke(1.dp, borderColor),
        modifier = Modifier.fillMaxWidth().height(60.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = if (bgColor == Color.Black) Color.White else Color(0xFF3B82F6),
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(text = iconText, fontSize = 20.sp, fontWeight = FontWeight.Black, color = textColor)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = text,
                        color = textColor,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

// --- PREVIEW ĐƯỢC BỌC TRONG UISTATE ---
@Preview(showBackground = true, name = "Welcome & Auth Screen")
@Composable
fun PreviewLoginScreen() {
    LoginScreen(
        state = BaseState.Success(LoginState()),
        onEvent = {}
    )
}