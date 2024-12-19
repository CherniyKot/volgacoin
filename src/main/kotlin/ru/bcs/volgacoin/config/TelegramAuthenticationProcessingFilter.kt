package ru.bcs.volgacoin.config

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import org.springframework.security.web.util.matcher.RequestMatcher

class TelegramAuthenticationProcessingFilter : AbstractAuthenticationProcessingFilter(RequestMatcher { true }) {

    override fun attemptAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse
    ): Authentication {
        val authHeader = request.getHeader("Authorization") ?: throw RuntimeException("No authorization header")
        if (!authHeader.startsWith("tma ")) {
            throw RuntimeException("Invalid authorization header")
        }
        return authenticationManager.authenticate(TelegramAuth())
    }

    override fun successfulAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain,
        authResult: Authentication
    ) {
        TODO("Not yet implemented")
    }
}