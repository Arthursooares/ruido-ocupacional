package edu.curso.projetos.dto

import java.time.LocalDate

data class AmbienteDTOResponse(
    val id: Long,
    val nome: String,
    val setor: String,
    val descricao: String,
    val ativo: Boolean,
    val dataCadastro: LocalDate
)
