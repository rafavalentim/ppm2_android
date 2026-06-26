package br.com.correios.ppm.unidade.data

import br.com.correios.ppm.data.ApiResult
import br.com.correios.ppm.data.requestSmart
import io.ktor.client.HttpClient

class UnidadeService(private val client: HttpClient, private val baseUrl: String) {

    suspend fun getUnidadeDeNegocioPorCodigo(id: String): ApiResult<UnidadeRaw> =
        client.requestSmart(
            baseUrl = baseUrl,
            path = "/rest/unidades/v2/$id"
        )
}
