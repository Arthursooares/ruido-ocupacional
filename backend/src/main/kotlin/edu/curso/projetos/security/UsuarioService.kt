package edu.curso.projetos.security

import edu.curso.projetos.dto.UsuarioDTORequest
import edu.curso.projetos.mapper.UsuarioMapper
import edu.curso.projetos.model.Usuario
import edu.curso.projetos.repository.UsuarioRepository
import edu.curso.projetos.service.ConflitoException
import edu.curso.projetos.service.SenhaInvalidaException
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.Locale

@Service
class UsuarioService(
    private val usuarioRepository: UsuarioRepository,
    private val passwordEncoder: PasswordEncoder,
    private val usuarioMapper: UsuarioMapper
) : UserDetailsService {
    override fun loadUserByUsername(emailInformado: String): UserDetails {
        val email = emailInformado.trim().lowercase(Locale.ROOT)
        val usuario = usuarioRepository.findByEmail(email)
            ?: throw UsernameNotFoundException("Credenciais inválidas")
        return UserDetailImplementation(usuario)
    }

    fun cadastrar(dadosUsuario: UsuarioDTORequest): Usuario {
        val email = dadosUsuario.email.trim().lowercase(Locale.ROOT)
        if (usuarioRepository.existsByEmail(email)) {
            throw ConflitoException("Email já cadastrado")
        }
        if (dadosUsuario.senha.toByteArray(Charsets.UTF_8).size > 72) {
            throw SenhaInvalidaException()
        }
        val senhaCodificada = checkNotNull(passwordEncoder.encode(dadosUsuario.senha))
        val usuario = usuarioMapper.toModel(dadosUsuario, senhaCodificada)
        return usuarioRepository.save(usuario)
    }
}
