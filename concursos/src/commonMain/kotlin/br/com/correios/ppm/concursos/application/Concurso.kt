package br.com.correios.ppm.concursos.application

data class Concurso(
    val id: String? = null,
    val titulo: String? = null,
    val descricao: String? = null,
    val dataAbertura: String? = null,
    val dataEncerramento: String? = null,
    val situacao: String? = null,
    val msgErro: String? = null
)
