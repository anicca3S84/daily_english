package com.codework.dailyenglish.data.auth.mapper

import com.codework.dailyenglish.data.auth.remote.dto.UserDto
import com.codework.dailyenglish.domain.auth.model.User

fun UserDto.toDomain(): User {
    return User(
        id = this.id ?: "",
        email = this.email ?: "",
        displayName = this.fullName ?: "Learner",
        avatarUrl = this.avatarUrl ?: "",
        fullName = this.fullName ?: "",
        isPremium = this.isPremium ?: false
    )
}