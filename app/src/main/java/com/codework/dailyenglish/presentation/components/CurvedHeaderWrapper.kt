package com.codework.dailyenglish.presentation.components
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.codework.dailyenglish.presentation.theme.AppColors
import com.codework.dailyenglish.presentation.theme.Spaces

@Composable
fun CurvedHeaderWrapper(
    modifier: Modifier = Modifier,
    fixedHeaderHeight: Dp = 190.dp,
    backgroundBrush: Brush = Brush.horizontalGradient(
        colors = listOf(AppColors.BluePrimary, AppColors.IndigoPurple)
    ),
    content: @Composable ColumnScope.() -> Unit,
    bottomCardContent: @Composable RowScope.() -> Unit
) {
    val cardHalfHeight = Spaces.Space40
    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(fixedHeaderHeight)
                .background(
                    brush = backgroundBrush,
                    shape = RoundedCornerShape(
                        bottomStart = Spaces.Space32,
                        bottomEnd = Spaces.Space32
                    )
                )
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(fixedHeaderHeight)
                .statusBarsPadding()
                .padding(top = Spaces.Space8)
        ) {
            content()
        }
        Card(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = Spaces.Space24)
                .offset(y = cardHalfHeight)
                .fillMaxWidth(),
            shape = RoundedCornerShape(Spaces.Space20),
            elevation = CardDefaults.cardElevation(defaultElevation = Spaces.Space8),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = Spaces.Space16, horizontal = Spaces.Space16),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                bottomCardContent()
            }
        }
    }
    Spacer(modifier = Modifier.height(cardHalfHeight + Spaces.Space10))
}
