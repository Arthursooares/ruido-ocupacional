package edu.curso.projetos.mapper

import edu.curso.projetos.dto.AmbienteDTORequest
import edu.curso.projetos.dto.AmbienteDTOResponse
import edu.curso.projetos.model.Ambiente
import org.springframework.stereotype.Component

@Component
class AmbienteMapper {
    fun toModel(dadosAmbiente: AmbienteDTORequest): Ambiente {
        return Ambiente(
            nome = dadosAmbiente.nome,
            setor = dadosAmbiente.setor,
            descricao = dadosAmbiente.descricao,
            ativo = requireNotNull(dadosAmbiente.ativo),
            dataCadastro = requireNotNull(dadosAmbiente.dataCadastro)
        )
    }

    fun toResponse(ambiente: Ambiente): AmbienteDTOResponse {
        return AmbienteDTOResponse(
            id = requireNotNull(ambiente.id),
            nome = ambiente.nome,
            setor = ambiente.setor,
            descricao = ambiente.descricao,
            ativo = ambiente.ativo,
            dataCadastro = ambiente.dataCadastro
        )
    }
}
