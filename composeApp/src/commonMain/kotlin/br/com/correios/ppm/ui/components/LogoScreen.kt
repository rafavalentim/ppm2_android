package br.com.correios.ppm.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import ppm_kmp.composeapp.generated.resources.Res
import ppm_kmp.composeapp.generated.resources.correios_horizontal_dark
import ppm_kmp.composeapp.generated.resources.correios_horizontal_light

@Composable
fun LogoScreen(
    topDp : Int
) {
    val isDarkTheme = isSystemInDarkTheme()
    val logoResId = if(isDarkTheme){
        Res.drawable.correios_horizontal_dark
    }else{
        Res.drawable.correios_horizontal_light
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = topDp.dp, end = 16.dp, bottom = 16.dp)
    ) {
        Image(
            painter = painterResource(logoResId),
            contentDescription = "Logo centralizada",
            modifier = Modifier
                .height(54.dp)
                .width(248.dp)
                .align(Alignment.Center)
        )
    }
}