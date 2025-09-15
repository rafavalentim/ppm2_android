package br.com.correios.ppm.splash.presentation

import br.com.correios.ppm.BaseViewModel
import br.com.correios.ppm.login.application.LoginUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SplashScreenViewModel(
    private  val loginUseCase: LoginUseCase
): BaseViewModel() {

    private val _uiState = MutableStateFlow<UsuarioUiState>(UsuarioUiState.Loading)

    val uiState: StateFlow<UsuarioUiState> = _uiState.asStateFlow()

    fun carregarUsuarioLogado() = scope.launch {
        _uiState.value = UsuarioUiState.Loading

        runCatching { loginUseCase.fetchUsuarioLogado() }
            .onSuccess {
                _uiState.value = UsuarioUiState.Success(it)
            }
            .onFailure {
                _uiState.value = UsuarioUiState.Error(it.message ?: "Erro")
            }
    }


    init {
        carregarUsuarioLogado()
    }

}