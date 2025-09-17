package org.example.taskmanager.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.Claims
import io.jsonwebtoken.SignatureAlgorithm
import org.example.taskmanager.model.Role
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*

@Component
class JwtUtil(
    @Value("\${jwt.secret}") private val secret: String,
    @Value("\${jwt.expiration-ms}") private val expirationMs: Long
) {
    private val key: Key = Keys.hmacShaKeyFor(secret.toByteArray(Charsets.UTF_8))

    fun generateToken(username: String, roles: Collection<Role>): String {
        val now = Date()
        val expiry = Date(now.time + expirationMs)
        val rolesStr = roles.joinToString(",") { it.name }
        return Jwts.builder()
            .setSubject(username)
            .claim("roles", rolesStr)
            .setIssuedAt(now)
            .setExpiration(expiry)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()
    }

    fun validateToken(token: String): Boolean {
        return try {
            val claims = getAllClaimsFromToken(token)
            val exp = claims.expiration
            exp.after(Date())
        } catch (ex: Exception) {
            false
        }
    }

    fun getUsernameFromToken(token: String): String {
        return getAllClaimsFromToken(token).subject
    }

    fun getRolesFromToken(token: String): List<String> {
        val claims = getAllClaimsFromToken(token)
        val rolesObj = claims["roles"] ?: return emptyList()
        return rolesObj.toString().split(",").map { it.trim() }.filter { it.isNotEmpty() }
    }

    private fun getAllClaimsFromToken(token: String): Claims {
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .body
    }
}