package com.codework.dailyenglish.presentation.screen.user

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.codework.dailyenglish.presentation.components.StateWrapper
import com.codework.dailyenglish.presentation.screen.user.mvi.UserInfoEvent
import com.codework.dailyenglish.presentation.screen.user.mvi.UserInfoState
import com.codework.dailyenglish.presentation.theme.AppColors
import com.codework.dailyenglish.presentation.theme.AppTextSizes
import com.codework.dailyenglish.presentation.utils.createTempPictureUri // Đảm bảo import hàm này

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserInfoScreen(
    state: UserInfoState,
    onEvent: (UserInfoEvent) -> Unit,
    onBackClick: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    // --- STATES CHO CHỌN ẢNH ---
    var showImagePickerOptions by remember { mutableStateOf(false) }
    var tempCameraUri by remember { mutableStateOf<Uri?>(null) }

    // --- LAUNCHERS ---
    // 1. Launcher cho Thư viện (Gallery)
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { onEvent(UserInfoEvent.OnAvatarSelected(it)) }
    }

    // 2. Launcher cho Chụp ảnh (Camera)
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            tempCameraUri?.let { onEvent(UserInfoEvent.OnAvatarSelected(it)) }
        }
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Personal Information",
                        fontWeight = FontWeight.Bold,
                        fontSize = AppTextSizes.TextSizeTitle,
                        color = AppColors.TextMain
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = AppColors.TextMain
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        }
    ) { paddingValues ->
        StateWrapper(
            uiState = state.userInfoState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) { profile ->
            profile?.let { data ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // --- Avatar Section ---
                    AvatarSection(
                        avatarUrl = state.editedAvatarUrl,
                        onAvatarClick = {
                            focusManager.clearFocus()
                            showImagePickerOptions = true // Mở Dialog
                        }
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // --- Full Name TextField ---
                    OutlinedTextField(
                        value = state.editedFullName,
                        onValueChange = { onEvent(UserInfoEvent.OnFullNameChanged(it)) },
                        label = { Text("Full Name") },
                        leadingIcon = { Icon(Icons.Filled.Person, contentDescription = null, tint = AppColors.TextMain) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // --- Email (Read Only) ---
                    OutlinedTextField(
                        value = data.email,
                        onValueChange = {},
                        label = { Text("Email Address") },
                        leadingIcon = { Icon(Icons.Filled.Email, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = false
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // --- Premium Status Card ---
                    PremiumStatusCard(isPremium = data.isPremium, expiresAt = data.premiumExpiresAt)

                    Spacer(modifier = Modifier.weight(1f))

                    // --- Save Button ---
                    Button(
                        onClick = {
                            focusManager.clearFocus()
                            onEvent(UserInfoEvent.OnSaveProfile)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !state.isUpdating,
                        colors = ButtonDefaults.buttonColors(containerColor = AppColors.BluePrimary)
                    ) {
                        if (state.isUpdating) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                        } else {
                            Text("Save Changes", fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }
        }
    }

    // --- DIALOG CHỌN NGUỒN ẢNH ---
    if (showImagePickerOptions) {
        AlertDialog(
            onDismissRequest = { showImagePickerOptions = false },
            title = { Text(text = "Update Avatar") },
            text = { Text(text = "Choose an option to update your profile picture.") },
            confirmButton = {
                TextButton(onClick = {
                    showImagePickerOptions = false
                    // Tạo Uri và mở Camera
                    val newUri = context.createTempPictureUri()
                    tempCameraUri = newUri
                    cameraLauncher.launch(newUri)
                }) {
                    Text("Take Photo")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showImagePickerOptions = false
                    // Mở Thư viện
                    galleryLauncher.launch("image/*")
                }) {
                    Text("Choose Gallery")
                }
            }
        )
    }
}

@Composable
private fun AvatarSection(avatarUrl: String, onAvatarClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(100.dp)
            .clickable { onAvatarClick() },
        contentAlignment = Alignment.BottomEnd
    ) {
        AsyncImage(
            model = avatarUrl.ifBlank { "https://via.placeholder.com/150" },
            contentDescription = "User Avatar",
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(AppColors.BluePrimary)
                .padding(6.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Filled.CameraAlt, null, tint = Color.White, modifier = Modifier.size(16.dp))
        }
    }
}

@Composable
private fun PremiumStatusCard(isPremium: Boolean, expiresAt: String?) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, if (isPremium) Color(0xFFFFD700) else MaterialTheme.colorScheme.outlineVariant),
        colors = CardDefaults.cardColors(containerColor = if (isPremium) Color(0xFFFFFDF0) else Color(0xFFEFF6FF))
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Filled.Star, null, tint = if (isPremium) Color(0xFFFFA500) else AppColors.BluePrimary)
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(if (isPremium) "Premium Member" else "Standard Account", fontWeight = FontWeight.Bold)
                if (isPremium && expiresAt != null) {
                    Text("Expires at: $expiresAt", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                }
            }
        }
    }
}