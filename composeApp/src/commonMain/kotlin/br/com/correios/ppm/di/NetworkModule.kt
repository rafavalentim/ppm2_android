package br.com.correios.ppm.di

import br.com.correios.ppm.config.SessaoExpiradaException
import br.com.correios.ppm.data.KeyValueStorage
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import io.ktor.client.statement.request
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

//Classe que cria os acessos às api's com o Ktor, substituto do Retrofit
val networkModule = module {
    single {
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }

            val storage = KeyValueStorage()

            // Adiciona Authorization em todas as requisições
            defaultRequest {
                val token = storage.getString("session_cookie", "")
                if (!token.isNullOrEmpty()) {
                    header("Authorization", "Bearer $token")
                }
            }

            // Intercepta respostas para verificar se sessão expirou
            expectSuccess = false
            HttpResponseValidator {
                validateResponse { response ->
                    val url = response.request.url.toString()
                    if (url.contains("cas") && url.contains("/login")) {
                        storage.getString( "session_cookie", "")
                        throw SessaoExpiradaException() as Throwable
                    }
                }
            }
        }
    }
}