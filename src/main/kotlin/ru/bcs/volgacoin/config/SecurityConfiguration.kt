package ru.bcs.volgacoin.config

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.Authentication
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import org.springframework.security.web.util.matcher.RequestMatcher


@Configuration
@EnableWebSecurity
class SecurityConfiguration {
    @Bean
    @Throws(Exception::class)
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .authorizeHttpRequests { auths ->
                auths.anyRequest().authenticated()
            }
            .addFilter(object : AbstractAuthenticationProcessingFilter(RequestMatcher { true }) {
                override fun attemptAuthentication(
                    request: HttpServletRequest?,
                    response: HttpServletResponse?
                ): Authentication {
                    TODO("Not yet implemented")
                }

                override fun successfulAuthentication(
                    request: HttpServletRequest?,
                    response: HttpServletResponse?,
                    chain: FilterChain?,
                    authResult: Authentication?
                ) {
                    super.successfulAuthentication(request, response, chain, authResult)
                    TODO("Not yet implemented")
                }
            })
            .authenticationProvider(TODO("Not yet implemented"))
        return http.build()
    }
}