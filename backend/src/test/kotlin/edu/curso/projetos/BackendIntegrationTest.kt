package edu.curso.projetos

import com.jayway.jsonpath.JsonPath
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import java.util.Date
import edu.curso.projetos.repository.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.AfterEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.ActiveProfiles
import java.net.URI
import java.net.http.*
import java.util.UUID
import kotlin.test.*

@Tag("integration")
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BackendIntegrationTest {
    @LocalServerPort private var porta: Int = 0
    @Autowired private lateinit var ambientes: AmbienteRepository
    @Autowired private lateinit var sensores: SensorRepository
    @Autowired private lateinit var usuarios: UsuarioRepository
    private val clienteHttp = HttpClient.newHttpClient()
    private val prefixoTeste = "av1-" + UUID.randomUUID().toString()
    private var token: String? = null
    private val email get() = "$prefixoTeste@example.test"
    private fun enviarRequisicao(metodo: String, caminho: String, corpo: String? = null, tokenAutenticacao: String? = token): HttpResponse<String> {
        val requisicao = HttpRequest.newBuilder(URI("http://localhost:$porta$caminho"))
            .header("Content-Type", "application/json")
        if (tokenAutenticacao != null) requisicao.header("Authorization", "Bearer $tokenAutenticacao")
        requisicao.method(metodo, corpo?.let(HttpRequest.BodyPublishers::ofString) ?: HttpRequest.BodyPublishers.noBody())
        return clienteHttp.send(requisicao.build(), HttpResponse.BodyHandlers.ofString())
    }
    private fun ambiente(nome: String = prefixoTeste) = """{"nome":"$nome","setor":"Produção","descricao":"Área de máquinas","ativo":true,"dataCadastro":"2025-01-01"}"""
    private fun sensor(id: Long, nome: String = prefixoTeste, codigo: String = prefixoTeste) = """{"nome":"$nome","codigo":"$codigo","modelo":"ESP32","ativo":true,"dataInstalacao":"2025-01-02","ambienteId":$id}"""
    @AfterEach fun limparSomenteFixturesDesteTeste() {
        sensores.deleteAll(sensores.findAll().filter { it.codigo.startsWith(prefixoTeste) })
        ambientes.deleteAll(ambientes.findAll().filter { it.nome.startsWith(prefixoTeste) })
        usuarios.findByEmail(email)?.let { usuarios.delete(it) }
    }
    @Test fun inicializaEExecutaFluxoCompletoDaAV1() {
        assertEquals(401, enviarRequisicao("GET", "/ambientes").statusCode())
        assertEquals(201, enviarRequisicao("POST", "/signup", """{"nome":"Aluno","email":"$email","senha":"senha-de-teste-123"}""").statusCode())
        assertEquals(409, enviarRequisicao("POST", "/signup", """{"nome":"Aluno","email":"$email","senha":"senha-de-teste-123"}""").statusCode())
        assertEquals(401, enviarRequisicao("POST", "/signin", """{"email":"$email","senha":"errada"}""").statusCode())
        val login = enviarRequisicao("POST", "/signin", """{"email":"$email","senha":"senha-de-teste-123"}""")
        assertEquals(200, login.statusCode())
        token = JsonPath.read<String>(login.body(), "$.token")
        assertEquals(401, enviarRequisicao("GET", "/sensores", tokenAutenticacao = "invalido").statusCode())
        val expirado = Jwts.builder().subject(email).expiration(Date(0)).signWith(Keys.hmacShaKeyFor(
            "chave-exclusiva-do-ambiente-de-testes-com-mais-de-32-bytes".toByteArray())).compact()
        assertEquals(401, enviarRequisicao("GET", "/sensores", tokenAutenticacao = expirado).statusCode())
        val senhaCodificada = usuarios.findByEmail(email)!!.senha
        assertNotEquals("senha-de-teste-123", senhaCodificada)
        assertTrue(senhaCodificada.startsWith("$" + "2"))
        assertFalse(login.body().contains(senhaCodificada))
        assertEquals(400, enviarRequisicao("POST", "/ambientes", ambiente("")).statusCode())
        assertEquals(400, enviarRequisicao("POST", "/ambientes", "{}").statusCode())
        assertEquals(201, enviarRequisicao("POST", "/ambientes", ambiente()).statusCode())
        val ambienteId = ambientes.buscarPorNome(prefixoTeste).single().id!!
        assertEquals(200, enviarRequisicao("GET", "/ambientes/$ambienteId").statusCode())
        assertTrue(enviarRequisicao("GET", "/ambientes").body().contains(prefixoTeste))
        assertTrue(enviarRequisicao("GET", "/ambientes/buscar?nome=${prefixoTeste.take(20)}").body().contains(prefixoTeste))
        assertEquals(200, enviarRequisicao("PUT", "/ambientes/$ambienteId", ambiente("$prefixoTeste-editado")).statusCode())
        assertEquals("$prefixoTeste-editado", ambientes.findById(ambienteId).orElseThrow().nome)
        assertEquals(404, enviarRequisicao("POST", "/sensores", sensor(Long.MAX_VALUE)).statusCode())
        assertEquals(201, enviarRequisicao("POST", "/sensores", sensor(ambienteId)).statusCode())
        val sensorId = sensores.buscarPorNome(prefixoTeste).single().id!!
        val consulta = enviarRequisicao("GET", "/sensores/$sensorId")
        assertEquals(200, consulta.statusCode())
        assertTrue(consulta.body().contains("ambienteNome"))
        assertTrue(enviarRequisicao("GET", "/sensores").body().contains(prefixoTeste))
        assertTrue(enviarRequisicao("GET", "/sensores/buscar?nome=${prefixoTeste.take(20)}").body().contains(prefixoTeste))
        assertEquals(409, enviarRequisicao("POST", "/sensores", sensor(ambienteId)).statusCode())
        assertEquals(200, enviarRequisicao("PUT", "/sensores/$sensorId", sensor(ambienteId, "$prefixoTeste-editado")).statusCode())
        assertEquals("$prefixoTeste-editado", sensores.findById(sensorId).orElseThrow().nome)
        assertEquals(201, enviarRequisicao("POST", "/sensores", sensor(ambienteId, "$prefixoTeste-segundo", "$prefixoTeste-2")).statusCode())
        val segundoSensorId = sensores.buscarPorNome("$prefixoTeste-segundo").single().id!!
        assertEquals(409, enviarRequisicao("PUT", "/sensores/$segundoSensorId", sensor(ambienteId)).statusCode())
        assertEquals(200, enviarRequisicao("DELETE", "/sensores/$segundoSensorId").statusCode())
        assertEquals(409, enviarRequisicao("DELETE", "/ambientes/$ambienteId").statusCode())
        assertEquals(200, enviarRequisicao("DELETE", "/sensores/$sensorId").statusCode())
        assertEquals(404, enviarRequisicao("GET", "/sensores/$sensorId").statusCode())
        assertEquals(200, enviarRequisicao("DELETE", "/ambientes/$ambienteId").statusCode())
        assertEquals(404, enviarRequisicao("GET", "/ambientes/$ambienteId").statusCode())
    }
}
