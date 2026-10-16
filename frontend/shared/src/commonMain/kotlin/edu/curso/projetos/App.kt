package edu.curso.projetos

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.curso.projetos.model.CadastroMemoria

@Composable
private fun Campo(rotulo: String, valor: String, aoAlterar: (String) -> Unit) {
    OutlinedTextField(value = valor, onValueChange = aoAlterar, label = { Text(rotulo) },
        modifier = Modifier.fillMaxWidth(), singleLine = true)
}

@Composable
fun App() {
    val cadastros = remember { CadastroMemoria() }
    var telaAtual by remember { mutableStateOf("ambientes") }
    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(Modifier.safeContentPadding().padding(16.dp).verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Ruído Ocupacional", style = MaterialTheme.typography.headlineMedium)
                Text("Protótipo AV1 • dados em memória, perdidos ao encerrar a aplicação. Sem conexão com o backend.")
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = { telaAtual = "ambientes" }) { Text("Ambientes") }
                    Button(onClick = { telaAtual = "sensores" }) { Text("Sensores") }
                }
                if (telaAtual == "ambientes") AmbienteFormulario(cadastros) else SensorFormulario(cadastros)
            }
        }
    }
}

@Composable
fun AmbienteFormulario(cadastros: CadastroMemoria) {
    var nome by remember { mutableStateOf("") }
    var setor by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }
    var ativo by remember { mutableStateOf(true) }
    var dataCadastro by remember { mutableStateOf("") }
    var mensagem by remember { mutableStateOf("") }
    fun limparCampos() {
        nome = ""
        setor = ""
        descricao = ""
        ativo = true
        dataCadastro = ""
        mensagem = ""
    }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Cadastro de ambientes", style = MaterialTheme.typography.titleLarge)
        Text("ID gerado automaticamente ao gravar")
        Campo("Nome (até 100)", nome) { nome = it }
        Campo("Setor (até 100)", setor) { setor = it }
        Campo("Descrição (até 255)", descricao) { descricao = it }
        Campo("Data de cadastro (AAAA-MM-DD)", dataCadastro) { dataCadastro = it }
        Row {
            Checkbox(checked = ativo, onCheckedChange = { ativo = it })
            Text("Ativo")
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = {
                val erroValidacao = cadastros.gravarAmbiente(nome, setor, descricao, ativo, dataCadastro)
                if (erroValidacao == null) {
                    limparCampos()
                    mensagem = "Ambiente gravado na memória."
                } else {
                    mensagem = erroValidacao
                }
            }) { Text("Gravar") }
            OutlinedButton(onClick = { limparCampos() }) { Text("Limpar Campos") }
        }
        Text(mensagem)
        Text("Ambientes cadastrados (${cadastros.ambientes.size})", style = MaterialTheme.typography.titleMedium)
        if (cadastros.ambientes.isEmpty()) Text("Nenhum ambiente cadastrado.")
        cadastros.ambientes.forEach { ambiente ->
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(12.dp)) {
                    Text("#${ambiente.id} — ${ambiente.nome}")
                    Text("${ambiente.setor} • ${ambiente.descricao}")
                    Text("${ambiente.dataCadastro} • ${if (ambiente.ativo) "Ativo" else "Inativo"}")
                }
            }
        }
    }
}

@Composable
fun SensorFormulario(cadastros: CadastroMemoria) {
    var nome by remember { mutableStateOf("") }
    var codigo by remember { mutableStateOf("") }
    var modelo by remember { mutableStateOf("") }
    var ativo by remember { mutableStateOf(true) }
    var dataInstalacao by remember { mutableStateOf("") }
    var ambienteId by remember { mutableStateOf<Long?>(null) }
    var menuAberto by remember { mutableStateOf(false) }
    var mensagem by remember { mutableStateOf("") }
    fun limparCampos() {
        nome = ""
        codigo = ""
        modelo = ""
        ativo = true
        dataInstalacao = ""
        ambienteId = null
        menuAberto = false
        mensagem = ""
    }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Cadastro de sensores", style = MaterialTheme.typography.titleLarge)
        Text("ID gerado automaticamente ao gravar")
        Campo("Nome (até 100)", nome) { nome = it }
        Campo("Código único (até 50)", codigo) { codigo = it }
        Campo("Modelo (até 100)", modelo) { modelo = it }
        Campo("Data de instalação (AAAA-MM-DD)", dataInstalacao) { dataInstalacao = it }
        Row {
            Checkbox(checked = ativo, onCheckedChange = { ativo = it })
            Text("Ativo")
        }
        Box {
            OutlinedButton(onClick = { menuAberto = true }, enabled = cadastros.ambientes.isNotEmpty()) {
                Text(cadastros.ambientes.firstOrNull { it.id == ambienteId }?.nome ?: "Selecionar ambiente")
            }
            DropdownMenu(expanded = menuAberto, onDismissRequest = { menuAberto = false }) {
                cadastros.ambientes.forEach { ambiente ->
                    DropdownMenuItem(text = { Text("#${ambiente.id} — ${ambiente.nome}") },
                        onClick = {
                            ambienteId = ambiente.id
                            menuAberto = false
                        })
                }
            }
        }
        if (cadastros.ambientes.isEmpty()) Text("Cadastre um ambiente antes de gravar um sensor.")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = {
                val erroValidacao = cadastros.gravarSensor(nome, codigo, modelo, ativo, dataInstalacao, ambienteId)
                if (erroValidacao == null) {
                    limparCampos()
                    mensagem = "Sensor gravado na memória."
                } else {
                    mensagem = erroValidacao
                }
            }) { Text("Gravar") }
            OutlinedButton(onClick = { limparCampos() }) { Text("Limpar Campos") }
        }
        Text(mensagem)
        Text("Sensores cadastrados (${cadastros.sensores.size})", style = MaterialTheme.typography.titleMedium)
        if (cadastros.sensores.isEmpty()) Text("Nenhum sensor cadastrado.")
        cadastros.sensores.forEach { sensor ->
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(12.dp)) {
                    Text("#${sensor.id} — ${sensor.nome} • ${sensor.codigo}")
                    Text("${sensor.modelo} • ${sensor.dataInstalacao} • ${if (sensor.ativo) "Ativo" else "Inativo"}")
                    Text("Ambiente: ${cadastros.ambientes.first { it.id == sensor.ambienteId }.nome}")
                }
            }
        }
    }
}
