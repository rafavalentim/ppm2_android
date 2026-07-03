package br.com.correios.ppm.concursos.data

import br.com.correios.ppm.data.ApiResult
import br.com.correios.ppm.data.BaseRaw

class AtualizaStatusObjetoRepository(private val service: GestaoLogisticaService) {

    suspend fun atualizarStatusObjeto(dto: AtualizaStatusObjetosDto): ResponseAtualizaStatusObjetoDto =
        when (val r = service.atualizarStatusObjeto(dto)) {
            is ApiResult.Success -> r.data
            is ApiResult.Error -> {
                println("Falhou: status=${r.status} msg=${r.message}\n${r.payload}")
                ResponseAtualizaStatusObjetoDto(
                    success = "false",
                    message = "Erro no processamento das informações.",
                    base = BaseRaw(status = r.status, msg = r.message, payload = r.payload)
                )
            }
        }
}
