package edu.curso.projetos.mapper

import edu.curso.projetos.dto.UsuarioDTORequest
import edu.curso.projetos.dto.UsuarioDTOResponse
import edu.curso.projetos.model.Usuario
import org.springframework.stereotype.Component
import java.util.Locale

@Component
class UsuarioMapper {
    fun toModel(dadosUsuario: UsuarioDTORequest, senhaCodificada: String): Usuario {
        return Usuario(
            nome = dadosUsuario.nome,
            email = dadosUsuario.email.trim().lowercase(Locale.ROOT),
            senha = senhaCodificada
        )
    }

    fun toResponse(usuario: Usuario): UsuarioDTOResponse {
        return UsuarioDTOResponse(
            id = requireNotNull(usuario.id),
            nome = usuario.nome,
            email = usuario.email
        )
    }
}
