package com.codework.daily_english.domain.auth.model

data class User(
    val id: String,
    val email: String,
    val displayName: String,
    val avatarUrl: String,
    val isPremium: Boolean
)