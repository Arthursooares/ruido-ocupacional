package edu.curso.projetos.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.Date

@Component
class JwtUtils(@Value("\${app.jwt.secret}") segredo: String) {
    private val chave = Keys.hmacShaKeyFor(segredo.toByteArray(Charsets.UTF_8))

    fun generateToken(email: String): String {
        return Jwts.builder()
            .subject(email)
            .issuedAt(Date())
            .expiration(Date(System.currentTimeMillis() + 3_600_000))
            .signWith(chave)
            .compact()
    }

    fun getUsernameFromToken(token: String): String? {
        return try {
            Jwts.parser().verifyWith(chave).build().parseSignedClaims(token).payload.subject
        } catch (_: JwtException) {
            null
        } catch (_: IllegalArgumentException) {
            null
        }
    }
}
