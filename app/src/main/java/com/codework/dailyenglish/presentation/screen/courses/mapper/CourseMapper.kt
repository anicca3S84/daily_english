package com.codework.dailyenglish.presentation.screen.courses.mapper
import com.codework.dailyenglish.domain.course.model.Course
import com.codework.dailyenglish.presentation.screen.courses.model.CourseUi
import com.codework.dailyenglish.presentation.theme.provider.CourseThemeProvider

fun Course.toUiModel(): CourseUi {

    val theme = CourseThemeProvider.getThemeByCode(this.code)

    return CourseUi(
        id = id,
        code = code,
        title = title,
        description = description,
        cefr = cefr,
        lessons = lessons,
        progress = progress,
        isLocked = isLocked,
        themeColor = theme,
        icon = theme.icon
    )
}

fun List<Course>.toUiModels(): List<CourseUi> {
    return this.map { it.toUiModel() }
}

