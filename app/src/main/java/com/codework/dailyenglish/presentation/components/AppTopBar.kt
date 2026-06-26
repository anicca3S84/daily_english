package com.codework.dailyenglish.presentation.components
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.codework.dailyenglish.presentation.theme.AppColors
import com.codework.dailyenglish.presentation.theme.AppIcons
import com.codework.dailyenglish.presentation.theme.Spaces

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    onBackClick: () -> Unit,
    onMenuClick: (() -> Unit)? = null,
    titleContent: @Composable () -> Unit
) {
    Surface(
        color = Color.White,
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = Spaces.Space0
    ) {
        Column(
            modifier = Modifier.statusBarsPadding()
        ) {
            CenterAlignedTopAppBar(
                windowInsets = WindowInsets(Spaces.Space0),
                modifier = Modifier.wrapContentHeight(),
                title = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = Spaces.Space8),
                        contentAlignment = Alignment.Center
                    ) {
                        titleContent()
                    }
                },
                navigationIcon = {
                    Box(
                        modifier = Modifier.padding(start = Spaces.Space12),
                        contentAlignment = Alignment.Center
                    ) {
                        Surface(
                            onClick = onBackClick,
                            shape = CircleShape,
                            color = Color.White,
                            border = BorderStroke(Spaces.Space1, Color(0xFFE5E7EB)),
                            modifier = Modifier.size(Spaces.Space40)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back",
                                    tint = Color(0xFF374151),
                                    modifier = Modifier.size(AppIcons.IconSizeSmall)
                                )
                            }
                        }
                    }
                },
                actions = {
                    Box(
                        modifier = Modifier.padding(end = Spaces.Space12),
                        contentAlignment = Alignment.Center
                    ) {
                        if (onMenuClick != null) {
                            Surface(
                                onClick = onMenuClick,
                                shape = CircleShape,
                                color = Color.White,
                                border = BorderStroke(Spaces.Space1, Color(0xFFE5E7EB)),
                                modifier = Modifier.size(Spaces.Space40)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Menu,
                                        contentDescription = null,
                                        tint = Color(0xFF374151),
                                        modifier = Modifier.size(AppIcons.IconSizeSmall)
                                    )
                                }
                            }
                        } else {
                            Spacer(modifier = Modifier.size(Spaces.Space40))
                        }
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    }
}