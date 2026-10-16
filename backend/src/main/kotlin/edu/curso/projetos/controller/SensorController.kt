package edu.curso.projetos.controller

import edu.curso.projetos.dto.SensorDTORequest
import edu.curso.projetos.dto.SensorDTOResponse
import edu.curso.projetos.dto.MensagemDTO
import edu.curso.projetos.mapper.SensorMapper
import edu.curso.projetos.service.SensorService
import edu.curso.projetos.service.AmbienteService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/sensores")
class SensorController(
    private val sensorService: SensorService,
    private val sensorMapper: SensorMapper,
    private val ambienteService: AmbienteService
) {
    @GetMapping
    fun listar(): List<SensorDTOResponse> {
        val sensores = sensorService.listar()
        return sensores.map { sensorMapper.toResponse(it) }
    }

    @GetMapping("/{id}")
    fun buscar(@PathVariable id: Long): SensorDTOResponse {
        val sensor = sensorService.buscar(id)
        return sensorMapper.toResponse(sensor)
    }

    @GetMapping("/buscar")
    fun pesquisar(@RequestParam nome: String): List<SensorDTOResponse> {
        val sensores = sensorService.pesquisar(nome)
        return sensores.map { sensorMapper.toResponse(it) }
    }

    @PostMapping
    fun cadastrar(@Valid @RequestBody dadosSensor: SensorDTORequest): ResponseEntity<MensagemDTO> {
        val ambiente = ambienteService.buscar(requireNotNull(dadosSensor.ambienteId))
        val sensor = sensorMapper.toModel(dadosSensor, ambiente)
        sensorService.cadastrar(sensor)
        return ResponseEntity.status(201).body(MensagemDTO("Sensor cadastrado com sucesso"))
    }

    @PutMapping("/{id}")
    fun atualizar(
        @PathVariable id: Long,
        @Valid @RequestBody dadosSensor: SensorDTORequest
    ): MensagemDTO {
        val ambiente = ambienteService.buscar(requireNotNull(dadosSensor.ambienteId))
        val sensor = sensorMapper.toModel(dadosSensor, ambiente)
        sensorService.atualizar(id, sensor)
        return MensagemDTO("Sensor atualizado com sucesso")
    }

    @DeleteMapping("/{id}")
    fun remover(@PathVariable id: Long): MensagemDTO {
        sensorService.remover(id)
        return MensagemDTO("Sensor removido com sucesso")
    }
}
