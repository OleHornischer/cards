package com.onit.cards.authentication

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.io.Serializable
import java.util.*


@Component
class JwtTokenUtil : Serializable {

    companion object {
        const val JWT_TOKEN_VALIDITY_MILLIS = (5 * 60 * 60 * 1000).toLong()
    }

    @Value("\${jwt.secret}")
    private val secret: String? = null

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun getUsernameFromToken(token: String): String? = getClaimFromToken(token, Claims::getSubject)

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun getExpirationDateFromToken(token: String): Date? = getClaimFromToken(token, Claims::getExpiration)

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun <T> getClaimFromToken(token: String, claimsResolver: (Claims) -> T): T? = claimsResolver.invoke(getAllClaimsFromToken(token))

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private fun getAllClaimsFromToken(token: String): Claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).body

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private fun isTokenExpired(token: String): Boolean = getExpirationDateFromToken(token)?.before(Date())?:true

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun generateToken(userDetails: UserDetails): String = doGenerateToken(HashMap(), userDetails.username)

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //while creating the token -
    //1. Define  claims of the token, like Issuer, Expiration, Subject, and the ID
    //2. Sign the JWT using the HS512 algorithm and secret key.
    //3. According to JWS Compact Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
    //   compaction of the JWT to a URL-safe string
    private fun doGenerateToken(claims: Map<String, Any>, subject: String): String =
            Jwts.builder().setClaims(claims)
                    .setSubject(subject)
                    .setIssuedAt(Date(System.currentTimeMillis()))
                    .setExpiration(Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY_MILLIS))
                    .signWith(SignatureAlgorithm.HS512, secret).compact()

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun validateToken(token: String, userDetails: UserDetails): Boolean =
            getUsernameFromToken(token) == userDetails.username
                    && !isTokenExpired(token)

}