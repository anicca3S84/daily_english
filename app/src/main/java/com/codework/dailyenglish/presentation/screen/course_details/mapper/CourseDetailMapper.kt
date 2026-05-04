package com.codework.dailyenglish.presentation.screen.course_details.mapper

import com.codework.dailyenglish.domain.course_detail.model.ModuleDomain
import com.codework.dailyenglish.presentation.screen.course_details.model.LessonSkillUi
import com.codework.dailyenglish.presentation.screen.course_details.model.LessonUi
import com.codework.dailyenglish.presentation.screen.course_details.model.ModuleUi

fun List<ModuleDomain>.toUiModels(): List<ModuleUi> {
    return this.map { module ->
        ModuleUi(
            title = module.title,
            description = module.description,
            lessons = module.lessons.map { lesson ->
                LessonUi(
                    id = lesson.id,
                    title = lesson.title,
                    description = lesson.description,
                    subTitle = lesson.subTitle,
                    status = lesson.status,
                    progress = lesson.progress,
                    expReward = lesson.expReward,
                    skills = lesson.skills.map { LessonSkillUi(it.type, it.isDone) }
                )
            }
        )
    }
}