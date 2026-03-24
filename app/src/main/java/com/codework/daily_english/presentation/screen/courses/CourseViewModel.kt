package com.codework.daily_english.presentation.screen.courses

import androidx.lifecycle.viewModelScope
import com.codework.daily_english.core.base.BaseViewModel
import com.codework.daily_english.presentation.screen.courses.model.TopicItem
import com.codework.daily_english.presentation.screen.courses.mvi.CourseEffect
import com.codework.daily_english.presentation.screen.courses.mvi.CourseEvent
import com.codework.daily_english.presentation.screen.courses.mvi.CourseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CourseViewModel @Inject constructor() :
    BaseViewModel<CourseState, CourseEvent, CourseEffect>(
        initialState = CourseState()
    ) {

    private val allLevels =
        listOf("Beginner (A1)", "Elementary (A2)", "Intermediate (B1)", "Advanced (B2)")

    init {
        loadCourseData(allLevels.first())
    }

    override fun onEvent(event: CourseEvent) {
        when (event) {
            is CourseEvent.OnLevelSelected -> loadCourseData(event.level)
            is CourseEvent.OnNavigateToLesson -> {
                // Bắn Effect kèm theo id để NavGraph bắt được
                sendEffect(CourseEffect.NavigateToLesson(event.lessonId))
            }
        }
    }

    private fun loadCourseData(level: String) {
        viewModelScope.launch {
            // 1. Chuyển sang Loading
//            _state.update {
//                BaseState.Loading
//            }


            val topics = mockTopicsForLevel(level)

            val data = CourseState(
                selectedLevel = level,
                availableLevels = allLevels,
                topics = topics
            )

//            _state.update { BaseState.Success(data) }
        }
    }

    fun mockTopicsForLevel(level: String): List<TopicItem> {
        return when {
            level.contains("A1") -> listOf(
                TopicItem(
                    "1",
                    "Greeting & Introduction",
                    "Learn how to say hello and introduce yourself.",
                    "https://picsum.photos/300/200",
                    5,
                    1.0f,
                    false
                ),
                TopicItem(
                    "1",
                    "Family & Friends",
                    "Learn how to say hello and introduce yourself.",
                    "https://picsum.photos/300/201",
                    5,
                    1.0f,
                    false
                ),
                TopicItem(
                    "1",
                    "Greeting & Introduction",
                    "Learn how to say hello and introduce yourself.",
                    "https://picsum.photos/300/200",
                    5,
                    0.8f,
                    false
                ),
                TopicItem(
                    "2",
                    "Family & Friends",
                    "Talk about your family members.",
                    "https://picsum.photos/300/201",
                    8,
                    0.4f,
                    false
                ),
                TopicItem(
                    "2",
                    "Family & Friends",
                    "Talk about your family members.",
                    "https://picsum.photos/300/201",
                    8,
                    0.4f,
                    false
                ),
                TopicItem(
                    "2",
                    "Family & Friends",
                    "Talk about your family members.",
                    "https://picsum.photos/300/201",
                    8,
                    0.4f,
                    false
                ),
                TopicItem(
                    "2",
                    "Family & Friends",
                    "Talk about your family members.",
                    "https://picsum.photos/300/201",
                    8,
                    0.4f,
                    false
                ),
                TopicItem(
                    "2",
                    "Family & Friends",
                    "Talk about your family members.",
                    "https://picsum.photos/300/201",
                    8,
                    0.4f,
                    false
                ),
            )

            level.contains("A2") -> listOf(
                TopicItem(
                    "4",
                    "Travel & Transport",
                    "Booking tickets and asking for directions.",
                    "https://picsum.photos/300/203",
                    10,
                    0.0f,
                    false
                ),
                TopicItem(
                    "5",
                    "Food & Dining",
                    "Ordering food in a restaurant.",
                    "https://picsum.photos/300/204",
                    7,
                    0.0f,
                    true
                )
            )

            else -> listOf(
                TopicItem(
                    "99",
                    "Coming Soon",
                    "Content is updating...",
                    "https://picsum.photos/300/205",
                    0,
                    0.0f,
                    true
                )
            )
        }
    }
}