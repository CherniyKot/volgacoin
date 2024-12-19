package ru.bcs.volgacoin.service

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

data class TelegramInitData(
    val hash: String,
    val data: Map<String, String>
) {
    val userData: UserData = ObjectMapper().readValue(data.getOrDefault("user", "null"))

    companion object {
        fun from(value: String): TelegramInitData {
            val initData = ObjectMapper().readValue(value, object : TypeReference<HashMap<String, String>>() {})
            val hash = initData.get("hash") ?: throw Exception("No hash value provided")
            initData.remove("hash")
            return TelegramInitData(hash, initData)
        }
    }
}

data class UserData(
    val allows_write_to_pm: Boolean,
    val first_name: String,
    val id: Long,
    val is_premium: Boolean,
    val language_code: String,
    val last_name: String,
    val username: String
)