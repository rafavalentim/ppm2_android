package br.com.correios.ppm.ui.screens.login

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.correios.ppm.login.presentation.LoginViewModel
import br.com.correios.ppm.ui.components.DefaultButton
import br.com.correios.ppm.ui.components.DefaultTextFieldCorreios
import br.com.correios.ppm.ui.components.LoadingScreen
import br.com.correios.ppm.ui.components.LogoScreen
import br.com.correios.ppm.ui.components.PasswordTextFieldCorreios
import br.com.correios.ppm.ui.components.SwitchWithIconExample
import br.com.correios.ppm.ui.components.TextVersioApp
import br.com.correios.ppm.ui.components.UiEvent
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.koin.compose.koinInject
import org.koin.core.Koin
import ppm_kmp.composeapp.generated.resources.Res
import ppm_kmp.composeapp.generated.resources.form_login_login


class LoginDescriptionScreen(val koin : Koin) : Screen{

    @Composable
    override fun Content() {
        LoginMainScreen(koin)
    }

    @Composable
    fun LoginMainScreen(
        koin: Koin,
        loginViewModel: LoginViewModel = koinInject()
    ) {

        val navigator = LocalNavigator.currentOrThrow

        val isLoading = loginViewModel.isLoading.collectAsState()

        val autenticacaoState by loginViewModel.autenticacaoUiState.collectAsState()

        val snackbarHostState = remember { SnackbarHostState() }

        LaunchedEffect(Unit) {
            loginViewModel.events.collect { event ->
                when (event) {
                    is UiEvent.ShowMessage -> {
                        snackbarHostState.showSnackbar(event.message)
                    }
                }
            }
        }

        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { innerPadding ->

            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Conteúdo com rolagem
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 16.dp, vertical = 24.dp)
                    ) {
                        LogoScreen(5)
                        DefaultTextFieldCorreios(Res.string.form_login_login, loginViewModel, autenticacaoState)
                        PasswordTextFieldCorreios(autenticacaoState)
                        SwitchWithIconExample(loginViewModel)
                        DefaultButton("Entrar", loginViewModel)
                        TextVersioApp("Versão: 0.0.0") //Alterar para a versão da aplicação posteriormente.
                    }

                    // Overlay de loading
                    if (isLoading.value) {
                        LoadingScreen(isLoading.value)
                    }
                }
            }



        }


    }
}
