package br.com.correios.ppm.concursos.presentation

import br.com.correios.ppm.BaseViewModel
import br.com.correios.ppm.concursos.application.AtualizaStatusObjetoUseCase
import br.com.correios.ppm.concursos.application.EdicaoUseCase
import br.com.correios.ppm.data.KeyValueStorage
import br.com.correios.ppm.ui.components.UiEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CadastroEdicaoViewModel(
    private val edicaoUseCase: EdicaoUseCase,
    private val atualizaStatusObjetoUseCase: AtualizaStatusObjetoUseCase,
    private val preferences: KeyValueStorage
) : BaseViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isConnected = MutableStateFlow(true)
    val isConnected: StateFlow<Boolean> = _isConnected.asStateFlow()

    private val _uiState = MutableStateFlow<CadastroEdicaoUiState>(CadastroEdicaoUiState.Idle)
    val uiState: StateFlow<CadastroEdicaoUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<UiEvent>(extraBufferCapacity = 1)
    val events: SharedFlow<UiEvent> = _events

    private val _numeroEdicao = MutableStateFlow("")
    val numeroEdicao: StateFlow<String> = _numeroEdicao.asStateFlow()

    private val _nomeEdicao = MutableStateFlow("")
    val nomeEdicao: StateFlow<String> = _nomeEdicao.asStateFlow()

    private val _erroNumeroEdicao = MutableStateFlow(false)
    val erroNumeroEdicao: StateFlow<Boolean> = _erroNumeroEdicao.asStateFlow()

    private val _erroNomeEdicao = MutableStateFlow(false)
    val erroNomeEdicao: StateFlow<Boolean> = _erroNomeEdicao.asStateFlow()

    fun truncateStringWithEllipsis(maxLength: Int, text: String): String =
        if (text.length > maxLength) text.take(maxLength) + "…" else text

    fun onNumeroEdicaoChanged(value: String) {
        _numeroEdicao.value = value
        if (value.isNotBlank()) _erroNumeroEdicao.value = false
    }

    fun onNomeEdicaoChanged(value: String) {
        _nomeEdicao.value = value
        if (value.isNotBlank()) _erroNomeEdicao.value = false
    }

    fun onValidarNumeroEdicao() {
        _erroNumeroEdicao.value = _numeroEdicao.value.isBlank()
    }

    fun onValidarNomeEdicao() {
        _erroNomeEdicao.value = _nomeEdicao.value.isBlank()
    }

    fun carregarEdicoes() = scope.launch {
        _isLoading.value = true
        _uiState.value = CadastroEdicaoUiState.Loading

        runCatching { edicaoUseCase.buscarEdicoes() }
            .onSuccess { edicoes ->
                val erro = edicoes.firstOrNull { !it.msgErro.isNullOrEmpty() }?.msgErro
                if (erro == null) {
                    _uiState.value = CadastroEdicaoUiState.Success(edicoes)
                } else {
                    _uiState.value = CadastroEdicaoUiState.Error(erro)
                    _events.emit(UiEvent.ShowMessage(erro))
                }
            }
            .onFailure {
                _uiState.value = CadastroEdicaoUiState.Error(it.message ?: "Erro")
                _events.emit(UiEvent.ShowMessage(it.message ?: "Erro ao buscar edições"))
            }

        _isLoading.value = false
    }

    fun cadastrar() = scope.launch {
        _isLoading.value = true

        runCatching {
            atualizaStatusObjetoUseCase.atualizar(
                numeroEdicao = _numeroEdicao.value,
                nomeEdicao = _nomeEdicao.value,
                matricula = preferences.getString("pref_username")
            )
        }
            .onSuccess { resultado ->
                val mensagem = if (resultado.success) {
                    resultado.message ?: "Cadastro realizado com sucesso"
                } else {
                    resultado.msgErro ?: resultado.message ?: "Erro ao cadastrar"
                }
                if (resultado.success) {
                    preferences.putString(PREF_NUMERO_EDICAO, _numeroEdicao.value)
                    preferences.putString(PREF_NOME_EDICAO, _nomeEdicao.value)
                }
                _events.emit(UiEvent.ShowMessage(mensagem))
            }
            .onFailure {
                _events.emit(UiEvent.ShowMessage(it.message ?: "Erro ao cadastrar"))
            }

        _isLoading.value = false
    }

    companion object {
        const val PREF_NUMERO_EDICAO = "pref_numeroEdicao"
        const val PREF_NOME_EDICAO = "pref_nomeEdicao"
    }
}
