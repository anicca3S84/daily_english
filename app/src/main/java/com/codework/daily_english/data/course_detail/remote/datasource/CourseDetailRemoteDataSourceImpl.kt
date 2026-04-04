package com.codework.daily_english.data.course_detail.remote.datasource

import com.codework.daily_english.core.network.AppResult
import com.codework.daily_english.data.course_detail.remote.CourseDetailApi
import com.codework.daily_english.data.course_detail.remote.dto.CourseDetailDto
import com.codework.daily_english.data.course_detail.remote.dto.LessonDto
import com.codework.daily_english.data.course_detail.remote.dto.LessonSkillDto
import com.codework.daily_english.data.course_detail.remote.dto.ModuleDto
import kotlinx.coroutines.delay
import javax.inject.Inject

class CourseDetailRemoteDataSourceImpl @Inject constructor(
    private val api: CourseDetailApi
) : CourseDetailRemoteDataSource {

    override suspend fun getCourseDetail(courseId: String): AppResult<CourseDetailDto> {
        // Tạm thời comment API thực tế
        // return safeApiCall { api.getCourseDetail(courseId) }.unwrap()

        delay(1200)

        // Fake Data bây giờ là DTO (String thay vì Enum)
        val fakeDto = CourseDetailDto(
            courseTitle = "Rookie - A1 Beginner",
            overallProgress = 0.48f,
            totalLessons = 25,
            completedLessons = 12,
            modules = listOf(
                ModuleDto(
                    title = "Unit 1: Getting Started",
                    description = "Làm quen với những câu giao tiếp vỡ lòng",
                    lessons = listOf(
                        LessonDto("1", "Greetings", "Chào hỏi cơ bản", "COMPLETED", 1f, 50, listOf(LessonSkillDto("VOCAB", true), LessonSkillDto("GRAM", true))),
                        LessonDto("2", "Introducing Myself", "Giới thiệu bản thân", "COMPLETED", 0.5f, 50, listOf(LessonSkillDto("VOCAB", true), LessonSkillDto("GRAM", true), LessonSkillDto("LISTEN", true)))
                    )
                ),
                ModuleDto(
                    title = "Unit 2: Family & Friends",
                    description = "Từ vựng về người thân và các mối quan hệ",
                    lessons = listOf(
                        LessonDto("3", "Family", "Từ vựng về gia đình", "LOCKED", 0f, 50, listOf(LessonSkillDto("LISTEN", true), LessonSkillDto("VOCAB", false), LessonSkillDto("GRAM", false))),
                        LessonDto("4", "Daily Routine", "Luyện nghe thói quen", "LOCKED", 0f, 50, listOf(LessonSkillDto("LISTEN", true))),
                        LessonDto("22", "Daily Routine", "Luyện nghe thói quen", "LOCKED", 0f, 50, listOf(LessonSkillDto("LISTEN", false))),
                        LessonDto("23", "Daily Routine", "Luyện nghe thói quen", "LOCKED", 0f, 50, listOf(LessonSkillDto("LISTEN", false)))
                    )
                ),

                ModuleDto(
                    title = "Unit 2: Family & Friends",
                    description = "Từ vựng về người thân và các mối quan hệ",
                    lessons = listOf(
                        LessonDto("5", "Family", "Từ vựng về gia đình", "LOCKED", 0f, 50, listOf(LessonSkillDto("LISTEN", false), LessonSkillDto("VOCAB", false), LessonSkillDto("GRAM", false))),
                        LessonDto("6", "Daily Routine", "Luyện nghe thói quen", "LOCKED", 0f, 50, listOf(LessonSkillDto("LISTEN", false)))
                    )
                ),
                ModuleDto(
                    title = "Unit 2: Family & Friends",
                    description = "Từ vựng về người thân và các mối quan hệ",
                    lessons = listOf(
                        LessonDto("7", "Family", "Từ vựng về gia đình", "LOCKED", 0f, 50, listOf(LessonSkillDto("LISTEN", false), LessonSkillDto("VOCAB", false), LessonSkillDto("GRAM", false))),
                        LessonDto("8", "Daily Routine", "Luyện nghe thói quen", "LOCKED", 0f, 50, listOf(LessonSkillDto("LISTEN", false)))
                    )
                ),

                ModuleDto(
                    title = "Unit 2: Family & Friends",
                    description = "Từ vựng về người thân và các mối quan hệ",
                    lessons = listOf(
                        LessonDto("9", "Family", "Từ vựng về gia đình", "LOCKED", 0f, 50, listOf(LessonSkillDto("LISTEN", false), LessonSkillDto("VOCAB", false), LessonSkillDto("GRAM", false))),
                        LessonDto("10", "Daily Routine", "Luyện nghe thói quen", "LOCKED", 0f, 50, listOf(LessonSkillDto("LISTEN", false)))
                    )
                )


            )
        )
        return AppResult.Success(fakeDto)
    }
}