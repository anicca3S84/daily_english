package com.codework.daily_english.presentation.components
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.codework.daily_english.presentation.theme.AppBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    onBackClick: () -> Unit,
    onMenuClick: (() -> Unit)? = null,
    titleContent: @Composable () -> Unit
) {
    Surface(
        color = AppBackground.copy(alpha = 0.85f),
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = 0.dp
    ) {
        Column(
            // SỬA: Thêm statusBarsPadding ở đây để màu trắng tràn lên Status Bar
            // nhưng nội dung bên dưới bắt đầu từ vùng an toàn
            modifier = Modifier.statusBarsPadding()
        ) {
            CenterAlignedTopAppBar(
                // SỬA: Set windowInsets về 0 vì đã dùng statusBarsPadding ở trên
                windowInsets = WindowInsets(0),
                // SỬA: Không fix cứng height 64dp để nó tự co giãn theo titleContent
                modifier = Modifier.wrapContentHeight(),
                title = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth() // Ép title chiếm tối đa khoảng trống ở giữa
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        titleContent()
                    }
                },
                navigationIcon = {
                    Box(
                        modifier = Modifier.padding(start = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Surface(
                            onClick = onBackClick,
                            shape = CircleShape,
                            color = Color.White,
                            border = BorderStroke(1.dp, Color(0xFFE5E7EB)),
                            modifier = Modifier.size(40.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back",
                                    tint = Color(0xFF374151),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                },
                actions = {
                    Box(
                        modifier = Modifier.padding(end = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (onMenuClick != null) {
                            Surface(
                                onClick = onMenuClick,
                                shape = CircleShape,
                                color = Color.White,
                                border = BorderStroke(1.dp, Color(0xFFE5E7EB)),
                                modifier = Modifier.size(40.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(Icons.Default.Menu, null, tint = Color(0xFF374151), modifier = Modifier.size(20.dp))
                                }
                            }
                        } else {
                            // Giữ khoảng trống đối xứng để Title luôn ở tâm tuyệt đối
                            Spacer(modifier = Modifier.size(40.dp))
                        }
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent
                )
            )

            // Đường kẻ Gradient
//            Box(
//                modifier = Modifier.fillMaxWidth().height(1.dp)
//                    .background(Brush.horizontalGradient(listOf(Color.Transparent, Color(0xFFE5E7EB), Color.Transparent)))
//            )
        }
    }
}