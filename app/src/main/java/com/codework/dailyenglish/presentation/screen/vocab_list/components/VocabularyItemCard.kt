package com.codework.dailyenglish.presentation.screen.vocab_list.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.BookmarkBorder
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.RadioButtonUnchecked
import androidx.compose.material.icons.rounded.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codework.dailyenglish.domain.dictionary.model.Word
import com.codework.dailyenglish.presentation.theme.AppColors

@Composable
fun VocabularyItemCard(
    word: Word,
    isSavedMode: Boolean,
    isSelected: Boolean,
    onSelectToggle: () -> Unit,
    onCardClick: () -> Unit, // Thêm callback này
    onPlayAudio: () -> Unit,
    onToggleSave: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCardClick() }, // Thay đổi ở đây: Click vào Card sẽ kích hoạt chi tiết từ
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFFF0F7FF) else Color.White
        ),
        border = BorderStroke(
            width = 1.dp,
            color = if (isSelected) AppColors.BluePrimary.copy(alpha = 0.5f) else Color(0xFFE2E8F0)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // KHU VỰC 1: Ô CHECKBOX CUSTOM TRÒN BÊN TRÁI
            Icon(
                imageVector = if (isSelected) Icons.Rounded.CheckCircle else Icons.Rounded.RadioButtonUnchecked,
                contentDescription = "Select status",
                tint = if (isSelected) AppColors.BluePrimary else Color(0xFFCBD5E1),
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onSelectToggle() } // Giữ nguyên ở đây: Click vào checkbox để chọn/bỏ chọn
            )

            Spacer(modifier = Modifier.width(14.dp))

            // KHU VỰC 2: NỘI DUNG TỪ VỰNG CHÍNH Ở GIỮA
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = word.word,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = AppColors.TextMain
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Rounded.VolumeUp,
                        contentDescription = "Play Audio",
                        tint = AppColors.BluePrimary,
                        modifier = Modifier
                            .size(20.dp)
                            .clickable { onPlayAudio() }
                    )
                }

                Text(
                    text = word.usPhonetic,
                    fontSize = 14.sp,
                    color = AppColors.BluePrimary,
                    modifier = Modifier.padding(top = 2.dp)
                )

                Text(
                    text = word.meaning,
                    fontSize = 15.sp,
                    color = Color(0xFF475569),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            // KHU VỰC 3: ACTION BUTTONS (NÚT SAVE / GỠ SAVE) BÊN PHẢI
            IconButton(onClick = onToggleSave) {
                Icon(
                    imageVector = if (isSavedMode || word.isSaved) Icons.Rounded.Bookmark else Icons.Rounded.BookmarkBorder,
                    contentDescription = "Save Action",
                    tint = if (isSavedMode || word.isSaved) AppColors.BluePrimary else Color(0xFF94A3B8),
                    modifier = Modifier.size(26.dp)
                )
            }
        }
    }
}