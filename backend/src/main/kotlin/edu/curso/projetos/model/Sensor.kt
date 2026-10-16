package edu.curso.projetos.model

import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "sensor")
data class Sensor(

    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(length = 100, nullable = false)
    val nome: String = "",

    @Column(length = 50, nullable = false, unique = true)
    val codigo: String = "",

    @Column(length = 100, nullable = false)
    val modelo: String = "",

    @Column(nullable = false)
    val ativo: Boolean = true,

    @Column(nullable = false)
    val dataInstalacao: LocalDate = LocalDate.now(),

    @ManyToOne(fetch = FetchType.EAGER, optional = false)

    @JoinColumn(name = "ambiente_id", nullable = false)
    val ambiente: Ambiente = Ambiente()
)
