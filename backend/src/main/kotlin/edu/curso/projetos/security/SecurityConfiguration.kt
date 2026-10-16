package edu.curso.projetos.security

import jakarta.servlet.DispatcherType
import org.springframework.context.annotation.*
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
class SecurityConfiguration {
    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun authenticationManager(usuarioService: UsuarioService, passwordEncoder: PasswordEncoder): AuthenticationManager {
        val provedorAutenticacao = DaoAuthenticationProvider(usuarioService)
        provedorAutenticacao.setPasswordEncoder(passwordEncoder)
        return ProviderManager(provedorAutenticacao)
    }

    @Bean
    fun filterChain(http: HttpSecurity, filtroJwt: JwtAuthenticationFilter): SecurityFilterChain {
        http.csrf { it.disable() }.httpBasic { it.disable() }.formLogin { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests {
                it.dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()
                    .requestMatchers(HttpMethod.POST, "/signin", "/signup").permitAll()
                    .anyRequest().authenticated()
            }
            .exceptionHandling {
                it.authenticationEntryPoint { _, response, _ ->
                    response.status = 401
                    response.contentType = "application/json;charset=UTF-8"
                    response.writer.write("""{"mensagem":"Autenticação necessária ou token inválido"}""")
                }
            }
            .addFilterBefore(filtroJwt, UsernamePasswordAuthenticationFilter::class.java)
        return http.build()
    }
}
