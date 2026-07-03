package br.com.correios.ppm.concursos.data

import br.com.correios.ppm.data.ApiResult
import br.com.correios.ppm.data.BaseRaw

class AtualizaStatusObjetoRepository(private val service: GestaoLogisticaService) {

    suspend fun atualizarStatusObjeto(dto: AtualizaStatusObjetosDto): ResponseAtualizaStatusObjetoDto =
        when (val r = service.atualizarStatusObjeto(dto)) {
            // o endpoint responde 200 sem corpo quando dá certo (sem o campo "success"),
            // então o sucesso HTTP em si já garante o "true" quando o corpo não traz nada.
            is ApiResult.Success -> r.data.copy(success = r.data.success ?: "true")
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
