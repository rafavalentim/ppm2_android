package br.com.correios.ppm.login.application

import br.com.correios.ppm.login.data.AutenticacaoRaw
import br.com.correios.ppm.login.data.LoginRepository
import br.com.correios.ppm.login.data.TokenResponse
import br.com.correios.ppm.login.data.UsuarioRaw

class LoginUseCase(private val repo : LoginRepository) {

    private fun mapToAutenticacaoRaw(autenticacao: Autenticacao): AutenticacaoRaw {

        return AutenticacaoRaw(
            usuario = autenticacao.usuario,
            senha = autenticacao.senha
        )
    }

    private fun mapToAutenticacao(autenticacaoRaw: AutenticacaoRaw): Autenticacao {

        return Autenticacao(
            usuario = autenticacaoRaw.usuario,
            senha = autenticacaoRaw.senha
        )
    }

    private fun mapToUsuario(raw : UsuarioRaw?) : Usuario? {

        return Usuario(
            nome = raw?.nome,
            login = raw?.login,
            email = raw?.email,
            endereco = raw?.endereco as Endereco?,
            lotacao = raw?.lotacao as Lotacao?,
            unidadeSRO = raw?.unidadeSRO,
            distritoPostal = raw?.distritoPostal
        )
    }

    suspend fun fetchUsuarioLogado(): Usuario?{

        val usuarioRaw = repo.getUsuarioLogado()

        return mapToUsuario(usuarioRaw)
    }

    suspend fun autenticar(autenticacao: Autenticacao): TokenResponse?{

        val raw = mapToAutenticacaoRaw(autenticacao)

        val token = repo.autenticar(raw)

        return token
    }

}