package ru.bcs.volgacoin.config

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import ru.bcs.volgacoin.service.TelegramInitData
import ru.bcs.volgacoin.service.TelegramInitDataService

class TelegramAuthenticationProcessingFilter(
    authenticationManager: AuthenticationManager,
    private val telegramInitDataService: TelegramInitDataService
) : BasicAuthenticationFilter(authenticationManager) {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        val authHeader = request.getHeader("Authorization")
        if (authHeader == null || !authHeader.startsWith("tma ")) {
            chain.doFilter(request, response)
            return
        }
        val principal = TelegramInitData.from(authHeader.removePrefix("tma "))
        if (!telegramInitDataService.isValid(principal) || !telegramInitDataService.isFresh(principal)) {
            chain.doFilter(request, response)
            return
        }

        val authentication = UsernamePasswordAuthenticationToken.authenticated(
            principal, null, listOf(
                SimpleGrantedAuthority("ROLE_USER")
            )
        )
        SecurityContextHolder.getContext().authentication = authentication

        chain.doFilter(request, response)
    }
}