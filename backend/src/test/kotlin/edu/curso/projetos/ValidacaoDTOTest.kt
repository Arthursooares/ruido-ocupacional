package edu.curso.projetos

import edu.curso.projetos.dto.*
import jakarta.validation.Validation
import java.time.LocalDate
import kotlin.test.*
import org.junit.jupiter.api.Test

class ValidacaoDTOTest {
    @Test fun ambienteRejeitaCamposVaziosETamanhoExcedido() {
        Validation.buildDefaultValidatorFactory().use { fabricaValidacao ->
            val erros = fabricaValidacao.validator.validate(AmbienteDTORequest("", "x".repeat(101), "", null, null))
            assertTrue(erros.map { it.propertyPath.toString() }.containsAll(listOf("nome", "setor", "descricao", "ativo", "dataCadastro")))
        }
    }
    @Test fun sensorExigeAmbientePositivoEDataNaoFutura() {
        Validation.buildDefaultValidatorFactory().use { fabricaValidacao ->
            val erros = fabricaValidacao.validator.validate(SensorDTORequest("Sensor", "S01", "ESP32", true, LocalDate.now().plusDays(1), 0))
            assertTrue(erros.map { it.propertyPath.toString() }.containsAll(listOf("ambienteId", "dataInstalacao")))
            assertTrue(fabricaValidacao.validator.validate(SensorDTORequest("Sensor", "S01", "ESP32", true, LocalDate.now(), 1)).isEmpty())
        }
    }
}
