package com.codework.dailyenglish.presentation.components

// 1. Compose UI Core
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*

// 2. Animation & Graphics
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp

// 3. Material Design 3
import androidx.compose.material3.MaterialTheme

// 4. System & Hardware
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.activity.compose.BackHandler

// 5. Coroutines
import kotlinx.coroutines.launch

@Composable
fun AppBottomSheet(
    visible: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    maxHeight: Float = 0.9f,
    enableDrag: Boolean = true,
    content: @Composable ColumnScope.() -> Unit
) {
    var isActuallyVisible by remember { mutableStateOf(visible) }
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val maxSheetHeight = screenHeight * maxHeight
    val offsetY = remember { Animatable(screenHeight.value) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(visible) {
        if (visible) {
            isActuallyVisible = true
            offsetY.animateTo(0f, tween(300, easing = FastOutSlowInEasing))
        } else {
            offsetY.animateTo(screenHeight.value, tween(250))
            isActuallyVisible = false
        }
    }

    BackHandler(enabled = visible) {
        onDismiss()
    }

    if (!isActuallyVisible && !visible) return

    Popup(
        onDismissRequest = onDismiss,
        properties = PopupProperties(focusable = true)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            val alpha by animateFloatAsState(
                targetValue = if (visible) 0.4f else 0f,
                animationSpec = tween(300), label = ""
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = alpha))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { onDismiss() }
            )

            // Sheet content
            Box(
                modifier = modifier
                    .offset { IntOffset(0, offsetY.value.toInt().coerceAtLeast(0)) } // Không cho kéo ngược lên trên quá đà
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .heightIn(max = maxSheetHeight)
                    .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    // THÊM ĐOẠN NÀY ĐỂ KÉO ĐƯỢC:
                    .pointerInput(enableDrag) {
                        if (!enableDrag) return@pointerInput
                        detectVerticalDragGestures(
                            onDragEnd = {
                                // Nếu kéo xuống quá 1/4 chiều cao sheet thì đóng, không thì nảy lại vị trí cũ
                                if (offsetY.value > screenHeight.value / 4) {
                                    onDismiss()
                                } else {
                                    coroutineScope.launch {
                                        offsetY.animateTo(0f, tween(200))
                                    }
                                }
                            },
                            onVerticalDrag = { change, dragAmount ->
                                change.consume()
                                // Chỉ cho phép kéo xuống (dragAmount > 0)
                                coroutineScope.launch {
                                    val newValue = offsetY.value + dragAmount
                                    offsetY.snapTo(newValue.coerceAtLeast(0f))
                                }
                            }
                        )
                    }
            ) {
                // ĐÂY LÀ PHẦN QUAN TRỌNG NHẤT: Bọc thẳng bằng Column, không dùng Box trung gian
                Column(
                    modifier = Modifier
                        .imePadding()
                        .fillMaxWidth()
                ) {
                    // Thanh gạch ngang (Drag Handle)
                    if (enableDrag) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(vertical = 12.dp)
                                .size(width = 40.dp, height = 4.dp)
                                .background(Color.Gray.copy(alpha = 0.3f), CircleShape)
                        )
                    }

                    // Ruột BottomSheet: Được hưởng toàn bộ đặc quyền của ColumnScope
                    content()
                }
            }
        }
    }
}