package br.com.correios.ppm.login.data

import br.com.correios.ppm.data.ApiResult
import br.com.correios.ppm.data.requestSmart
import io.ktor.client.HttpClient
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.contentType

class LoginService(private val client: HttpClient){

    val baseUrl = "https://applogisticahom.correios.com.br"
    private val base = baseUrl.trimEnd('/')

    suspend fun getUsuarioLogado(): ApiResult<UsuarioRaw> =
        client.requestSmart(
            baseUrl = baseUrl,          // "https://applogisticahom.correios.com.br"
            path = "/v1/usuario"
        )

    suspend fun autentica(req: AutenticacaoRaw): ApiResult<TokenResponse> =
        client.requestSmart(
            baseUrl = baseUrl,
            path = "/v1/autenticacao",
            method = HttpMethod.Post
        ) {
            contentType(ContentType.Application.Json)
            setBody(req)
        }

//    suspend fun getVersaoAppAtual(app: String): ApiResult<VersaoApp> =
//        client.requestSmart {
//            url("${base}/lojaapp/v1/aplicativos/$app/versao-atual")
//            //method = HttpMethod.Get
//        }





}