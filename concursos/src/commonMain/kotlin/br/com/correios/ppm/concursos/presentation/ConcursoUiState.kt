package br.com.correios.ppm.concursos.presentation

import br.com.correios.ppm.concursos.application.Concurso

sealed class ConcursoUiState {
    data object Idle : ConcursoUiState()
    data object Loading : ConcursoUiState()
    data class Success(val concurso: Concurso) : ConcursoUiState()
    data class Error(val mensagem: String?) : ConcursoUiState()
}
