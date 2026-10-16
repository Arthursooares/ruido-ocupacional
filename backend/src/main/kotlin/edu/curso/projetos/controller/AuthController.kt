package edu.curso.projetos.controller

import edu.curso.projetos.dto.*
import edu.curso.projetos.mapper.UsuarioMapper
import edu.curso.projetos.security.JwtUtils
import edu.curso.projetos.security.UsuarioService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.*

@RestController
class AuthController(
    private val usuarioService: UsuarioService,
    private val jwtUtils: JwtUtils,
    private val authenticationManager: AuthenticationManager,
    private val usuarioMapper: UsuarioMapper
) {
    @PostMapping("/signup")
    fun signup(@Valid @RequestBody dadosUsuario: UsuarioDTORequest): ResponseEntity<UsuarioDTOResponse> {
        val usuario = usuarioService.cadastrar(dadosUsuario)
        return ResponseEntity.status(201).body(usuarioMapper.toResponse(usuario))
    }

    @PostMapping("/signin")
    fun signin(@Valid @RequestBody credenciais: CredenciaisDTO): TokenDTO {
        val autenticacao = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken.unauthenticated(credenciais.email, credenciais.senha)
        )
        return TokenDTO(jwtUtils.generateToken(autenticacao.name))
    }
}
