package ru.itmo.vkusno_i_prosto.service

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*
import kotlin.collections.HashMap

@Component
class JwtService {

    val tokenTtl: Long = 1000 * 60 * 60 * 100 // 100 hours
    val secret: String = "superpupersecretkeydonttellanyone"

    fun generateToken(login: String, username: String): String {
        val claims: Map<String, Any> = mutableMapOf("username" to username)
        return Jwts.builder()
            .setClaims(claims)
            .setSubject(login)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + tokenTtl)) // 100 hours
            .signWith(getSigningKey())
            .compact()
    }

    fun validateToken(token: String, username: String): Boolean {
        val extractedUsername = extractLogin(token)
        return extractedUsername == username && !isTokenExpired(token)
    }

    fun extractLogin(token: String): String {
        return extractAllClaims(token).subject
    }

    private fun isTokenExpired(token: String): Boolean {
        return extractExpiration(token).before(Date())
    }

    private fun extractExpiration(token: String): Date {
        return extractAllClaims(token).expiration
    }

    private fun extractAllClaims(token: String): Claims {
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).body
    }

    private fun getSigningKey(): Key = Keys.hmacShaKeyFor(secret.toByteArray(Charsets.UTF_8))
}
