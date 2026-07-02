package br.com.correios.ppm.data

import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.request
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpMethod
import io.ktor.http.URLBuilder
import io.ktor.http.encodedPath
import io.ktor.http.isSuccess
import io.ktor.http.takeFrom
import kotlinx.serialization.json.Json


suspend inline fun <reified T> HttpClient.requestSmart(
    baseUrl: String,
    path: String,
    method: HttpMethod = HttpMethod.Get,
    crossinline block: HttpRequestBuilder.() -> Unit = {}
): ApiResult<T> = try {
    val resp = request {
        url {
            takeFrom(baseUrl)
            encodedPath = buildString {
                append(URLBuilder(baseUrl).encodedPath.trimEnd('/'))
                append('/')
                append(path.trimStart('/'))
            }
        }
        this.method = method
        block()
    }
    val text = runCatching { resp.bodyAsText() }.getOrNull()
    if (resp.status.isSuccess()) {
        val parsed = runCatching {
            Json { ignoreUnknownKeys = true; isLenient = true; explicitNulls = false }
                .decodeFromString<T>(text ?: "")
        }.getOrNull()
        when {
            parsed != null -> ApiResult.Success(parsed)
            T::class == String::class -> @Suppress("UNCHECKED_CAST")
            ApiResult.Success((text ?: "") as T)
            else -> ApiResult.Error(resp.status.value, "Conteúdo não-JSON", text)
        }
    } else ApiResult.Error(resp.status.value, "HTTP ${resp.status}", text)
} catch (e: Throwable) {
    ApiResult.Error(message = e.message, cause = e)
}
