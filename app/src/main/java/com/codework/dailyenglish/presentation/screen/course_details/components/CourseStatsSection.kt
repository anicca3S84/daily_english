package com.codework.dailyenglish.presentation.screen.course_details.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import com.codework.dailyenglish.R
import com.codework.dailyenglish.presentation.screen.course_details.mvi.CourseDetailState
import com.codework.dailyenglish.presentation.theme.AppColors
import com.codework.dailyenglish.presentation.theme.Spaces

@Composable
fun CourseStatsSection(state: CourseDetailState) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Spaces.Space12)
    ) {
        StatChip(
            modifier = Modifier.weight(1f),
            icon = Icons.Default.Schedule,
            iconColor = AppColors.BlueMain,
            title = stringResource(R.string.label_duration),
            value = state.duration
        )
        StatChip(
            modifier = Modifier.weight(1f),
            icon = Icons.Default.BarChart,
            iconColor = AppColors.OrangeMain,
            title = stringResource(R.string.label_level),
            value = state.cefr
        )
        StatChip(
            modifier = Modifier.weight(1f),
            icon = Icons.Default.WorkspacePremium,
            iconColor = AppColors.PurpleMain,
            title = stringResource(R.string.label_lessons),
            value = pluralStringResource(
                id = R.plurals.label_lessons_count,
                count = state.totalLessons,
                state.totalLessons
            )
        )
    }
}