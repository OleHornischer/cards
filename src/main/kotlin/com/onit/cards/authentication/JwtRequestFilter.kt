package com.onit.cards.authentication

import com.onit.cards.service.JwtUserDetailsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Component
class JwtRequestFilter : OncePerRequestFilter() {

    @Autowired
    lateinit var jwtUserDetailsService: JwtUserDetailsService

    @Autowired
    lateinit var jwtTokenUtil: JwtTokenUtil

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {

        val requestTokenHeader = request.getHeader("Authorization")

        // JWT Token is in the form "Bearer token". Remove Bearer word and get
        // only the Token
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            val jwtToken = requestTokenHeader.substring(7)
            val username = jwtTokenUtil.getUsernameFromToken(jwtToken)

            // Once we get the token validate it.
            username?.map { uname ->
                {
                    if (null == SecurityContextHolder.getContext().authentication) {

                        val userDetails = this.jwtUserDetailsService.loadUserByUsername(username)

                        // if token is valid configure Spring Security to manually set
                        // authentication
                        if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {

                            val usernamePasswordAuthenticationToken = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
                            usernamePasswordAuthenticationToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                            // After setting the Authentication in the context, we specify
                            // that the current user is authenticated. So it passes the
                            // Spring Security Configurations successfully.
                            SecurityContextHolder.getContext().authentication = usernamePasswordAuthenticationToken
                        }
                    }
                }
            } ?: logger.info("Missing or incorrect JWT token")

        }
        chain.doFilter(request, response)
    }
}