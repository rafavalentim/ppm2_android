package br.com.correios.ppm.ui.screens.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.correios.ppm.login.presentation.LoginViewModel
import br.com.correios.ppm.splash.presentation.SplashScreenViewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.koin.compose.koinInject
import org.koin.core.Koin

class LoginScreen(val koin : Koin) : Screen{

    @Composable
    override fun Content() {
        LoginMainScreen(koin)
    }


    @Composable
    fun LoginMainScreenTest(
        koin : Koin,
        splashScreenViewModel: SplashScreenViewModel = koin.get()
    ) {

        //Implementando a navegação entre telas com o Voyager
        val navigator = LocalNavigator.currentOrThrow

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(modifier = Modifier.width(64.dp))
                Spacer(Modifier.height(24.dp))

            }
        }
    }


    @Composable
    fun LoginMainScreen(
        koin: Koin,
        loginViewModel: LoginViewModel = koinInject()
    ) {

        val navigator = LocalNavigator.currentOrThrow

        val isLoading by viewModel.isLoading.observeAsState(false)

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
                    DefaultTextFieldCorreios(R.string.form_login_login, viewModel)
                    PasswordTextFieldCorreios(viewModel)
                    SwitchWithIconExample(viewModel)
                    DefaultButton("Entrar", viewModel)
                    TextVersioApp("Versão: ${BuildConfig.VERSION_NAME}")
                }

                // Overlay de loading
                if (isLoading) {
                    LoadingScreen().LoadingScreenDescription(isLoading)
                }
            }
        }
    }












}
