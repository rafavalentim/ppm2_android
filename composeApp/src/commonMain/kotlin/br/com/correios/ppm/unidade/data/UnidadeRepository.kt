package br.com.correios.ppm.unidade.data

import br.com.correios.ppm.data.ApiResult
import br.com.correios.ppm.data.BaseRaw

class UnidadeRepository(private val service: UnidadeService) {

    suspend fun getUnidadeDeNegocioPorCodigo(id: String): UnidadeRaw? {
        var unidade = UnidadeRaw()

        when (val r = service.getUnidadeDeNegocioPorCodigo(id)) {
            is ApiResult.Success -> unidade = r.data
            is ApiResult.Error -> {
                unidade = unidade.copy(base = BaseRaw(status = r.status, msg = r.message, payload = r.payload))
                println("Falhou: status=${r.status} msg=${r.message}\n${r.payload}")
            }
        }
        return unidade
    }
}
