package com.codework.daily_english.presentation.model

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codework.daily_english.presentation.screen.lessons.speaking.ChatMessage
import com.codework.daily_english.presentation.screen.lessons.speaking.SpeakingEvent
import com.codework.daily_english.presentation.theme.BluePrimary
import com.codework.daily_english.presentation.theme.TextPrimary

@Composable
fun ChatBubble(message: ChatMessage, onEvent: (SpeakingEvent) -> Unit) {
    val isUser = message.isFromUser
    val displayText = if (message.isTranslated && message.translatedText != null) {
        message.translatedText
    } else {
        message.text
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Bottom
    ) {
        if (!isUser) {
            Surface(
                shape = CircleShape,
                color = BluePrimary.copy(alpha = 0.1f),
                modifier = Modifier.size(36.dp)
            ) {
                Icon(Icons.Default.Face, contentDescription = "AI", tint = BluePrimary, modifier = Modifier.padding(6.dp))
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Column(
            horizontalAlignment = if (isUser) Alignment.End else Alignment.Start,
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            Box(
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(
                            topStart = 20.dp,
                            topEnd = 20.dp,
                            bottomStart = if (isUser) 20.dp else 4.dp,
                            bottomEnd = if (isUser) 4.dp else 20.dp
                        )
                    )
                    .background(if (isUser) BluePrimary else Color(0xFFF1F5F9))
                    .clickable(enabled = !isUser && message.translatedText != null) {
                        onEvent(SpeakingEvent.OnToggleTranslate(message.id))
                    }
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                AnimatedContent(targetState = displayText, label = "text_translation") { text ->
                    Text(
                        text = text,
                        color = if (isUser) Color.White else TextPrimary,
                        fontSize = 16.sp,
                        lineHeight = 24.sp
                    )
                }
            }

            if (!isUser) {
                Spacer(modifier = Modifier.height(4.dp))
                Row(modifier = Modifier.padding(start = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { onEvent(SpeakingEvent.OnPlayAudio(message.id)) },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.VolumeUp, contentDescription = "Listen", tint = Color(0xFF94A3B8), modifier = Modifier.size(18.dp))
                    }
                    Spacer(modifier = Modifier.width(8.dp))

                    if (message.translatedText != null) {
                        IconButton(
                            onClick = { onEvent(SpeakingEvent.OnToggleTranslate(message.id)) },
                            modifier = Modifier.size(24.dp)
                        ) {
                            val iconTint = if (message.isTranslated) BluePrimary else Color(0xFF94A3B8)
                            Icon(Icons.Default.Translate, contentDescription = "Translate", tint = iconTint, modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }
        }
    }
}