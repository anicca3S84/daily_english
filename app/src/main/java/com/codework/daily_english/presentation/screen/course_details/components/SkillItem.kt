package com.codework.daily_english.presentation.screen.course_details.components

import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.codework.daily_english.presentation.screen.course_details.model.LessonSkillUi
import com.codework.daily_english.presentation.theme.Spaces
import com.codework.daily_english.presentation.theme.TextSizes


@Composable
fun SkillItem(
    skill: LessonSkillUi,
    isLessonLocked: Boolean
) {
    val isHighlight = !isLessonLocked && skill.isDone

    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = skill.type.icon,
            contentDescription = null,
            modifier = Modifier
                .size(Spaces.Space16)
                .alpha(if (isHighlight) 1f else 0.5f),
            tint = if (isHighlight) MaterialTheme.colorScheme.primary else Color.Gray
        )

        Spacer(modifier = Modifier.width(Spaces.Space4))

        Text(
            text = stringResource(skill.type.labelRes),
            fontSize = TextSizes.TextSize13,
            color = Color.Gray,
            fontWeight = if (isHighlight) FontWeight.Bold else FontWeight.Medium
        )
    }
}