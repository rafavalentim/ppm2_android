package br.com.correios.ppm.login.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class LotacaoRaw(

    @SerialName("codigo")
    var codigo: String? = null,
    @SerialName("codigoDR")
    var codigoDR: String? = null,
    @SerialName("nome")
    var nome: String? = null,
    @SerialName("nomeDR")
    var nomeDR: String? = null,
    @SerialName("sigla")
    var sigla: String? = null,
    @SerialName("siglaDR")
    var siglaDR: String? = null
)
