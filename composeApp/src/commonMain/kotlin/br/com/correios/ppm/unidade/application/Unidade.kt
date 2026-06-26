package br.com.correios.ppm.unidade.application

data class Unidade(
    val codigoUnidade: String?,
    val nome: String?,
    val sigla: String?,
    val dr: String?,
    val tipo: String?,
    val msgErro: String? = null
)
