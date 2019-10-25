package com.onit.cards.service

import com.onit.cards.authentication.JwtRequest
import com.onit.cards.authentication.JwtResponse
import com.onit.cards.authentication.JwtTokenUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.DisabledException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController


@RestController
@CrossOrigin
class JwtAuthenticationController {

    @Autowired
    lateinit var authenticationManager: AuthenticationManager

    @Autowired
    lateinit var jwtTokenUtil: JwtTokenUtil

    @Autowired
    lateinit var userDetailsService: JwtUserDetailsService

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @PostMapping("/authenticate")
    fun createAuthenticationToken(@RequestBody authenticationRequest: JwtRequest): ResponseEntity<*> {

        authenticate(authenticationRequest.username, authenticationRequest.password)

        val userDetails = userDetailsService.loadUserByUsername(authenticationRequest.username)

        val token = jwtTokenUtil.generateToken(userDetails)

        return ResponseEntity.ok(JwtResponse(token))
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Throws(Exception::class)
    private fun authenticate(username: String, password: String) {
        try {
            authenticationManager.authenticate(UsernamePasswordAuthenticationToken(username, password))
        } catch (e: DisabledException) {
            throw Exception("USER_DISABLED", e)
        } catch (e: BadCredentialsException) {
            throw Exception("INVALID_CREDENTIALS", e)
        }

    }
}