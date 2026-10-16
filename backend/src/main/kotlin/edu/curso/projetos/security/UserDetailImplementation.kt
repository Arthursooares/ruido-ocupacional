package edu.curso.projetos.security

import edu.curso.projetos.model.Usuario
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserDetailImplementation(
    private val usuario: Usuario
) : UserDetails {
    override fun getUsername() = usuario.email
    override fun getPassword() = usuario.senha
    override fun getAuthorities() = listOf(SimpleGrantedAuthority("ROLE_USER"))
    override fun isEnabled() = true
    override fun isAccountNonExpired() = true
    override fun isAccountNonLocked() = true
    override fun isCredentialsNonExpired() = true
}
