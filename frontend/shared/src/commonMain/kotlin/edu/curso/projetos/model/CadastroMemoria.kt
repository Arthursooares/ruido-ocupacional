package edu.curso.projetos.model

import androidx.compose.runtime.mutableStateListOf

fun dataValida(texto: String): Boolean {
    if (!Regex("[0-9]{4}-[0-9]{2}-[0-9]{2}").matches(texto)) return false
    val ano = texto.substring(0, 4).toInt()
    val mes = texto.substring(5, 7).toInt()
    val dia = texto.substring(8, 10).toInt()
    if (ano !in 1..9999 || mes !in 1..12) return false
    val anoBissexto = ano % 400 == 0 || (ano % 4 == 0 && ano % 100 != 0)
    val diasPorMes = listOf(31, if (anoBissexto) 29 else 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
    return dia in 1..diasPorMes[mes - 1]
}

class CadastroMemoria {
    private val ambientesMutaveis = mutableStateListOf<Ambiente>()
    private val sensoresMutaveis = mutableStateListOf<Sensor>()
    val ambientes: List<Ambiente> get() = ambientesMutaveis
    val sensores: List<Sensor> get() = sensoresMutaveis
    private var proximoAmbienteId = 1L
    private var proximoSensorId = 1L

    fun gravarAmbiente(nome: String, setor: String, descricao: String, ativo: Boolean, dataCadastro: String): String? {
        if (nome.isBlank() || nome.length > 100 || setor.isBlank() || setor.length > 100 ||
            descricao.isBlank() || descricao.length > 255) return "Preencha nome, setor e descrição respeitando os limites."
        if (!dataValida(dataCadastro)) return "Informe uma data válida no formato AAAA-MM-DD."
        ambientesMutaveis.add(Ambiente(proximoAmbienteId++, nome.trim(), setor.trim(), descricao.trim(), ativo, dataCadastro))
        return null
    }

    fun gravarSensor(nome: String, codigo: String, modelo: String, ativo: Boolean, dataInstalacao: String, ambienteId: Long?): String? {
        if (nome.isBlank() || nome.length > 100 || codigo.isBlank() || codigo.length > 50 ||
            modelo.isBlank() || modelo.length > 100) return "Preencha nome, código e modelo respeitando os limites."
        if (!dataValida(dataInstalacao)) return "Informe uma data válida no formato AAAA-MM-DD."
        if (ambientes.none { it.id == ambienteId }) return "Cadastre e selecione um ambiente."
        if (sensores.any { it.codigo == codigo.trim() }) return "Código de sensor já cadastrado."
        sensoresMutaveis.add(Sensor(proximoSensorId++, nome.trim(), codigo.trim(), modelo.trim(), ativo, dataInstalacao, requireNotNull(ambienteId)))
        return null
    }
}
