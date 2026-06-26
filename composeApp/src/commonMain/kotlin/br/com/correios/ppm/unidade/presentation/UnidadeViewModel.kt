package br.com.correios.ppm.unidade.presentation

import br.com.correios.ppm.BaseViewModel
import br.com.correios.ppm.ui.components.UiEvent
import br.com.correios.ppm.unidade.application.UnidadeUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UnidadeViewModel(private val useCase: UnidadeUseCase) : BaseViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _uiState = MutableStateFlow<UnidadeUiState>(UnidadeUiState.Idle)
    val uiState: StateFlow<UnidadeUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<UiEvent>(extraBufferCapacity = 1)
    val events: SharedFlow<UiEvent> = _events

    fun buscarUnidade(id: String) = scope.launch {
        if (id.isBlank()) {
            _events.emit(UiEvent.ShowMessage("Informe o código da unidade"))
            return@launch
        }

        _isLoading.value = true
        _uiState.value = UnidadeUiState.Loading

        runCatching { useCase.buscarUnidade(id) }
            .onSuccess { unidade ->
                if (unidade.msgErro.isNullOrEmpty()) {
                    _uiState.value = UnidadeUiState.Success(unidade)
                } else {
                    _uiState.value = UnidadeUiState.Error(unidade.msgErro)
                    _events.emit(UiEvent.ShowMessage(unidade.msgErro))
                }
            }
            .onFailure {
                _uiState.value = UnidadeUiState.Error(it.message ?: "Erro")
                _events.emit(UiEvent.ShowMessage(it.message ?: "Erro ao buscar unidade"))
            }

        _isLoading.value = false
    }
}
