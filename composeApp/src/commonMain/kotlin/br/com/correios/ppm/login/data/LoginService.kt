package br.com.correios.ppm.login.data

import br.com.correios.ppm.login.application.Usuario
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType

class LoginService(private val client: HttpClient){

    val baseUrl = "https://applogisticahom.correios.com.br"

    suspend fun getUsuarioLogado(): UsuarioRaw? {

        var response : HttpResponse = client.get("${baseUrl}/v1/usuario").body()

        println(response)


        return client.get("${baseUrl}/v1/usuario").body()
    }

    suspend fun autentica(request: AutenticacaoRaw?): TokenResponse? {
        return client.post("/v1/autenticacao") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    suspend fun getVersaoAppAtual(app: String?): VersaoApp? {
        return client.get("/lojaapp/v1/aplicativos/$app/versao-atual").body()
    }
}