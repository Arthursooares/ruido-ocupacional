package edu.curso.projetos.dto

import jakarta.validation.constraints.*
import java.time.LocalDate

data class SensorDTORequest(
    @field:NotBlank @field:Size(max = 100)
    val nome: String,

    @field:NotBlank @field:Size(max = 50)
    val codigo: String,

    @field:NotBlank @field:Size(max = 100)
    val modelo: String,

    @field:NotNull
    val ativo: Boolean?,

    @field:NotNull @field:PastOrPresent
    val dataInstalacao: LocalDate?,

    @field:NotNull @field:Positive
    val ambienteId: Long?
)
