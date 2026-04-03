package com.codework.daily_english.data.course.remote.dto


import com.google.gson.annotations.SerializedName

data class CourseDto(
    @SerializedName("public_id")
    val id: String?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("cefr")
    val cefr: String?,
    @SerializedName("total_lessons")
    val lessons: Int?,
    @SerializedName("progress")
    val progress: Float?,
    @SerializedName("is_locked")
    val isLocked: Boolean?,
    @SerializedName("icon_key")
    val code: String?
)