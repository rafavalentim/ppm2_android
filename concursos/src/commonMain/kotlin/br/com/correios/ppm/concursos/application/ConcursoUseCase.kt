package br.com.correios.ppm.concursos.application

import br.com.correios.ppm.concursos.data.ConcursoRepository

class ConcursoUseCase(private val repository: ConcursoRepository) {

    suspend fun buscarConcurso(id: String): Concurso {
        val raw = repository.getConcursoPorId(id)
        return Concurso(
            id = raw?.id,
            titulo = raw?.titulo,
            descricao = raw?.descricao,
            dataAbertura = raw?.dataAbertura,
            dataEncerramento = raw?.dataEncerramento,
            situacao = raw?.situacao,
            msgErro = raw?.msgErro
        )
    }
}
