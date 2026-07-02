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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codework.dailyenglish.domain.dictionary.model.Word
import com.codework.dailyenglish.presentation.components.PhoneticGroup
import com.codework.dailyenglish.presentation.theme.AppColors

@Composable
fun WordResultCard(
    word: Word,
    onClick: () -> Unit,
    onPlayAudioUk: () -> Unit,
    onPlayAudioUs: () -> Unit,
    onToggleSave: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),

        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // --- 1. HEADER (Giữ nguyên) ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = word.word,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E293B)
                )

                Spacer(modifier = Modifier.width(10.dp))

                Surface(
                    color = AppColors.BluePrimary,
                    shape = RoundedCornerShape(6.dp),
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = word.type,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- 2. AUDIO SECTION (Giữ nguyên) ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                PhoneticGroup(
                    label = "UK",
                    phonetic = word.ukPhonetic,
                    mainColor = Color(0xFFE91E63),
                    onClick = onPlayAudioUk
                )
                Spacer(modifier = Modifier.width(32.dp))
                PhoneticGroup(
                    label = "US",
                    phonetic = word.usPhonetic,
                    mainColor = Color(0xFF3B82F6),
                    onClick = onPlayAudioUs
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                thickness = 1.dp,
                color = Color(0xFFE2E8F0)
            )

            // --- 3. DEFINITION ---
            Text(
                text = word.meaning,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF334155),
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // --- 4. FOOTER: EXAMPLE & BOOKMARK ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom // Căn đáy để Bookmark nằm dưới
            ) {
                // SỬA: Dùng IntrinsicSize.Min để chiều cao Row phụ thuộc vào con cao nhất (Text)
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .height(IntrinsicSize.Min), // QUAN TRỌNG: Gạch sẽ co giãn theo text
                    verticalAlignment = Alignment.Top
                ) {
                    // Thanh gạch dọc
                    Box(
                        modifier = Modifier
                            .width(3.dp)
                            .fillMaxHeight() // Chiếm hết chiều cao của Text bên cạnh
                            .padding(vertical = 2.dp) // Thụt vào 1 xíu ở 2 đầu cho tinh tế
                            .background(AppColors.BluePrimary.copy(alpha = 0.5f), CircleShape)
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Text(
                            text = "Example",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = AppColors.BluePrimary,
                            fontSize = 11.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = word.examples[0],
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF64748B),
                            fontStyle = FontStyle.Italic
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Nút Bookmark
                IconButton(
                    onClick = onToggleSave,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = if (word.isSaved) Icons.Rounded.Bookmark else Icons.Rounded.BookmarkBorder,
                        contentDescription = null,
                        tint = if (word.isSaved) AppColors.BluePrimary else Color(0xFF94A3B8),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}