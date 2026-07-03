package br.com.correios.ppm.concursos.data

import br.com.correios.ppm.data.BaseRaw
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseAtualizaStatusObjetoDto(
    @SerialName("success") val success: String? = null,
    @SerialName("message") val message: String? = null,
    @SerialName("msgs") val msgs: List<String?>? = emptyList(),
    @SerialName("date") val date: String? = null,
    @SerialName("path") val path: String? = null,
    var base: BaseRaw? = null
) {
    val msgErro get() = base?.msg
}
