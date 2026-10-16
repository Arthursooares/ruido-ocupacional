package edu.curso.projetos.service

import edu.curso.projetos.model.Ambiente
import edu.curso.projetos.repository.AmbienteRepository
import edu.curso.projetos.repository.SensorRepository
import org.springframework.stereotype.Service

@Service
class AmbienteService(
    private val ambienteRepository: AmbienteRepository,
    private val sensorRepository: SensorRepository
) {
    fun listar(): List<Ambiente> {
        return ambienteRepository.findAll()
    }

    fun buscar(id: Long): Ambiente {
        return ambienteRepository.findById(id)
            .orElseThrow { RecursoNaoEncontrado("Ambiente não encontrado") }
    }

    fun pesquisar(nome: String): List<Ambiente> {
        return ambienteRepository.buscarPorNome(nome)
    }

    fun cadastrar(ambiente: Ambiente): Ambiente {
        return ambienteRepository.save(ambiente)
    }

    fun atualizar(id: Long, ambiente: Ambiente): Ambiente {
        buscar(id)
        return ambienteRepository.save(ambiente.copy(id = id))
    }

    fun remover(id: Long) {
        val ambiente = buscar(id)
        if (sensorRepository.existsByAmbienteId(id)) {
            throw ConflitoException("Remova os sensores vinculados antes de excluir o ambiente")
        }
        ambienteRepository.delete(ambiente)
    }
}
