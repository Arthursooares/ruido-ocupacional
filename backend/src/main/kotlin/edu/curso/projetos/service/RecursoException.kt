package edu.curso.projetos.service

class RecursoNaoEncontrado(mensagem: String) : RuntimeException(mensagem)
class ConflitoException(mensagem: String) : RuntimeException(mensagem)
class SenhaInvalidaException : RuntimeException("Senha deve ter no máximo 72 bytes em UTF-8")
