package br.com.correios.ppm.concursos.presentation

import br.com.correios.ppm.BaseViewModel
import br.com.correios.ppm.concursos.application.ConcursoUseCase
import br.com.correios.ppm.ui.components.UiEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ConcursoViewModel(private val useCase: ConcursoUseCase) : BaseViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _uiState = MutableStateFlow<ConcursoUiState>(ConcursoUiState.Idle)
    val uiState: StateFlow<ConcursoUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<UiEvent>(extraBufferCapacity = 1)
    val events: SharedFlow<UiEvent> = _events

    fun buscarConcurso(id: String) = scope.launch {
        if (id.isBlank()) {
            _events.emit(UiEvent.ShowMessage("Informe o código do concurso"))
            return@launch
        }

        _isLoading.value = true
        _uiState.value = ConcursoUiState.Loading

        runCatching { useCase.buscarConcurso(id) }
            .onSuccess { concurso ->
                if (concurso.msgErro.isNullOrEmpty()) {
                    _uiState.value = ConcursoUiState.Success(concurso)
                } else {
                    _uiState.value = ConcursoUiState.Error(concurso.msgErro)
                    _events.emit(UiEvent.ShowMessage(concurso.msgErro))
                }
            }
            .onFailure {
                _uiState.value = ConcursoUiState.Error(it.message ?: "Erro")
                _events.emit(UiEvent.ShowMessage(it.message ?: "Erro ao buscar concurso"))
            }

        _isLoading.value = false
    }
}
