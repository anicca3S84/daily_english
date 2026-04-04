package com.codework.daily_english.data.course.remote.datasource

import com.codework.daily_english.core.network.AppResult
import com.codework.daily_english.data.course.remote.CourseApi
import com.codework.daily_english.data.course.remote.dto.CourseDashboardDto
import com.codework.daily_english.data.course.remote.dto.CourseDto
import com.codework.daily_english.data.course.remote.dto.UserStatsDto
import jakarta.inject.Inject
import kotlinx.coroutines.delay
import java.util.UUID

class CourseRemoteDataSourceImpl @Inject constructor(
    private val api: CourseApi
) : CourseRemoteDataSource {

    override suspend fun getCourseDashboardData(): AppResult<CourseDashboardDto> {
        //real return
//        return safeApiCall { api.getCourseDashboardData() }.unwrap()

        delay(2000)

        val fakeStats = UserStatsDto(
            streak = 1365,
            totalExp = 1503,
            lessonsDone = 20,
            wordsLearned = 350
        )

        val fakeCourses = listOf(
            CourseDto(
                id = UUID.randomUUID().toString(),
                title = "Rookie",
                description = "A1 • Beginner",
                cefr = "A1",
                lessons = 25,
                progress = 0.6f,
                isLocked = false,
                code = "ROOKIE"
            ),
            CourseDto(
                id = UUID.randomUUID().toString(),
                title = "Explorer",
                description = "A2 • Elementary",
                cefr = "A2",
                lessons = 30,
                progress = 0.2f,
                isLocked = false,
                code = "EXPLORER"
            ),
            CourseDto(
                id = UUID.randomUUID().toString(),
                title = "Communicator",
                description = "B1 • Intermediate",
                cefr = "B1",
                lessons = 35,
                progress = 0.0f,
                isLocked = false,
                code = "COMMUNICATOR"
            ),
            CourseDto(
                id = UUID.randomUUID().toString(),
                title = "Fluent",
                description = "B2 • Proficient",
                cefr = "B2",
                lessons = 40,
                progress = 0.0f,
                isLocked = false,
                code = "FLUENT"
            ),
            CourseDto(
                id = UUID.randomUUID().toString(),
                title = "Master",
                description = "C1 • Advanced",
                cefr = "C1",
                lessons = 45,
                progress = 0.0f,
                isLocked = false,
                code = "MASTER"
            ),
            CourseDto(
                id = UUID.randomUUID().toString(),
                title = "Legendary",
                description = "C2 • Mastery",
                cefr = "C2",
                lessons = 45,
                progress = 0.0f,
                isLocked = false,
                code = "LEGENDARY"
            )
        )

        val fakeDashboardDto = CourseDashboardDto(
            userStats = fakeStats,
            courses = fakeCourses
        )

        return AppResult.Success(fakeDashboardDto)
    }
}