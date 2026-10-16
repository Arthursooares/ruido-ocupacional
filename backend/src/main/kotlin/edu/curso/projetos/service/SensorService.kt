package edu.curso.projetos.service

import edu.curso.projetos.model.Sensor
import edu.curso.projetos.repository.SensorRepository
import org.springframework.stereotype.Service

@Service
class SensorService(
    private val sensorRepository: SensorRepository
) {
    fun listar(): List<Sensor> {
        return sensorRepository.findAll()
    }

    fun buscar(id: Long): Sensor {
        return sensorRepository.findById(id)
            .orElseThrow { RecursoNaoEncontrado("Sensor não encontrado") }
    }

    fun pesquisar(nome: String): List<Sensor> {
        return sensorRepository.buscarPorNome(nome)
    }

    fun cadastrar(sensor: Sensor): Sensor {
        if (sensorRepository.existsByCodigoAndIdNot(sensor.codigo, 0L)) {
            throw ConflitoException("Código de sensor já cadastrado")
        }
        return sensorRepository.save(sensor)
    }

    fun atualizar(id: Long, sensor: Sensor): Sensor {
        buscar(id)
        if (sensorRepository.existsByCodigoAndIdNot(sensor.codigo, id)) {
            throw ConflitoException("Código de sensor já cadastrado")
        }
        return sensorRepository.save(sensor.copy(id = id))
    }

    fun remover(id: Long) {
        val sensor = buscar(id)
        sensorRepository.delete(sensor)
    }
}
