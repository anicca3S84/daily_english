package com.codework.dailyenglish.presentation.screen.more

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.codework.dailyenglish.presentation.screen.more.components.ProfileHeaderSection
import com.codework.dailyenglish.presentation.screen.more.components.ProfileMenuItem
import com.codework.dailyenglish.presentation.screen.more.components.SettingsGroupTitle
import com.codework.dailyenglish.presentation.screen.more.mvi.MoreEvent
import com.codework.dailyenglish.presentation.screen.more.mvi.MoreState

@Composable
fun MoreScreen(
    state: MoreState,
    onEvent: (MoreEvent) -> Unit
) {
    // ĐÃ BỎ StateWrapper ở đây
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            ProfileHeaderSection(
                data = state,
                onEditClick = { onEvent(MoreEvent.OnEditProfile) }
            )
        }
        item {
            Column(modifier = Modifier.padding(16.dp)) {
                SettingsGroupTitle("Account Settings")
                ProfileMenuItem(
                    icon = Icons.Outlined.Person,
                    title = "Personal Information",
                    onClick = { onEvent(MoreEvent.OnMenuClick("Personal")) }
                )
                ProfileMenuItem(
                    icon = Icons.Outlined.Notifications,
                    title = "Notifications",
                    onClick = { onEvent(MoreEvent.OnMenuClick("Notifications")) }
                )
                ProfileMenuItem(
                    icon = Icons.Outlined.Language,
                    title = "App Language",
                    value = "English",
                    onClick = { onEvent(MoreEvent.OnMenuClick("Language")) }
                )

                Spacer(modifier = Modifier.height(24.dp))

                SettingsGroupTitle("Support & About")
                ProfileMenuItem(
                    icon = Icons.Outlined.HelpOutline,
                    title = "Help Center",
                    onClick = { onEvent(MoreEvent.OnMenuClick("Help")) }
                )
                ProfileMenuItem(
                    icon = Icons.Outlined.PrivacyTip,
                    title = "Privacy Policy",
                    onClick = { onEvent(MoreEvent.OnMenuClick("Privacy")) }
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { onEvent(MoreEvent.OnLogout) },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Logout,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Log Out",
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Version 1.0.0", // Bạn có thể thêm biến appVersion vào MoreState nếu muốn
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }
    }
}
