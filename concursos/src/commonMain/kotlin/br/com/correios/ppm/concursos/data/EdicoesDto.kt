package br.com.correios.ppm.concursos.data

import br.com.correios.ppm.data.BaseRaw
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EdicoesDto(
    @SerialName("codigo") val codigo: String? = null,
    @SerialName("descricao") val descricao: String? = null,
    @SerialName("edicaoAtual") val edicaoAtual: String? = null,
    @SerialName("success") val success: String? = null,
    @SerialName("message") val message: String? = null,
    var base: BaseRaw? = null
) {
    val msgErro get() = base?.msg
}
