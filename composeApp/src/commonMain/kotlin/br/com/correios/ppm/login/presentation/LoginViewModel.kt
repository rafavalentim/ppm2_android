package br.com.correios.ppm.login.presentation

import br.com.correios.ppm.BaseViewModel
import br.com.correios.ppm.data.KeyValueStorage
import br.com.correios.ppm.login.application.LoginUseCase
import br.com.correios.ppm.login.data.TokenResponse
import br.com.correios.ppm.splash.presentation.UsuarioUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

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


    private fun setarTokenSessao(tokenResponse: TokenResponse){
        tokenResponse.token?.let {
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






}