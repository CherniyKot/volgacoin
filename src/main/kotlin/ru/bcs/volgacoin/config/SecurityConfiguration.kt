package ru.bcs.volgacoin.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import ru.bcs.volgacoin.service.TelegramInitDataService


@Configuration
@EnableWebSecurity
class SecurityConfiguration {
    @Bean
    fun filterChain(http: HttpSecurity, authenticationManager: AuthenticationManager, telegramInitDataService: TelegramInitDataService): SecurityFilterChain {
        http
            .authorizeHttpRequests { auths ->
                auths.requestMatchers("/api/**").authenticated()
                auths.requestMatchers("/**").permitAll()
            }
            .addFilter(
                TelegramAuthenticationProcessingFilter(authenticationManager, telegramInitDataService)
            )
        return http.build()
    }

    @Bean
    fun authenticationManager(authenticationConfiguration: AuthenticationConfiguration): AuthenticationManager {
        return authenticationConfiguration.authenticationManager
    }
}