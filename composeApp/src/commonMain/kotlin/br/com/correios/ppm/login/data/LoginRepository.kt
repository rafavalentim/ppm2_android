package br.com.correios.ppm.login.data

import br.com.correios.ppm.data.ApiResult
import br.com.correios.ppm.data.BaseRaw
import br.com.correios.ppm.login.application.Autenticacao

class LoginRepository(
    private val service : LoginService
) {

    suspend fun getUsuarioLogado(): UsuarioRaw?{

        //Por enquanto chamar somente a service. Ver se teremos esses dados no banco.
        return fetchUsuarioLogado(service)
    }

    suspend fun fetchUsuarioLogado(service: LoginService) : UsuarioRaw? {

        var usuario = UsuarioRaw()

        when (val r = service.getUsuarioLogado()) {
            is ApiResult.Success -> {

                usuario = r.data
            }
            is ApiResult.Error -> {

                usuario.base = BaseRaw()
                usuario.base?.status = r.status
                usuario.base?.msg = r.message
                usuario.base?.payload = r.payload

                // status pode ser nulo (erro de rede), payload pode ter HTML/JSON do erro
                println("Falhou: status=${r.status} msg=${r.message}\n${r.payload}")
            }
        }
        return usuario
    }

    suspend fun autenticar(raw: AutenticacaoRaw): ApiResult<TokenResponse>{

            return service.autentica(raw)
    }



    suspend fun getVersaoAppAtual(app: String): VersaoApp?{

        var versaoApp: VersaoApp? = null

        when (val r = service.getVersaoAppAtual(app)) {
            is ApiResult.Success -> {

                versaoApp = r.data
            }
            is ApiResult.Error -> {

                println("Falhou: status=${r.status} msg=${r.message}\n${r.payload}")
            }
        }
        return versaoApp
    }

    suspend fun downloadManifestoIos(app: String, versao: String): ApiResult<String> =
        service.downloadManifestoIos(app, versao)

    fun manifestoIosUrl(app: String, versao: String): String =
        service.manifestoIosUrl(app, versao)

}