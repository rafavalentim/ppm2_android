package br.com.correios.ppm.ui.screens.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import ppm_kmp.composeapp.generated.resources.Res
import ppm_kmp.composeapp.generated.resources.icone

@Composable
fun SplashMainScreen() {
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
                modifier = Modifier.width(64.dp),

                //color = MaterialTheme.colorScheme.onBackground,
                //trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )
            Image(
                painter = painterResource(
                    resource = Res.drawable.icone
                ),
                contentDescription = "correios"
            )
        }
    }
}

// Preview LIGHT
//@Preview
//@Composable
//fun MainScreen_Light_Preview() {
//    AppTheme(darkTheme = false) { // ou AppTheme(useDarkTheme = false)
//        SplashMainScreen()
//    }
//}

// Preview DARK
//@Preview
//@Composable
//fun MainScreen_Dark_Preview() {
//    AppTheme(darkTheme = true) { // ou AppTheme(useDarkTheme = true)
//        SplashMainScreen()
//    }
//}
