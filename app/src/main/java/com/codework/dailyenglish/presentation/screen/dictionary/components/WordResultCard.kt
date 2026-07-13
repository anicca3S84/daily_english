package com.codework.dailyenglish.presentation.screen.dictionary.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.BookmarkBorder
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codework.dailyenglish.domain.dictionary.model.Word

private data class CardTheme(
    val primaryColor: Color,
    val badgeBgColor: Color,
    val badgeTextColor: Color,
    val audioIconColor: Color
)

private val cardThemes = listOf(
    CardTheme(
        primaryColor = Color(0xFF6FCF97),      // Green pastel
        badgeBgColor = Color(0xFFF0FBF4),
        badgeTextColor = Color(0xFF2F855A),
        audioIconColor = Color(0xFF6FCF97)
    ),
    CardTheme(
        primaryColor = Color(0xFF7CB8F5),      // Blue pastel
        badgeBgColor = Color(0xFFF2F8FF),
        badgeTextColor = Color(0xFF356AC3),
        audioIconColor = Color(0xFF7CB8F5)
    ),
    CardTheme(
        primaryColor = Color(0xFFA78BFA),      // Purple pastel
        badgeBgColor = Color(0xFFF7F5FF),
        badgeTextColor = Color(0xFF6B46C1),
        audioIconColor = Color(0xFFA78BFA)
    ),
    CardTheme(
        primaryColor = Color(0xFFF6C667),      // Soft Orange
        badgeBgColor = Color(0xFFFFF8E8),
        badgeTextColor = Color(0xFFC57A00),
        audioIconColor = Color(0xFFF6C667)
    ),
    CardTheme(
        primaryColor = Color(0xFFF59BB8),      // Pink pastel
        badgeBgColor = Color(0xFFFFF5F8),
        badgeTextColor = Color(0xFFC2417A),
        audioIconColor = Color(0xFFF59BB8)
    )
)

@Composable
fun WordResultCard(
    word: Word,
    index: Int = 0,
    isSavedMode: Boolean = false,
    isSelected: Boolean = false,
    onClick: () -> Unit = {},
    onPlayAudio: () -> Unit = {},
    onPlayAudioUk: () -> Unit = onPlayAudio,
    onPlayAudioUs: () -> Unit = onPlayAudio,
    onToggleSave: () -> Unit = {}
) {
    val theme = cardThemes[index % cardThemes.size]

    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) theme.badgeBgColor else Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 2.dp else 1.dp),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected) theme.primaryColor else Color(0xFFE2E8F0)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() }
    )
       {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .fillMaxHeight()
                    .clip(
                        RoundedCornerShape(
                            topStart = 16.dp,
                            bottomStart = 16.dp
                        )
                    )
                    .background(theme.primaryColor)
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 14.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = word.word,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1E293B),
                                fontSize = 15.sp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(
                                color = theme.badgeBgColor,
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = word.type,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = theme.badgeTextColor,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = word.usPhonetic.ifEmpty { word.ukPhonetic },
                                style = MaterialTheme.typography.bodyMedium,
                                fontSize = 12.sp,
                                color = Color(0xFF64748B)
                            )
                            Text(
                                text = "•",
                                color = Color(0xFFCBD5E1),
                                fontSize = 13.sp
                            )
                            Text(
                                text = word.meaning,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium,
                                fontSize = 12.sp,
                                color = Color(0xFF475569)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = Color.White,
                            border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                            modifier = Modifier.size(34.dp)
                        ) {
                            IconButton(
                                onClick = onPlayAudioUs,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.VolumeUp,
                                    contentDescription = "US Audio",
                                    tint = theme.audioIconColor,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }

                        Surface(
                            shape = CircleShape,
                            color = Color.White,
                            border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                            modifier = Modifier.size(34.dp)
                        ) {
                            IconButton(
                                onClick = onToggleSave,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Icon(
                                    imageVector = if (word.isSaved) Icons.Rounded.Bookmark else Icons.Rounded.BookmarkBorder,
                                    contentDescription = "Save Word",
                                    tint = if (word.isSaved) theme.primaryColor else Color(0xFF94A3B8),
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }

                val exampleEn = word.examples.getOrNull(0)
                if (!exampleEn.isNullOrBlank()) {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 10.dp),
                        thickness = 1.dp,
                        color = Color(0xFFF1F5F9)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top
                    ) {
                        Surface(
                            color = theme.badgeBgColor,
                            shape = RoundedCornerShape(4.dp),
                            modifier = Modifier.padding(top = 2.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Star,
                                    contentDescription = null,
                                    tint = theme.badgeTextColor,
                                    modifier = Modifier.size(10.dp)
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = "Ví dụ",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = theme.badgeTextColor,
                                    fontSize = 10.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Text(
                                text = exampleEn,
                                style = MaterialTheme.typography.bodyMedium,
                                fontSize = 11.sp,
                                color = Color(0xFF1E293B),
                                lineHeight = 18.sp
                            )

//                            val exampleVi = word.examples.getOrNull(1)
//                            if (!exampleVi.isNullOrBlank()) {
//                                Text(
//                                    text = exampleVi,
//                                    style = MaterialTheme.typography.bodySmall,
//                                    color = Color(0xFF64748B),
//                                    lineHeight = 16.sp
//                                )
//                            }
                        }
                    }
                }
            }
        }
    }
}