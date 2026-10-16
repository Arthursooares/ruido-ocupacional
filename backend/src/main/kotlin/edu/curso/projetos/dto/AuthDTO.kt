package edu.curso.projetos.dto

import jakarta.validation.constraints.*

data class UsuarioDTORequest(
    @field:NotBlank @field:Size(max = 100)
    val nome: String,

    @field:NotBlank @field:Email @field:Size(max = 150)
    val email: String,

    @field:NotBlank @field:Size(min = 8, max = 64)
    val senha: String
)

data class CredenciaisDTO(
    @field:NotBlank @field:Email @field:Size(max = 150)
    val email: String,

    @field:NotBlank @field:Size(max = 64)
    val senha: String
)

data class TokenDTO(val token: String, val tipo: String = "Bearer", val expiresIn: Long = 3600)

data class MensagemDTO(val mensagem: String)

data class ErroDTO(val mensagem: String, val campos: Map<String, String> = emptyMap())

data class UsuarioDTOResponse(val id: Long, val nome: String, val email: String)
