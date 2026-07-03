package br.com.correios.ppm.concursos.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AtualizaStatusObjetosDto(
    @SerialName("idBroker") val idBroker: String? = null,
    @SerialName("conteudo") val conteudo: String? = null,
    @SerialName("from") val from: String? = null,
    @SerialName("numero") val numero: String? = null, // matrícula do usuário
    @SerialName("dataRecebimento") val dataRecebimento: String? = null,
    @SerialName("conta") val conta: String? = null,
    @SerialName("shortCode") val shortCode: String? = null
)
