package br.com.correios.ppm.ui.screens.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import ppm_kmp.composeapp.generated.resources.Res
import ppm_kmp.composeapp.generated.resources.icone
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.graphicsLayer
import cafe.adriel.voyager.core.screen.Screen
import org.koin.core.Koin


class SplashScreen(val koin : Koin) : Screen{

    @Composable
    override fun Content() {
        SplashMainScreen(koin)
    }


}


@Composable
fun SplashMainScreen(
    koin : Koin
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
//            CircularProgressIndicator(modifier = Modifier.width(64.dp))
            Spacer(Modifier.height(24.dp))

            BouncyImage(
                painter = painterResource(Res.drawable.icone),
                contentDescription = "correios",
                amplitudeDp = 18,      // altura do “pulo”
                cycleDurationMs = 900  // duração do ciclo completo
            )
        }
    }
}

/**
 * Combina pulo (offset Y) + escala com keyframes para dar um efeito elástico.
 */
@Composable
fun BouncyImage(
    painter: Painter,
    contentDescription: String?,
    amplitudeDp: Int = 18,
    cycleDurationMs: Int = 900
) {
    val infinite = rememberInfiniteTransition(label = "bouncy")

    // Anima a posição vertical: chão (0) -> topo (-amplitude) -> chão (0)
    val yOffset by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 0f, // usamos keyframes para os valores intermediários
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = cycleDurationMs
                // Sobe rápido até o topo
                0f at 0 with LinearOutSlowInEasing
                -amplitudeDp.toFloat() at (cycleDurationMs * 0.38f).toInt() with FastOutSlowInEasing
                // Cai e volta ao chão (leve “quique” implícito no squash/stretch)
                0f at cycleDurationMs with FastOutLinearInEasing
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "yOffset"
    )

    // Escala vertical (esticado no topo, levemente achatado no chão)
    val scaleY by infinite.animateFloat(
        initialValue = 1f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = cycleDurationMs
                1.0f at 0
                1.12f at (cycleDurationMs * 0.38f).toInt() with FastOutSlowInEasing // topo
                0.96f at cycleDurationMs with FastOutLinearInEasing               // chão
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "scaleY"
    )

    // Escala horizontal (inverso da vertical no impacto para “squash”)
    val scaleX by infinite.animateFloat(
        initialValue = 1f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = cycleDurationMs
                1.0f at 0
                0.98f at (cycleDurationMs * 0.38f).toInt() with FastOutSlowInEasing // topo (levemente estreito)
                1.06f at cycleDurationMs with FastOutLinearInEasing                 // chão (mais largo)
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "scaleX"
    )

    Image(
        painter = painter,
        contentDescription = contentDescription,
        modifier = Modifier
            .offset(y = yOffset.dp)
            .graphicsLayer(
                scaleX = scaleX,
                scaleY = scaleY
            )
    )
}
