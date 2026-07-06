package br.com.correios.ppm.login.presentation

import androidx.lifecycle.viewModelScope
import br.com.correios.ppm.BaseViewModel
import br.com.correios.ppm.data.ApiResult
import br.com.correios.ppm.data.KeyValueStorage
import br.com.correios.ppm.login.application.Autenticacao
import br.com.correios.ppm.login.application.LoginUseCase
import br.com.correios.ppm.login.data.TokenResponse
import br.com.correios.ppm.splash.presentation.UsuarioUiState
import br.com.correios.ppm.ui.components.UiEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private  val loginUseCase: LoginUseCase
): BaseViewModel() {

    val preferences = KeyValueStorage()

    private val _isLoading = MutableStateFlow(false)
    val isLoading : StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _uiState = MutableStateFlow<UsuarioUiState>(UsuarioUiState.Loading)
    val uiState: StateFlow<UsuarioUiState> = _uiState.asStateFlow()

    private val _autenticacaoUiState = MutableStateFlow<AutenticacaoUiState>(AutenticacaoUiState.Loading)
    val autenticacaoUiState: StateFlow<AutenticacaoUiState> = _autenticacaoUiState.asStateFlow()

    private val _token = MutableStateFlow<TokenResponse?>(null)
    val token : StateFlow<TokenResponse?> = _token.asStateFlow()

    private val _username = MutableStateFlow("")
    val username = _username.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    private val _loginStatus = MutableStateFlow("")
    val loginStatus = _loginStatus.asStateFlow()

    //Objeto para usar no lugar do Toast em Compose
    private val _events = MutableSharedFlow<UiEvent>(extraBufferCapacity = 1)
    val events: SharedFlow<UiEvent> = _events



    fun onUsernameChanged(value: String) {
        _username.value = value
        // se precisar refletir em Autenticacao, faça aqui
        // _autenticacao.update { it.copy(usuario = value) }
    }

    fun onPasswordChanged(value: String) {
        _password.value = value
    }


    private fun setarTokenSessao(tokenResponse: TokenResponse?){
        tokenResponse?.token?.let {
            preferences.putString("session_cookie", it)
        }
    }

    fun preencherViewComUltimoUsuarioLogado(): String?{

        val isRememberLogin = preferences.getBoolean("remember_login", null)
        val userName = preferences.getString("pref_username")

        if(isRememberLogin == true && !userName.isNullOrBlank()){
            return userName
        }else{
            return  ""
        }
    }

    fun setRememberLogin(isRemember : Boolean){
        if(isRemember){
            preferences.putBoolean("remember_login", true)
        }else{
            preferences.putBoolean("remember_login", false)
        }
    }


    fun carregarUsuarioLogado() = scope.launch {
        _uiState.value = UsuarioUiState.Loading

        runCatching { loginUseCase.fetchUsuarioLogado() }
            .onSuccess {
                _uiState.value = UsuarioUiState.Success(it)

                if(it?.msgErro.isNullOrEmpty()){
                    it?.nome?.let { nome -> preferences.putString("pref_nome", nome) }
                    it?.login?.let { login -> preferences.putString("pref_username", login) }
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


    fun onLoginClick() {

        val user = username.value
        val pass = password.value

        if (user.isNullOrEmpty() || pass.isNullOrEmpty()) {
            viewModelScope.launch {
                _events.emit(UiEvent.ShowMessage("Username or Password is empty"))
            }
            return
        }

        viewModelScope.launch {
            _isLoading.value = true

            try {
                val autenticacao = Autenticacao(user, pass)

                when (val r = loginUseCase.autenticar(autenticacao)) {

                    is ApiResult.Success -> {
                        val token = r.data

                        if (token.token.isNullOrEmpty()) {
                            _autenticacaoUiState.value =
                                AutenticacaoUiState.Error("Erro ao obter o token de sessão")

                            _events.emit(
                                UiEvent.ShowMessage("Ocorreu um erro, token vazio")
                            )
                        } else {
                            _autenticacaoUiState.value =
                                AutenticacaoUiState.Success(autenticacao)

                            setarTokenSessao(token)

                            carregarUsuarioLogado()

                            _events.emit(UiEvent.ShowMessage("Sucesso"))

                            _events.emit(UiEvent.LoginSuccess)
                        }
                    }

                    is ApiResult.Error -> {
                        _autenticacaoUiState.value =
                            AutenticacaoUiState.Error(r.payload ?: "Erro")

                        _events.emit(
                            UiEvent.ShowMessage(r.payload ?: "Erro")
                        )
                    }
                }

            } catch (e: Exception) {
                _autenticacaoUiState.value =
                    AutenticacaoUiState.Error(e.message ?: "Erro inesperado")

                _events.emit(
                    UiEvent.ShowMessage(e.message ?: "Erro inesperado")
                )

            } finally {
                _isLoading.value = false
            }
        }
    }
}