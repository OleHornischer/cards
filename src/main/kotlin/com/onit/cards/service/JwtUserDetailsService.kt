package com.onit.cards.service

import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import java.util.*

@Service
class JwtUserDetailsService : UserDetailsService {

    companion object {
        const val DEFAULT_APP_USER: String = "cardapp"
        const val DEFAULT_APP_USER_PASSWORD: String = "Rs<MB&m%R6VpAysHJ-~bL!wU.2^+X>[Fmy9X@cWH;G<^B5F74)@'Pq=V8:FV_y*%"
    }

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        return if (DEFAULT_APP_USER == username) {
            User(DEFAULT_APP_USER, DEFAULT_APP_USER_PASSWORD, Collections.emptyList())
        } else {
            throw UsernameNotFoundException("User not found with username: $username")
        }
    }
}