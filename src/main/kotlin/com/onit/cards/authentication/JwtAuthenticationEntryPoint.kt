package com.onit.cards.authentication

import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import java.io.IOException
import java.io.Serializable
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Component
class JwtAuthenticationEntryPoint : AuthenticationEntryPoint, Serializable {

    @Throws(IOException::class, ServletException::class)
    override fun commence(request: HttpServletRequest, response: HttpServletResponse, authException: AuthenticationException) {

        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized")
    }
}