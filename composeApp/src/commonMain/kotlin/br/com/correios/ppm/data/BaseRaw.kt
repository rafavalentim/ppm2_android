package br.com.correios.ppm.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
open class BaseRaw (
    @SerialName("status")
    var status: Int? = null,

    @SerialName("msg")
    var msg: String? = null,

    @SerialName("payload")
    var payload: String? = null,
)