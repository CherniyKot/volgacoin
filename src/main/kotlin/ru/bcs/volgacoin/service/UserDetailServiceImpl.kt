package ru.bcs.volgacoin.service

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService

class UserDetailServiceImpl : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        TODO("Not yet implemented")
    }
}