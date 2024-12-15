package ru.bcs.volgacoin.service

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class TelegramInitDataServiceTest {

    private val botToken = "5768337691:AAGDAe6rjxu1cUgxK4BizYi--Utc3J9v5AU"

    private val rawData ="{\"user\":\"{\\\"id\\\":279058397,\\\"first_name\\\":\\\"Vladislav\\\",\\\"last_name\\\":\\\"Kibenko\\\",\\\"username\\\":\\\"vdkfrost\\\",\\\"language_code\\\":\\\"en\\\",\\\"is_premium\\\":true,\\\"allows_write_to_pm\\\":true}\",\"chat_instance\":\"-3788475317572404878\",\"chat_type\":\"private\",\"auth_date\":\"1709144340\",\"hash\":\"371697738012ebd26a111ace4aff23ee265596cd64026c8c3677956a85ca1827\"}"

    private val telegramInitDataService = TelegramInitDataService(botToken)

    @Test
    fun testInitDataValidation() {
        val data = TelegramInitData.from(rawData)

        assertTrue(telegramInitDataService.isValid(rawData))
        assertTrue(telegramInitDataService.isValid(data))

        assertFalse(telegramInitDataService.isValid(data.copy(hash = "Wrong hash")))
    }
}