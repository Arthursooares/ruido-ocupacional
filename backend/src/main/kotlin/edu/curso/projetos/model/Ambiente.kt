package edu.curso.projetos.model

import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "ambiente")
data class Ambiente(

    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(length = 100, nullable = false)
    val nome: String = "",

    @Column(length = 100, nullable = false)
    val setor: String = "",

    @Column(length = 255, nullable = false)
    val descricao: String = "",

    @Column(nullable = false)
    val ativo: Boolean = true,

    @Column(nullable = false)
    val dataCadastro: LocalDate = LocalDate.now()
)
