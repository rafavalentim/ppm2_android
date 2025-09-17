package br.com.correios.ppm.login.presentation

import androidx.lifecycle.viewModelScope
import br.com.correios.ppm.BaseViewModel
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
import kotlin.toString

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


    private fun setarTokenSessao(tokenResponse: TokenResponse?){
        tokenResponse?.token?.let {
            preferences.putString("session_cookie", token.value?.token.toString())
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
            }
            .onFailure {
                _uiState.value = UsuarioUiState.Error(it.message ?: "Erro")
            }
    }



    fun onLoginClick(){

        val user = username
        val pass = autenticacaoUiState.value.aut?.senha

        if (user.value.isNullOrEmpty() || pass.isNullOrEmpty()) {
            _loginStatus.value = "Username or Password is empty"
        }else{

            _isLoading.value = true

            //Inicializando o objeto Autenticacao com os dados do login
            val autenticacao = Autenticacao(user.value, pass)

            viewModelScope.launch {

                val token = loginUseCase.autenticar(autenticacao)

                if(token == null){

                    _loginStatus.value = "Login failed"
                    _events.emit(UiEvent.ShowMessage(_loginStatus.value, actionLabel = "OK"))

                }else{

                    //Pegando o token de sessão
                    val tokenResponse: TokenResponse? = token
                    tokenResponse.let {
                        setarTokenSessao(tokenResponse)
                    }

                    //Buscar os dados do usuário logado. (Avaliar criar uma tabela no banco para
                    // armazenas as informações do usuário logado. Melhor que jogar no Preferences).
                    carregarUsuarioLogado()
                }
            }
        }
    }
}