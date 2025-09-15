package br.com.correios.ppm.login.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EnderecoRaw (
    @SerialName("identificacao")
    var identificacao: String? = null,
    @SerialName("principal")
    var principal: String? = null,
    @SerialName("complemento")
    var complemento: String? = null,
    @SerialName("numero")
    var numero: String? = null,
    @SerialName("logradouro")
    var logradouro: String? = null,
    @SerialName("ddiFixo")
    var ddiFixo: String? = null,
    @SerialName("dddFixo")
    var dddFixo: String? = null,
    @SerialName("foneFixo")
    var foneFixo: String? = null,
    @SerialName("bairro")
    var bairro: String? = null,
    @SerialName("cidade")
    var cidade: String? = null,
    @SerialName("uf")
    var uf: String? = null,
    @SerialName("codigoPostal")
    var codigoPostal: String? = null,
    @SerialName("siglaPais")
    var siglaPais: String? = null
)
