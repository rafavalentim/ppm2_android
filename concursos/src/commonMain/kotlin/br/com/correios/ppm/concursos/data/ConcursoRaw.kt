package br.com.correios.ppm.concursos.data

import br.com.correios.ppm.data.BaseRaw
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ConcursoRaw(
    @SerialName("id") val id: String? = null,
    @SerialName("titulo") val titulo: String? = null,
    @SerialName("descricao") val descricao: String? = null,
    @SerialName("dataAbertura") val dataAbertura: String? = null,
    @SerialName("dataEncerramento") val dataEncerramento: String? = null,
    @SerialName("situacao") val situacao: String? = null,
    var base: BaseRaw? = null
) {
    val msgErro get() = base?.msg
}
