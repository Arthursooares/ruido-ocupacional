package edu.curso.projetos.repository

import edu.curso.projetos.model.Ambiente
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface AmbienteRepository : JpaRepository<Ambiente, Long> {
    @Query("select ambiente from Ambiente ambiente where lower(ambiente.nome) like lower(concat('%', :nome, '%'))")
    fun buscarPorNome(@Param("nome") nome: String): List<Ambiente>
}
