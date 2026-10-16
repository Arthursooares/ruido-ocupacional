package edu.curso.projetos.controller

import edu.curso.projetos.dto.AmbienteDTORequest
import edu.curso.projetos.dto.AmbienteDTOResponse
import edu.curso.projetos.dto.MensagemDTO
import edu.curso.projetos.mapper.AmbienteMapper
import edu.curso.projetos.service.AmbienteService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/ambientes")
class AmbienteController(
    private val ambienteService: AmbienteService,
    private val ambienteMapper: AmbienteMapper
) {
    @GetMapping
    fun listar(): List<AmbienteDTOResponse> {
        val ambientes = ambienteService.listar()
        return ambientes.map { ambienteMapper.toResponse(it) }
    }

    @GetMapping("/{id}")
    fun buscar(@PathVariable id: Long): AmbienteDTOResponse {
        val ambiente = ambienteService.buscar(id)
        return ambienteMapper.toResponse(ambiente)
    }

    @GetMapping("/buscar")
    fun pesquisar(@RequestParam nome: String): List<AmbienteDTOResponse> {
        val ambientes = ambienteService.pesquisar(nome)
        return ambientes.map { ambienteMapper.toResponse(it) }
    }

    @PostMapping
    fun cadastrar(@Valid @RequestBody dadosAmbiente: AmbienteDTORequest): ResponseEntity<MensagemDTO> {
        val ambiente = ambienteMapper.toModel(dadosAmbiente)
        ambienteService.cadastrar(ambiente)
        return ResponseEntity.status(201).body(MensagemDTO("Ambiente cadastrado com sucesso"))
    }

    @PutMapping("/{id}")
    fun atualizar(
        @PathVariable id: Long,
        @Valid @RequestBody dadosAmbiente: AmbienteDTORequest
    ): MensagemDTO {
        val ambiente = ambienteMapper.toModel(dadosAmbiente)
        ambienteService.atualizar(id, ambiente)
        return MensagemDTO("Ambiente atualizado com sucesso")
    }

    @DeleteMapping("/{id}")
    fun remover(@PathVariable id: Long): MensagemDTO {
        ambienteService.remover(id)
        return MensagemDTO("Ambiente removido com sucesso")
    }
}
