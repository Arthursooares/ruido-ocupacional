package edu.curso.projetos.mapper

import edu.curso.projetos.dto.SensorDTORequest
import edu.curso.projetos.dto.SensorDTOResponse
import edu.curso.projetos.model.Sensor
import edu.curso.projetos.model.Ambiente
import org.springframework.stereotype.Component

@Component
class SensorMapper {
    fun toModel(dadosSensor: SensorDTORequest, ambiente: Ambiente): Sensor {
        return Sensor(
            nome = dadosSensor.nome,
            codigo = dadosSensor.codigo,
            modelo = dadosSensor.modelo,
            ativo = requireNotNull(dadosSensor.ativo),
            dataInstalacao = requireNotNull(dadosSensor.dataInstalacao),
            ambiente = ambiente
        )
    }

    fun toResponse(sensor: Sensor): SensorDTOResponse {
        return SensorDTOResponse(
            id = requireNotNull(sensor.id),
            nome = sensor.nome,
            codigo = sensor.codigo,
            modelo = sensor.modelo,
            ativo = sensor.ativo,
            dataInstalacao = sensor.dataInstalacao,
            ambienteId = requireNotNull(sensor.ambiente.id),
            ambienteNome = sensor.ambiente.nome
        )
    }
}
