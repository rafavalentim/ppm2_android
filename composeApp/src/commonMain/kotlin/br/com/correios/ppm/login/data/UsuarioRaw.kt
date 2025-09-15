package br.com.correios.ppm.login.data


import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class UsuarioRaw (
    @SerialName("nome")
    var nome: String? = null,

    @SerialName("email")
    var email: String? = null,

    @SerialName("login")
    var login: String? = null,

    @SerialName("endereco")
    var endereco: EnderecoRaw? = null,

    @SerialName("unidadeSRO")
    var unidadeSRO: String? = null,

    @SerialName("distritoPostal")
    var distritoPostal: String? = null,

    @SerialName("lotacao")
    var lotacao: LotacaoRaw? = null
)