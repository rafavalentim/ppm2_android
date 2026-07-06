package br.com.correios.ppm.concursos.application

import br.com.correios.ppm.concursos.data.AtualizaStatusObjetoRepository
import br.com.correios.ppm.concursos.data.AtualizaStatusObjetosDto
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.number

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

    // Atualiza o status de leitura de um objeto (contrato do endpoint sclsms).
    suspend fun atualizarStatusObjeto(
        numeroEdicao: String,
        codigoObjeto: String,
        tipoOperacao: TipoOperacao,
        dataHora: LocalDateTime,
        matricula: String?
    ): AtualizaStatusObjeto {
        val numerosObjeto = codigoObjeto.filter { it.isDigit() }
        val dto = AtualizaStatusObjetosDto(
            idBroker = "app",
            conteudo = SHORT_CODE + tipoOperacao.codigo + numeroEdicao + numerosObjeto,
            from = "app",
            numero = matricula,
            dataRecebimento = formatarDataRecebimento(dataHora),
            conta = CONTA,
            shortCode = SHORT_CODE
        )
        val raw = repository.atualizarStatusObjeto(dto)
        return AtualizaStatusObjeto(
            success = raw.success == "true",
            message = raw.message,
            msgErro = raw.msgErro
        )
    }

    private fun formatarDataRecebimento(dataHora: LocalDateTime): String {
        fun pad(valor: Int) = valor.toString().padStart(2, '0')
        return "${dataHora.year}-${pad(dataHora.month.number)}-${pad(dataHora.day)}" +
            "T${pad(dataHora.hour)}:${pad(dataHora.minute)}:${pad(dataHora.second)}.000-"
    }

    private companion object {
        const val CONTA = "12345"
        const val SHORT_CODE = "110"
    }
}
