package edu.curso.projetos.model

import jakarta.persistence.*

@Entity
@Table(name = "usuario")
data class Usuario(

    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(length = 100, nullable = false)
    val nome: String = "",

    @Column(length = 150, nullable = false, unique = true)
    val email: String = "",

    @Column(length = 100, nullable = false)
    val senha: String = ""
)
