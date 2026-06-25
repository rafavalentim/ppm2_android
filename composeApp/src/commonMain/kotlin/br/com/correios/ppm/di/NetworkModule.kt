package br.com.correios.ppm.di

import br.com.correios.ppm.config.SessaoExpiradaException
import br.com.correios.ppm.data.KeyValueStorage
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.accept
import io.ktor.client.request.header
import io.ktor.client.statement.request
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

//Classe que cria os acessos às api's com o Ktor, substituto do Retrofit
val networkModule = module {
    single {
        HttpClient {
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        isLenient = true
                        explicitNulls = false
                    }
                )
            }
            expectSuccess = false
            defaultRequest {
                accept(ContentType.Any) // aceita texto/HTML/JSON/etc.
                val token = KeyValueStorage().getString("session_cookie", "")
                if (!token.isNullOrEmpty()) header(HttpHeaders.Cookie, token)
            }
        }
    }
}