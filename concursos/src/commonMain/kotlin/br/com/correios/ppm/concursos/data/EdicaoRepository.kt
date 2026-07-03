package br.com.correios.ppm.concursos.data

import br.com.correios.ppm.data.ApiResult
import br.com.correios.ppm.data.BaseRaw

class EdicaoRepository(private val service: GestaoLogisticaService) {

    suspend fun getListaEdicoes(): List<EdicoesDto> =
        when (val r = service.getListaEdicoes()) {
            is ApiResult.Success -> r.data
            is ApiResult.Error -> {
                println("Falhou: status=${r.status} msg=${r.message}\n${r.payload}")
                listOf(
                    EdicoesDto(
                        success = "false",
                        message = "Erro no processamento das informações.",
                        base = BaseRaw(status = r.status, msg = r.message, payload = r.payload)
                    )
                )
            }
        }
}
