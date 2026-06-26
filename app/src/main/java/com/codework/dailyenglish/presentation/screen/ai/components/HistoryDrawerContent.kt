package com.codework.dailyenglish.presentation.screen.ai.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.codework.dailyenglish.domain.ai.model.ChatSession
import com.codework.dailyenglish.presentation.theme.AppColors

@Composable
fun HistoryDrawerContent(sessions: List<ChatSession>, onNewChatClick: () -> Unit, onSessionClick: (String) -> Unit) {
    ModalDrawerSheet(drawerContainerColor = AppColors.AppBackground) {
        Column(modifier = Modifier.padding(20.dp).fillMaxHeight()) {
            Text("Chat History", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black, color = Color(0xFF263238))
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onNewChatClick, modifier = Modifier.fillMaxWidth().background(brush = AppColors.MainGradient, shape = RoundedCornerShape(12.dp)), colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent), contentPadding = PaddingValues(0.dp)) {
                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Add, null, tint = Color.White)
                    Text("New Conversation", modifier = Modifier.padding(start = 8.dp), color = Color.White)
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            LazyColumn {
                items(sessions) { session ->
                    NavigationDrawerItem(label = { Text(session.title, maxLines = 1) }, selected = false, onClick = { onSessionClick(session.id) }, icon = { Icon(Icons.Default.History, null, tint = Color.Gray) }, shape = RoundedCornerShape(12.dp), colors = NavigationDrawerItemDefaults.colors(unselectedContainerColor = Color.White), modifier = Modifier.padding(vertical = 4.dp))
                }
            }
        }
    }
}