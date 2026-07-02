package br.com.correios.ppm.concursos.presentation

import br.com.correios.ppm.BaseViewModel
import br.com.correios.ppm.concursos.application.ConcursoUseCase
import br.com.correios.ppm.data.KeyValueStorage
import br.com.correios.ppm.ui.components.UiEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainConcursosViewModel(
    private val useCase: ConcursoUseCase,
    private val preferences: KeyValueStorage
) : BaseViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isConnected = MutableStateFlow(true)
    val isConnected: StateFlow<Boolean> = _isConnected.asStateFlow()

    private val _nomeUsuario = MutableStateFlow(preferences.getString("pref_nome") ?: "")
    val nomeUsuario: StateFlow<String> = _nomeUsuario.asStateFlow()

    private val _matricula = MutableStateFlow(preferences.getString("pref_username") ?: "")
    val matricula: StateFlow<String> = _matricula.asStateFlow()

    private val _events = MutableSharedFlow<UiEvent>(extraBufferCapacity = 1)
    val events: SharedFlow<UiEvent> = _events

    fun truncateStringWithEllipsis(maxLength: Int, text: String): String =
        if (text.length > maxLength) text.take(maxLength) + "…" else text
}
