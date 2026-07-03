package br.com.correios.ppm.concursos.application

import br.com.correios.ppm.concursos.data.AtualizaStatusObjetoRepository
import br.com.correios.ppm.concursos.data.AtualizaStatusObjetosDto

class AtualizaStatusObjetoUseCase(private val repository: AtualizaStatusObjetoRepository) {

    // idBroker/conteudo não têm um campo correspondente óbvio no formulário de cadastro de edição;
    // mapeados para numeroEdicao/nomeEdicao até existir um contrato definitivo para esta tela.
    suspend fun atualizar(
        numeroEdicao: String,
        nomeEdicao: String,
        matricula: String?
    ): AtualizaStatusObjeto {
        val dto = AtualizaStatusObjetosDto(
            idBroker = numeroEdicao,
            conteudo = nomeEdicao,
            numero = matricula
        )
        val raw = repository.atualizarStatusObjeto(dto)
        return AtualizaStatusObjeto(
            success = raw.success == "true",
            message = raw.message,
            msgErro = raw.msgErro
        )
    }
}
