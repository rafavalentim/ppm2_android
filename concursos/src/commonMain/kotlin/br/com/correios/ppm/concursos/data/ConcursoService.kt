package br.com.correios.ppm.concursos.data

import br.com.correios.ppm.data.ApiResult
import br.com.correios.ppm.data.requestSmart
import io.ktor.client.HttpClient

class ConcursoService(private val client: HttpClient, private val baseUrl: String) {

    suspend fun getConcursoPorId(id: String): ApiResult<ConcursoRaw> =
        client.requestSmart(
            baseUrl = baseUrl,
            path = "/rest/concursos/v1/$id"
        )
}
