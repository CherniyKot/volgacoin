package ru.bcs.volgacoin.config

import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import ru.bcs.volgacoin.service.TelegramInitData
import org.springframework.security.authentication.AbstractAuthenticationToken

data class TelegramAuth(val initData: TelegramInitData): Authentication {
    override fun getName()=initData.userData.username

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        TODO("Not yet implemented")
    }

    override fun getCredentials(): Any {
        TODO("Not yet implemented")
    }

    override fun getDetails(): Any {
        TODO("Not yet implemented")
    }

    override fun getPrincipal(): Any {
        TODO("Not yet implemented")
    }

    override fun isAuthenticated(): Boolean {
        TODO("Not yet implemented")
    }

    override fun setAuthenticated(isAuthenticated: Boolean) {
        TODO("Not yet implemented")
    }
}