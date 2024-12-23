package ru.bcs.volgacoin.config

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import ru.bcs.volgacoin.service.TelegramInitData
import ru.bcs.volgacoin.service.TelegramInitDataService
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.Base64

class TelegramAuthenticationProcessingFilter(
    authenticationManager: AuthenticationManager,
    private val telegramInitDataService: TelegramInitDataService
) : BasicAuthenticationFilter(authenticationManager) {

    val log = LoggerFactory.getLogger(TelegramAuthenticationProcessingFilter::class.java)

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        val authHeader = request.getHeader("Authorization")
        if (authHeader == null || !authHeader.startsWith("tma ")) {
            chain.doFilter(request, response)
            return
        }
        log.debug("HEADER: {}", authHeader)
        val principal = TelegramInitData.from(String(Base64.getDecoder().decode(authHeader.removePrefix("tma "))))
        log.debug("Auth with data: {}", principal)
        log.debug(
            "Auth with timestamp: {}, now is: {}", principal.data["auth_date"]?.let {
                LocalDateTime.ofEpochSecond(
                    it.toLong(), 0, ZoneOffset.UTC
                )
            },
            LocalDateTime.now()
        )
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