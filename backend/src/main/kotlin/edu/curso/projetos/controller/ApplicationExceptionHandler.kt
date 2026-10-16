package edu.curso.projetos.controller

import edu.curso.projetos.dto.ErroDTO
import edu.curso.projetos.service.*
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.*
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException

@RestControllerAdvice
class ApplicationExceptionHandler {
    @ExceptionHandler(SenhaInvalidaException::class)
    fun senha(erro: SenhaInvalidaException) = ResponseEntity.badRequest().body(ErroDTO(erro.message!!))

    @ExceptionHandler(RecursoNaoEncontrado::class)
    fun naoEncontrado(erro: RecursoNaoEncontrado) = ResponseEntity.status(404).body(ErroDTO(erro.message!!))

    @ExceptionHandler(ConflitoException::class)
    fun conflito(erro: ConflitoException) = ResponseEntity.status(409).body(ErroDTO(erro.message!!))

    @ExceptionHandler(DataIntegrityViolationException::class)
    fun integridade() = ResponseEntity.status(409).body(ErroDTO("Registro duplicado ou vinculado a outro cadastro"))

    @ExceptionHandler(AuthenticationException::class)
    fun autenticacao() = ResponseEntity.status(401).body(ErroDTO("Credenciais inválidas"))

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun validacao(erro: MethodArgumentNotValidException): ResponseEntity<ErroDTO> {
        val camposInvalidos = erro.bindingResult.fieldErrors.associate { campo ->
            campo.field to (campo.defaultMessage ?: "Valor inválido")
        }
        return ResponseEntity.badRequest().body(ErroDTO("Revise os campos informados", camposInvalidos))
    }

    @ExceptionHandler(HttpMessageNotReadableException::class, MethodArgumentTypeMismatchException::class)
    fun formato() = ResponseEntity.badRequest().body(ErroDTO("Formato de requisição inválido"))
}
