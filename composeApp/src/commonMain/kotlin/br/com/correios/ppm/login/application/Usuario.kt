package br.com.correios.ppm.login.application

data class Usuario (
    var nome: String? = null,
    var email: String? = null,
    var login: String? = null,
    var endereco: Endereco? = null,
    var unidadeSRO: String? = null,
    var distritoPostal: String? = null,
    var lotacao: Lotacao? = null
)