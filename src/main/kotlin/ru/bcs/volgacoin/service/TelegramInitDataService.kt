package ru.bcs.volgacoin.service

import org.apache.commons.codec.digest.HmacAlgorithms
import org.apache.commons.codec.digest.HmacUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.LocalDateTime


@Service
class TelegramInitDataService(
    @Value("\${telegram.bot.token}")
    val botToken: String
) {
    fun isValid(data: TelegramInitData): Boolean {
        val hash1 = HmacUtils
            .getInitializedMac(HmacAlgorithms.HMAC_SHA_256, "WebAppData".toByteArray())
            .doFinal(botToken.toByteArray())

        val hash2 = HmacUtils
            .getInitializedMac(HmacAlgorithms.HMAC_SHA_256, hash1)
            .doFinal(
                data.data.entries.toMutableList()
                    .sortedBy { it.key }
                    .joinToString(separator = "\n") { "${it.key}=${it.value}" }
                    .toByteArray()
            )

        @OptIn(ExperimentalStdlibApi::class)
        return data.hash == hash2.toHexString()
    }

    fun isValid(data: String) = isValid(TelegramInitData.from(data))

    fun isFresh(data: TelegramInitData): Boolean {
        return data.data["auth_date"]?.let { LocalDateTime.parse(it) > (LocalDateTime.now().minusSeconds(1)) } ?: false
    }
}