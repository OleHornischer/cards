package com.onit.cards.service

import com.onit.cards.config.Constants
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service
class JwtUserDetailsService : UserDetailsService {

    @Value("\${default.app.user}")
    private val defaultAppUser: String? = null

    @Value("\${default.app.user.password}")
    private val defaultAppUserPassword: String? = null

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Autowired
    lateinit var passwordEncoder: PasswordEncoder

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        return if (null != defaultAppUser && defaultAppUser == username) {
            User(defaultAppUser, passwordEncoder.encode(defaultAppUserPassword), Collections.emptyList())
        } else {
            throw UsernameNotFoundException("User not found with username: $username")
        }
    }
}