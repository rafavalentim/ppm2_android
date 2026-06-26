package br.com.correios.ppm.unidade.data

import br.com.correios.ppm.data.BaseRaw
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UnidadeRaw(

    var base: BaseRaw? = null,

    @SerialName("codigoUnidade")
    var codigoUnidade: String? = null,

    @SerialName("nome")
    var nome: String? = null,

    @SerialName("sigla")
    var sigla: String? = null,

    @SerialName("dr")
    var dr: String? = null,

    @SerialName("tipo")
    var tipo: String? = null
)
