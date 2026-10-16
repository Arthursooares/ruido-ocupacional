package edu.curso.projetos.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val jwtUtils: JwtUtils,
    private val usuarioService: UsuarioService
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val autorizacao = request.getHeader("Authorization")
        if (autorizacao != null && autorizacao.startsWith("Bearer ")) {
            val email = jwtUtils.getUsernameFromToken(autorizacao.substring(7))
            if (email != null && SecurityContextHolder.getContext().authentication == null) {
                try {
                    val usuario = usuarioService.loadUserByUsername(email)
                    val autenticacao = UsernamePasswordAuthenticationToken(usuario, null, usuario.authorities)
                    SecurityContextHolder.getContext().authentication = autenticacao
                } catch (_: UsernameNotFoundException) {
                    SecurityContextHolder.clearContext()
                }
            }
        }
        filterChain.doFilter(request, response)
    }
}
