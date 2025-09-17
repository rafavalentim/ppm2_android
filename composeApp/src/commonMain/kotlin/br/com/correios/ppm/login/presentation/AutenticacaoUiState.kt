package br.com.correios.ppm.login.presentation

import br.com.correios.ppm.login.application.Autenticacao


sealed class AutenticacaoUiState(
    open val aut: Autenticacao? = Autenticacao(
        usuario = "",
        senha = ""
    )
) {
    data object Loading : AutenticacaoUiState()
    data class Success(val autenticacao: Autenticacao?) : AutenticacaoUiState()
    data class Error(val message: String) : AutenticacaoUiState()

}