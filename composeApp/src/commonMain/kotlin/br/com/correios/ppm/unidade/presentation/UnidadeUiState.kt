package br.com.correios.ppm.unidade.presentation

import br.com.correios.ppm.unidade.application.Unidade

sealed class UnidadeUiState {
    data object Idle : UnidadeUiState()
    data object Loading : UnidadeUiState()
    data class Success(val unidade: Unidade) : UnidadeUiState()
    data class Error(val message: String) : UnidadeUiState()
}
