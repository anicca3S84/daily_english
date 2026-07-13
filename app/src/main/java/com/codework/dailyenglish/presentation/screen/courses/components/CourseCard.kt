package com.codework.dailyenglish.presentation.screen.courses.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp // BỔ SUNG: Xử lý font chữ .sp
import com.codework.dailyenglish.R
import com.codework.dailyenglish.presentation.components.AppProgressBar
import com.codework.dailyenglish.presentation.screen.courses.model.CourseUi
import com.codework.dailyenglish.presentation.theme.Spaces

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseCard(
    course: CourseUi,
    modifier: Modifier = Modifier,
    isLocked: Boolean = false,
    isCompleted: Boolean = false,
    isPremium: Boolean = false, // BỔ SUNG: Xác định khóa học có yêu cầu VIP hay không
    onClick: () -> Unit = {},
) {
    val progressPercent = (course.progress * 100).toInt()
    val safeProgress = course.progress.coerceIn(0f, 1f)

    val activeColor = when {
        isLocked && isPremium -> Color(0xFFF59E0B)
        isLocked -> Color.Gray
        else -> course.themeColor.primaryColor
    }

    Card(
        onClick = onClick,
        modifier = modifier.height(Spaces.Space165),
        shape = RoundedCornerShape(Spaces.Space20),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = Spaces.Space2)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {

            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .width(Spaces.Space40)
                    .fillMaxHeight()
                    .background(activeColor.copy(alpha = 0.1f))
            )

            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .width(Spaces.Space40)
                    .padding(top = Spaces.Space16),
                contentAlignment = Alignment.TopCenter
            ) {
                when {
                    isCompleted -> Icon(
                        Icons.Rounded.CheckCircle,
                        contentDescription = null,
                        tint = Color(0xFF4CAF50)
                    )
                    isLocked -> Icon(
                        Icons.Rounded.Lock,
                        contentDescription = null,
                        tint = if (isPremium) Color(0xFFD97706) else Color.Gray // Ổ khóa vàng sang trọng cho khóa Premium
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = Spaces.Space16, top = Spaces.Space16, bottom = Spaces.Space16, end = Spaces.Space48)
                    .alpha(if (isLocked) 0.75f else 1f) // Giảm độ mờ một chút để thẻ VIP trông vẫn sắc nét thu hút
            ) {
                Icon(
                    imageVector = course.icon,
                    contentDescription = null,
                    tint = activeColor,
                    modifier = Modifier.size(Spaces.Space36)
                )

                Spacer(modifier = Modifier.height(Spaces.Space12))

                // ĐỔI MỚI: Đặt thẻ tiêu đề kết hợp nhãn PRO
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = course.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false) // Nhường không gian cho nhãn PRO kế bên
                    )

                    if (isPremium) {
                        Spacer(modifier = Modifier.width(Spaces.Space6))
                        // Nhãn dán "PRO" nhỏ gọn màu vàng kim tinh tế
                        Box(
                            modifier = Modifier
                                .background(Color(0xFFFEF3C7), shape = RoundedCornerShape(Spaces.Space4))
                                .padding(horizontal = Spaces.Space6, vertical = Spaces.Space2)
                        ) {
                            Text(
                                text = "PRO",
                                color = Color(0xFFD97706),
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = 10.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(Spaces.Space2))

                Text(
                    text = course.description,
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontSize = 10.sp
                    ),
                    color = Color.Gray,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.weight(1f))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${course.lessons} " + stringResource(R.string.label_lessons),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Gray
                    )
                    Text(
                        text = "$progressPercent%",
                        style = MaterialTheme.typography.labelSmall,
                        color = activeColor,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(Spaces.Space8))

                val progressBrush = remember(activeColor) {
                    Brush.linearGradient(listOf(activeColor, activeColor))
                }

                AppProgressBar(
                    progress = safeProgress,
                    modifier = Modifier.fillMaxWidth(),
                    activeColor = progressBrush,
                    height = Spaces.Space7
                )
            }
        }
    }
}