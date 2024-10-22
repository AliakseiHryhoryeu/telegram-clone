package com.example.telegramclone.dto

import com.example.telegramclone.models.User

data class CreateUserResponse(
    val user: User,
    val jwt: String
)
