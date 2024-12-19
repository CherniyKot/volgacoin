package ru.bcs.volgacoin.config

import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication

class CustomAuthenticationProvider : AuthenticationProvider {
    override fun authenticate(authentication: Authentication?): Authentication {
        authentication.get
    }

    override fun supports(authentication: Class<*>) = authentication.equals(TelegramAuth::class.java)
}