package edu.curso.projetos.repository

import edu.curso.projetos.model.Sensor
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface SensorRepository : JpaRepository<Sensor, Long> {
    @Query("select sensor from Sensor sensor where lower(sensor.nome) like lower(concat('%', :nome, '%'))")
    fun buscarPorNome(@Param("nome") nome: String): List<Sensor>

    fun existsByCodigoAndIdNot(codigo: String, id: Long): Boolean
    fun existsByAmbienteId(ambienteId: Long): Boolean
}
