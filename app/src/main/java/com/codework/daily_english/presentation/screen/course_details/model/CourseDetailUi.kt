package com.codework.daily_english.presentation.screen.course_details.model

import com.codework.daily_english.R
import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Headphones
import androidx.compose.material.icons.rounded.MenuBook
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.ui.graphics.vector.ImageVector

enum class LessonStatus { IN_PROGRESS, LOCKED, COMPLETED }

enum class SkillType(
    @StringRes val labelRes: Int,
    val icon: ImageVector
) {
    LISTEN(R.string.label_listen, Icons.Rounded.Headphones),
    VOCAB(R.string.label_vocab, Icons.Rounded.MenuBook),
    GRAM(R.string.label_gram, Icons.Rounded.Edit),
    SPEAK(R.string.label_speak, Icons.Rounded.Mic)
}

data class LessonUi(
    val id: String,
    val title: String,
    val description: String,
    val status: LessonStatus,
    val progress: Float = 0f,
    val skills: List<LessonSkillUi> = emptyList(),
    val expReward: Int = 50,
)

data class ModuleUi(
    val title: String,
    val description: String,
    val lessons: List<LessonUi>
)

data class LessonSkillUi( val type: SkillType, val isDone: Boolean )


