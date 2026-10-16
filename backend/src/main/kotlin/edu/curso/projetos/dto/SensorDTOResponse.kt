package edu.curso.projetos.dto

import java.time.LocalDate

data class SensorDTOResponse(
    val id: Long,
    val nome: String,
    val codigo: String,
    val modelo: String,
    val ativo: Boolean,
    val dataInstalacao: LocalDate,
    val ambienteId: Long,
    val ambienteNome: String
)
