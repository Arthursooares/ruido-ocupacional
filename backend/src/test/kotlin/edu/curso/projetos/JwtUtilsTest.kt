package edu.curso.projetos

import edu.curso.projetos.security.JwtUtils
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import java.util.Date
import kotlin.test.*
import org.junit.jupiter.api.Test

class JwtUtilsTest {
    private val segredoTeste = "chave-exclusiva-dos-testes-unitarios-com-mais-de-32-bytes"
    private val jwtUtils = JwtUtils(segredoTeste)
    @Test fun tokenValidoIdentificaUsuario() {
        assertEquals("aluno@example.test", jwtUtils.getUsernameFromToken(jwtUtils.generateToken("aluno@example.test")))
    }
    @Test fun rejeitaAssinaturaDiferenteETokenMalformado() {
        val outroJwtUtils = JwtUtils("outra-chave-exclusiva-dos-testes-com-mais-de-32-bytes")
        assertNull(jwtUtils.getUsernameFromToken(outroJwtUtils.generateToken("intruso")))
        assertNull(jwtUtils.getUsernameFromToken("invalido"))
    }
    @Test fun rejeitaTokenExpirado() {
        val token = Jwts.builder().subject("aluno@example.test").expiration(Date(0))
            .signWith(Keys.hmacShaKeyFor(segredoTeste.toByteArray())).compact()
        assertNull(jwtUtils.getUsernameFromToken(token))
    }
    @Test fun exigeChaveForte() { assertFails { JwtUtils("curta") } }
}
