package br.com.correios.ppm.concursos.application

data class AtualizaStatusObjeto(
    val success: Boolean,
    val message: String? = null,
    val msgErro: String? = null
)
