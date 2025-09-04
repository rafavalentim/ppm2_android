package br.com.correios.ppm

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Ppm_android",
    ) {
        App()
    }
}