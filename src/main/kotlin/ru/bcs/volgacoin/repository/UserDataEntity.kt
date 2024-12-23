package ru.bcs.volgacoin.repository

import java.time.LocalDateTime

data class UserDataEntity(
    val id: Long,
    val username: String,
    val clicks: Long,
    val energy: Double,
    val last_login: LocalDateTime
)
