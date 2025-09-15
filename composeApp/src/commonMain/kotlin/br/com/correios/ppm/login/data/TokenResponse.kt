package br.com.correios.ppm.login.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TokenResponse (

    @SerialName("token")
    val token: String? = null
)