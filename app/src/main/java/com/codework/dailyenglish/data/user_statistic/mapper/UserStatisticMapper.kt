package com.codework.dailyenglish.data.user_statistic.mapper

import com.codework.dailyenglish.data.user_statistic.remote.dto.SyncTimeRequestDto
import com.codework.dailyenglish.data.user_statistic.remote.dto.WeeklyStudyStatsDto
import com.codework.dailyenglish.presentation.screen.home.model.DailyStudyTime
import java.time.LocalDate

fun Int.toSyncTimeDto() = SyncTimeRequestDto(
    seconds = this
)

fun WeeklyStudyStatsDto.toDomain(): DailyStudyTime {
    val localDate = LocalDate.parse(this.date)

    val dayNameFormatted = localDate.dayOfWeek.name
        .lowercase()
        .replaceFirstChar { it.uppercase() }
        .take(3)

    val minutes = when {
        secondsStudied >= 60 -> secondsStudied / 60
        secondsStudied >= 30 -> 1
        else -> 0
    }

    return DailyStudyTime(
        dayName = dayNameFormatted,
        minutes = minutes,
        isToday = this.isToday
    )
}