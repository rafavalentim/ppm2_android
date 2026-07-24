package br.com.correios.ppm.login.application

import br.com.correios.ppm.data.ApiResult
import br.com.correios.ppm.data.BaseRaw
import br.com.correios.ppm.login.data.AutenticacaoRaw
import br.com.correios.ppm.login.data.LoginRepository
import br.com.correios.ppm.login.data.TokenResponse
import br.com.correios.ppm.login.data.UsuarioRaw
import br.com.correios.ppm.login.data.VersaoApp

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
            distritoPostal = raw?.distritoPostal,
            msgErro = raw?.base?.payload
        )
    }

    suspend fun fetchUsuarioLogado(): Usuario?{

        val usuarioRaw = repo.getUsuarioLogado()

        return mapToUsuario(usuarioRaw)
    }

    suspend fun autenticar(autenticacao: Autenticacao): ApiResult<TokenResponse>{

        val raw = mapToAutenticacaoRaw(autenticacao)

        return repo.autenticar(raw)
    }


    suspend fun getVersaoAppAtual(appPackage: String): VersaoApp? {

        return repo.getVersaoAppAtual(appPackage)
    }

    suspend fun downloadManifestoIos(appPackage: String, versao: String): ApiResult<String> {

        return repo.downloadManifestoIos(appPackage, versao)
    }

    fun manifestoIosUrl(appPackage: String, versao: String): String {

        return repo.manifestoIosUrl(appPackage, versao)
    }

}