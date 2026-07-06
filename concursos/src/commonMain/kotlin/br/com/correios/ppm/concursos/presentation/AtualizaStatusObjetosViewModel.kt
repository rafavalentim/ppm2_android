package br.com.correios.ppm.concursos.presentation

import br.com.correios.ppm.BaseViewModel
import br.com.correios.ppm.concursos.application.AtualizaStatusObjetoUseCase
import br.com.correios.ppm.concursos.application.TipoOperacao
import br.com.correios.ppm.data.KeyValueStorage
import br.com.correios.ppm.ui.components.UiEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
class AtualizaStatusObjetosViewModel(
    private val useCase: AtualizaStatusObjetoUseCase,
    private val preferences: KeyValueStorage
) : BaseViewModel() {

    private val zonaBrasilia = TimeZone.of("America/Sao_Paulo")

    val tipoOperacaoOptions = TipoOperacao.options

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isConnected = MutableStateFlow(true)
    val isConnected: StateFlow<Boolean> = _isConnected.asStateFlow()

    private val _events = MutableSharedFlow<UiEvent>(extraBufferCapacity = 1)
    val events: SharedFlow<UiEvent> = _events

    private val _tipoSelecionado = MutableStateFlow(tipoOperacaoOptions.first())
    val tipoSelecionado: StateFlow<String> = _tipoSelecionado.asStateFlow()

    private val _numeroEdicao = MutableStateFlow(preferences.getString(CadastroEdicaoViewModel.PREF_NUMERO_EDICAO) ?: "")
    val numeroEdicao: StateFlow<String> = _numeroEdicao.asStateFlow()

    private val _nomeEdicao = MutableStateFlow(preferences.getString(CadastroEdicaoViewModel.PREF_NOME_EDICAO) ?: "")
    val nomeEdicao: StateFlow<String> = _nomeEdicao.asStateFlow()

    private val _codigoObjeto = MutableStateFlow("")
    val codigoObjeto: StateFlow<String> = _codigoObjeto.asStateFlow()

    private var dataSelecionadaValor: LocalDate = agoraEmBrasilia().date

    private val _dataSelecionada = MutableStateFlow(dataSelecionadaValor.formatarBr())
    val dataSelecionada: StateFlow<String> = _dataSelecionada.asStateFlow()

    private val _horaSelecionada = MutableStateFlow(agoraEmBrasilia().formatarHora())
    val horaSelecionada: StateFlow<String> = _horaSelecionada.asStateFlow()

    private val _erroCodigoObjeto = MutableStateFlow(false)
    val erroCodigoObjeto: StateFlow<Boolean> = _erroCodigoObjeto.asStateFlow()

    private val _erroNumeroEdicao = MutableStateFlow(false)
    val erroNumeroEdicao: StateFlow<Boolean> = _erroNumeroEdicao.asStateFlow()

    private val _erroNomeEdicao = MutableStateFlow(false)
    val erroNomeEdicao: StateFlow<Boolean> = _erroNomeEdicao.asStateFlow()

    private val _showBarcodeScanner = MutableStateFlow(false)
    val showBarcodeScanner: StateFlow<Boolean> = _showBarcodeScanner.asStateFlow()

    fun onShowBarcodeChanged(show: Boolean) {
        _showBarcodeScanner.value = show
    }

    fun onValidarCodigoObjeto() {
        _erroCodigoObjeto.value = _codigoObjeto.value.isBlank()
    }

    fun onValidarNumeroEdicao() {
        _erroNumeroEdicao.value = _numeroEdicao.value.isBlank()
    }

    fun onValidarNomeEdicao() {
        _erroNomeEdicao.value = _nomeEdicao.value.isBlank()
    }

    fun onTipoSelecionadoChange(novo: String) {
        _tipoSelecionado.value = novo
    }

    fun onCodigoObjetoChange(novo: String) {
        _codigoObjeto.value = novo
        if (novo.isNotBlank()) _erroCodigoObjeto.value = false
    }

    fun onDataSelecionadaChange(epochMillis: Long) {
        val data = Instant.fromEpochMilliseconds(epochMillis).toLocalDateTime(TimeZone.UTC).date
        dataSelecionadaValor = data
        _dataSelecionada.value = data.formatarBr()
    }

    fun onHoraSelecionadaChange(hour: Int, minute: Int) {
        fun pad(valor: Int) = valor.toString().padStart(2, '0')
        _horaSelecionada.value = "${pad(hour)}:${pad(minute)}"
    }

    fun atualizarStatusObjetos() = scope.launch {
        onValidarCodigoObjeto()
        onValidarNumeroEdicao()
        onValidarNomeEdicao()

        if (_erroCodigoObjeto.value || _erroNumeroEdicao.value || _erroNomeEdicao.value) {
            _events.emit(UiEvent.ShowMessage("É necessário preencher todos os campos."))
            return@launch
        }

        _isLoading.value = true

        val agora = agoraEmBrasilia()
        val dataHora = LocalDateTime(dataSelecionadaValor, agora.time)

        runCatching {
            useCase.atualizarStatusObjeto(
                numeroEdicao = _numeroEdicao.value,
                codigoObjeto = _codigoObjeto.value,
                tipoOperacao = TipoOperacao.fromLabel(_tipoSelecionado.value),
                dataHora = dataHora,
                matricula = preferences.getString("pref_username")
            )
        }
            .onSuccess { resultado ->
                val mensagem = if (resultado.success) {
                    resultado.message ?: "Atualização realizada com sucesso"
                } else {
                    resultado.msgErro ?: resultado.message ?: "Erro ao atualizar"
                }
                _events.emit(UiEvent.ShowMessage(mensagem))
                if (resultado.success) {
                    onCodigoObjetoChange("")
                }
            }
            .onFailure {
                _events.emit(UiEvent.ShowMessage(it.message ?: "Erro ao atualizar status do objeto"))
            }

        _isLoading.value = false
    }

    private fun agoraEmBrasilia(): LocalDateTime = Clock.System.now().toLocalDateTime(zonaBrasilia)

    private fun LocalDate.formatarBr(): String {
        fun pad(valor: Int) = valor.toString().padStart(2, '0')
        return "${pad(day)}/${pad(month.number)}/$year"
    }

    private fun LocalDateTime.formatarHora(): String {
        fun pad(valor: Int) = valor.toString().padStart(2, '0')
        return "${pad(hour)}:${pad(minute)}"
    }
}
