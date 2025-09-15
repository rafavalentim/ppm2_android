package br.com.correios.ppm.login.application

data class Endereco (
    var identificacao: String? = null,
    var principal: String? = null,
    var complemento: String? = null,
    var numero: String? = null,
    var logradouro: String? = null,
    var ddiFixo: String? = null,
    var dddFixo: String? = null,
    var foneFixo: String? = null,
    var bairro: String? = null,
    var cidade: String? = null,
    var uf: String? = null,
    var codigoPostal: String? = null,
    var siglaPais: String? = null
    )