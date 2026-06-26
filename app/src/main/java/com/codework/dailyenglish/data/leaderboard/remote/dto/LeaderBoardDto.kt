package com.codework.dailyenglish.data.leaderboard.remote.dto

import com.google.gson.annotations.SerializedName

data class LeaderBoardDto(
    @SerializedName("userId") val userId: String,
    @SerializedName("fullName") val fullName: String?,
    @SerializedName("avatarUrl") val avatarUrl: String?,
    @SerializedName("totalXp") val totalXp: Int,
    @SerializedName("rank") val rank: Int,
    @SerializedName("currentUser") val isCurrentUser: Boolean
)