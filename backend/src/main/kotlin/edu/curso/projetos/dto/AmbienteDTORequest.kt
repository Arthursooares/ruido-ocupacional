package edu.curso.projetos.dto

import jakarta.validation.constraints.*
import java.time.LocalDate

data class AmbienteDTORequest(
    @field:NotBlank @field:Size(max = 100)
    val nome: String,

    @field:NotBlank @field:Size(max = 100)
    val setor: String,

    @field:NotBlank @field:Size(max = 255)
    val descricao: String,

    @field:NotNull
    val ativo: Boolean?,

    @field:NotNull @field:PastOrPresent
    val dataCadastro: LocalDate?
)
