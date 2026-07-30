package br.com.correios.ppm.splash.presentation

import br.com.correios.ppm.AppInfo
import br.com.correios.ppm.AppUpdater
import br.com.correios.ppm.BaseViewModel
import br.com.correios.ppm.PlatformType
import br.com.correios.ppm.UpdateDownloadStatus
import br.com.correios.ppm.data.ApiResult
import br.com.correios.ppm.getPlatform
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

    private val _updateAvailable = MutableStateFlow<VersaoApp?>(null)
    val updateAvailable: StateFlow<VersaoApp?> = _updateAvailable.asStateFlow()

    private val _downloadStatus = MutableStateFlow<UpdateDownloadStatus?>(null)
    val downloadStatus: StateFlow<UpdateDownloadStatus?> = _downloadStatus.asStateFlow()

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
        val bundleId = AppInfo.applicationId
        val newVersion = loginUseCase.getVersaoAppAtual(bundleId)

        if (newVersion != null && (newVersion.nuCompilacao?.toInt() ?: 0) > AppInfo.versionCode) {
            if (getPlatform().type == PlatformType.IOS) {
                instalarAtualizacaoIos(bundleId, newVersion)
            } else {
                _updateAvailable.value = newVersion
            }
        }
    }

    private suspend fun instalarAtualizacaoIos(bundleId: String, newVersion: VersaoApp) {
        val versao = newVersion.nuVersao
        if (versao == null) {
            _downloadStatus.value = UpdateDownloadStatus.Failed("Versão da atualização não informada")
            return
        }

        when (val resultado = loginUseCase.downloadManifestoIos(bundleId, versao)) {
            is ApiResult.Success -> {
                val manifestUrl = loginUseCase.manifestoIosUrl(bundleId, versao)
                AppUpdater.instalarComManifesto(manifestUrl)
            }
            is ApiResult.Error -> {
                _downloadStatus.value = UpdateDownloadStatus.Failed(
                    "Falha ao obter manifesto de atualização (${resultado.status ?: "sem conexão"})"
                )
            }
        }
    }

    fun confirmarAtualizacao() {
        val newVersion = _updateAvailable.value ?: return
        _updateAvailable.value = null
        downloadNewVersion(newVersion)
    }

    fun dismissDownloadStatus() {
        _downloadStatus.value = null
    }

    private fun downloadNewVersion(newVersion: VersaoApp) {
        AppUpdater.baixarEInstalar(newVersion) { status ->
            _downloadStatus.value = status
            if (status is UpdateDownloadStatus.Failed) {
                _events.tryEmit(UiEvent.ShowMessage("Falha ao atualizar: ${status.message} ❌"))
            }
        }
    }

    init {
        carregarUsuarioLogado()
        verificaAtualizacao()
    }

}