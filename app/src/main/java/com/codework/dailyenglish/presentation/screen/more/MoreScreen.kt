package com.codework.dailyenglish.presentation.screen.more

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.codework.dailyenglish.presentation.screen.more.components.ProfileHeaderSection
import com.codework.dailyenglish.presentation.screen.more.components.ProfileMenuItem
import com.codework.dailyenglish.presentation.screen.more.components.SettingsGroupTitle
import com.codework.dailyenglish.presentation.screen.more.mvi.MoreEvent
import com.codework.dailyenglish.presentation.screen.more.mvi.MoreState

val BluePrimary = Color(0xFF2196F3)
val BackgroundColor = Color(0xFFF7F8FC) // Màu nền tổng thể hơi sáng giống ảnh

@Composable
fun MoreScreen(
    state: MoreState,
    onEvent: (MoreEvent) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            ProfileHeaderSection(
                data = state,
                onSettingsClick = { /* Thêm event Setting nếu cần */ }
            )
        }
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {

                // GAMIFICATION SECTION
                SettingsGroupTitle("Gamification")
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(vertical = 8.dp)) {
                        ProfileMenuItem(
                            icon = Icons.Filled.EmojiEvents,
                            title = "Leaderboard",
                            subtitle = "See how you rank and compete\nwith other learners",
                            onClick = { onEvent(MoreEvent.OnClickLeaderboard) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // ACCOUNT SETTINGS SECTION
                SettingsGroupTitle("Account Settings")
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(vertical = 8.dp)) {
                        ProfileMenuItem(
                            icon = Icons.Filled.Person,
                            title = "Personal Information",
                            subtitle = "View and edit your profile details",
                            onClick = { onEvent(MoreEvent.OnMenuClick("Personal")) }
                        )
                        ProfileMenuItem(
                            icon = Icons.Filled.Language,
                            title = "App Language",
                            subtitle = "Choose your preferred language",
                            value = "English",
                            onClick = { onEvent(MoreEvent.OnMenuClick("Language")) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // SUPPORT & ABOUT SECTION
                SettingsGroupTitle("Support & About")
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(vertical = 8.dp)) {
                        ProfileMenuItem(
                            icon = Icons.Outlined.HelpOutline,
                            title = "Help Center",
                            subtitle = "Get help and find answers",
                            onClick = { onEvent(MoreEvent.OnMenuClick("Help")) }
                        )
                        ProfileMenuItem(
                            icon = Icons.Outlined.Info,
                            title = "About Daily English",
                            subtitle = "Version 1.0.0",
                            onClick = { onEvent(MoreEvent.OnMenuClick("About")) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = { onEvent(MoreEvent.OnLogout) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    ),
                    shape = RoundedCornerShape(16.dp)
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
            }
        }
    }
}