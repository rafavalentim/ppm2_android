package br.com.correios.ppm.concursos.data

import br.com.correios.ppm.data.ApiResult
import br.com.correios.ppm.data.requestSmart
import io.ktor.client.HttpClient
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.contentType

class GestaoLogisticaService(private val client: HttpClient, private val baseUrl: String) {

    suspend fun getConcursoPorId(id: String): ApiResult<ConcursoRaw> =
        client.requestSmart(
            baseUrl = baseUrl,
            path = "/rest/concursos/v1/$id"
        )

    suspend fun atualizarStatusObjeto(dto: AtualizaStatusObjetosDto): ApiResult<ResponseAtualizaStatusObjetoDto> =
        client.requestSmart(
            baseUrl = baseUrl,
            path = "/sgcon/sclsms",
            method = HttpMethod.Post
        ) {
            contentType(ContentType.Application.Json)
            setBody(dto)
        }

    suspend fun getListaEdicoes(): ApiResult<List<EdicoesDto>> =
        client.requestSmart(
            baseUrl = baseUrl,
            path = "/sgcon/scledicoes"
        )
}
