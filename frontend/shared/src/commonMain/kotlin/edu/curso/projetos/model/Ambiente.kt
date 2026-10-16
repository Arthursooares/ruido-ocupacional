package edu.curso.projetos.model
data class Ambiente(
    val id: Long,
    val nome: String,
    val setor: String,
    val descricao: String,
    val ativo: Boolean,
    val dataCadastro: String
)
