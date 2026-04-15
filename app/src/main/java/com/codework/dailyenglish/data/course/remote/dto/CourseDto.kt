package com.codework.dailyenglish.data.course.remote.dto


import com.google.gson.annotations.SerializedName

data class CourseDto(
    @SerializedName("id")
    val id: String?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("cefr")
    val cefr: String?,
    @SerializedName("lessons")
    val lessons: Int?,
    @SerializedName("progress")
    val progress: Float?,
    @SerializedName("isLocked")
    val isLocked: Boolean?,
    @SerializedName("code")
    val code: String?
)