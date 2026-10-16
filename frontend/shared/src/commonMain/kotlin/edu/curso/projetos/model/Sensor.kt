package edu.curso.projetos.model

data class Sensor(
    val id: Long,
    val nome: String,
    val codigo: String,
    val modelo: String,
    val ativo: Boolean,
    val dataInstalacao: String,
    val ambienteId: Long
)
