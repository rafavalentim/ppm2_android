package br.com.correios.ppm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import br.com.correios.ppm.ui.screens.splash.SplashMainScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            SplashMainScreen()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    //App()
    SplashMainScreen()
}