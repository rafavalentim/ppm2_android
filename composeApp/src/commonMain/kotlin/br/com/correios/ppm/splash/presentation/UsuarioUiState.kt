package br.com.correios.ppm.splash.presentation

import br.com.correios.ppm.login.application.Usuario

sealed class UsuarioUiState {

    data object Loading : UsuarioUiState()
    data class Success(val usuario: Usuario?) : UsuarioUiState()
    data class Error(val message: String) : UsuarioUiState()
}