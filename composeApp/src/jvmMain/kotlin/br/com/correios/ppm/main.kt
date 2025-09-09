package br.com.correios.ppm

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import br.com.correios.ppm.di.initKoin
import br.com.correios.ppm.ui.screens.splash.SplashMainScreen


val koin = initKoin()

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Ppm_kmp",
    ) {
       App()
    }
}