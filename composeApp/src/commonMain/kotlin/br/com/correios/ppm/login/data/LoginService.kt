package br.com.correios.ppm.login.data

import br.com.correios.ppm.data.ApiResult
import br.com.correios.ppm.data.requestBytes
import br.com.correios.ppm.data.requestSmart
import io.ktor.client.HttpClient
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.contentType

class LoginService(private val client: HttpClient, private val baseUrl: String) {

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

    suspend fun getVersaoAppAtual(app: String): ApiResult<VersaoApp> =
        client.requestSmart(
            baseUrl = baseUrl,
            path = "/lojaapp/v1/aplicativos/$app/versao-atual"
        )


    suspend fun downloadManifestoIos(app: String, versao: String): ApiResult<String> =
        client.requestSmart(
            baseUrl = baseUrl,
            path = manifestoIosPath(app, versao)
        )

    fun manifestoIosUrl(app: String, versao: String): String =
        "$baseUrl${manifestoIosPath(app, versao)}"

    private fun manifestoIosPath(app: String, versao: String): String =
        "/aplicativos/$app/versoes/$versao/manifest.plist"

    suspend fun downloadVersao(app: String, versao: String): ApiResult<ByteArray> =
        client.requestBytes(
            baseUrl = baseUrl,
            path = "/aplicativos/$app/versoes/$versao/file"
        )



}