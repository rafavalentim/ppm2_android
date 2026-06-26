package br.com.correios.ppm.unidade.application

import br.com.correios.ppm.unidade.data.UnidadeRepository
import br.com.correios.ppm.unidade.data.UnidadeRaw

class UnidadeUseCase(private val repo: UnidadeRepository) {

    private fun mapToUnidade(raw: UnidadeRaw?): Unidade {
        return Unidade(
            codigoUnidade = raw?.codigoUnidade,
            nome = raw?.nome,
            sigla = raw?.sigla,
            dr = raw?.dr,
            tipo = raw?.tipo,
            msgErro = raw?.base?.payload
        )
    }

    suspend fun buscarUnidade(id: String): Unidade {
        val raw = repo.getUnidadeDeNegocioPorCodigo(id)
        return mapToUnidade(raw)
    }
}
