package ru.bcs.volgacoin.service

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
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

@JsonIgnoreProperties(ignoreUnknown = true)
data class UserData(
    @JsonProperty("allows_write_to_pm") val allowsWriteToPm: Boolean,
    @JsonProperty("first_name") val firstName: String,
    @JsonProperty("id") val id: Long,
    @JsonProperty("is_premium") val isPremium: Boolean,
    @JsonProperty("language_code") val languageCode: String,
    @JsonProperty("last_name") val lastName: String,
    @JsonProperty("username") val username: String
)