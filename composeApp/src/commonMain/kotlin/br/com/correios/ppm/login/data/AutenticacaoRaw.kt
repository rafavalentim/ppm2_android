package br.com.correios.ppm.login.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AutenticacaoRaw (

   @SerialName("usuario")
    val usuario: String,

    @SerialName("senha")
    val senha: String
)