package br.com.correios.ppm.concursos.presentation

import br.com.correios.ppm.concursos.application.Edicao

sealed class CadastroEdicaoUiState {
    data object Idle : CadastroEdicaoUiState()
    data object Loading : CadastroEdicaoUiState()
    data class Success(val edicoes: List<Edicao>) : CadastroEdicaoUiState()
    data class Error(val mensagem: String?) : CadastroEdicaoUiState()
}
