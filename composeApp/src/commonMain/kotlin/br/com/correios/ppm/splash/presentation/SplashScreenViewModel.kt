package br.com.correios.ppm.splash.presentation

import br.com.correios.ppm.AppInfo
import br.com.correios.ppm.AppUpdater
import br.com.correios.ppm.BaseViewModel
import br.com.correios.ppm.login.application.LoginUseCase
import br.com.correios.ppm.login.data.VersaoApp
import br.com.correios.ppm.ui.components.UiEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SplashScreenViewModel(
    private  val loginUseCase: LoginUseCase
): BaseViewModel() {

    private val _uiState = MutableStateFlow<UsuarioUiState>(UsuarioUiState.Loading)
    private val _events = MutableSharedFlow<UiEvent>(extraBufferCapacity = 1)
    val events: SharedFlow<UiEvent> = _events
    val uiState: StateFlow<UsuarioUiState> = _uiState.asStateFlow()

    fun carregarUsuarioLogado() = scope.launch {
        _uiState.value = UsuarioUiState.Loading

        runCatching { loginUseCase.fetchUsuarioLogado() }
            .onSuccess {
                _uiState.value = UsuarioUiState.Success(it)

                if(it?.msgErro.isNullOrEmpty()){
                    //disparando o snackbar
                    _events.emit(UiEvent.ShowMessage("Usuário carregado com sucesso 🚀"))
                }else{
                    _uiState.value = UsuarioUiState.Error(it.msgErro ?: "Erro")
                    // dispara snackbar
                    _events.emit(UiEvent.ShowMessage("Direcionando para a tela de login"))
                }
            }
            .onFailure {
                _uiState.value = UsuarioUiState.Error(it.message ?: "Erro")
                // dispara snackbar
                _events.emit(UiEvent.ShowMessage("Falha ao carregar usuário ❌"))
            }
    }

    private fun verificaAtualizacao() = scope.launch {
        val newVersion = loginUseCase.getVersaoAppAtual(AppInfo.applicationId)

        if (newVersion != null && (newVersion.nuCompilacao?.toInt() ?: 0) > AppInfo.versionCode) {
            downloadNewVersion(newVersion)
        }
    }

    private fun downloadNewVersion(newVersion: VersaoApp) {
        AppUpdater.baixarEInstalar(newVersion)
    }

    init {
        carregarUsuarioLogado()
        verificaAtualizacao()
    }

}