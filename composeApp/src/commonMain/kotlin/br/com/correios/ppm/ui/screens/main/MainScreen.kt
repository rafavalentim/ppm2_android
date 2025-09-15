package br.com.correios.ppm.ui.screens.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import br.com.correios.ppm.splash.presentation.SplashScreenViewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.koin.core.Koin

class MainScreen(val koin : Koin) : Screen{

    @Composable
    override fun Content() {
        MainMainScreen(koin)
    }


    @Composable
    fun MainMainScreen(
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
                CircularProgressIndicator(
                    modifier = Modifier
                    .width(64.dp),
                   color = Color.Red
                )
                Spacer(Modifier.height(24.dp))

            }
        }
    }
}