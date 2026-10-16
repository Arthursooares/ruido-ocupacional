package edu.curso.projetos

import edu.curso.projetos.model.*
import kotlin.test.*

class CadastroMemoriaTest {
    @Test fun datasReais() {
        assertTrue(dataValida("2024-02-29"))
        assertFalse(dataValida("2025-02-29"))
        assertFalse(dataValida("2026-13-01"))
        assertFalse(dataValida("2026-04-31"))
        assertFalse(dataValida("15/10/2026"))
    }
    @Test fun gravaSemPerderVinculos() {
        val cadastro = CadastroMemoria()
        assertNull(cadastro.gravarAmbiente("Fábrica", "Produção", "Área de máquinas", true, "2026-01-01"))
        assertNull(cadastro.gravarSensor("Sensor A", "S-01", "ESP32", true, "2026-01-02", 1))
        assertEquals(1L, cadastro.sensores.single().ambienteId)
        assertNotNull(cadastro.gravarSensor("Outro", "S-01", "ESP32", true, "2026-01-02", 1))
        assertEquals(1, cadastro.sensores.size)
    }
    @Test fun rejeitaIncompletosESensorSemAmbiente() {
        val cadastro = CadastroMemoria()
        assertNotNull(cadastro.gravarAmbiente("", "Setor", "Descrição", true, "2026-01-01"))
        assertNotNull(cadastro.gravarSensor("Sensor", "S-01", "ESP32", true, "2026-01-01", 99))
        assertTrue(cadastro.ambientes.isEmpty())
        assertTrue(cadastro.sensores.isEmpty())
    }
}
