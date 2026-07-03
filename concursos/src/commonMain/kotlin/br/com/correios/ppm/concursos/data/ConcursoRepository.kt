package br.com.correios.ppm.concursos.data

import br.com.correios.ppm.data.ApiResult
import br.com.correios.ppm.data.BaseRaw

class ConcursoRepository(private val service: GestaoLogisticaService) {

    suspend fun getConcursoPorId(id: String): ConcursoRaw? {
        var concurso = ConcursoRaw()

        when (val r = service.getConcursoPorId(id)) {
            is ApiResult.Success -> concurso = r.data
            is ApiResult.Error -> {
                concurso = concurso.copy(base = BaseRaw(status = r.status, msg = r.message, payload = r.payload))
                println("Falhou: status=${r.status} msg=${r.message}\n${r.payload}")
            }
        }
        return concurso
    }
}
