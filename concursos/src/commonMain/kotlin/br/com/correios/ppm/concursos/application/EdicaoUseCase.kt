package br.com.correios.ppm.concursos.application

import br.com.correios.ppm.concursos.data.EdicaoRepository

class EdicaoUseCase(private val repository: EdicaoRepository) {

    suspend fun buscarEdicoes(): List<Edicao> =
        repository.getListaEdicoes().map { raw ->
            Edicao(
                codigo = raw.codigo,
                descricao = raw.descricao,
                edicaoAtual = raw.edicaoAtual,
                msgErro = raw.msgErro
            )
        }
}
