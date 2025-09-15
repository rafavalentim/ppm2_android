package br.com.correios.ppm.login.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VersaoApp (

    @SerialName("noIdentificador")
    var noIdentificador: String? = null,

    @SerialName("nuVersaoLoja")
    var nuVersaoLoja: Long? = null,

    @SerialName("dhPublicacao")
    var dhPublicacao: String? = null,

    @SerialName("nuVersao")
    var nuVersao: String? = null,

    @SerialName("nuCompilacao")
    var nuCompilacao: String? = null,

    @SerialName("txComentario")
    var txComentario: String? = null,

    @SerialName("urlDownload")
    var urlDownload: String? = null,
)