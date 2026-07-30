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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.graphicsLayer
import br.com.correios.ppm.UpdateDownloadStatus
import br.com.correios.ppm.splash.presentation.SplashScreenViewModel
import br.com.correios.ppm.splash.presentation.UsuarioUiState
import br.com.correios.ppm.ui.components.UiEvent
import br.com.correios.ppm.ui.screens.login.LoginDescriptionScreen
import br.com.correios.ppm.ui.screens.main.MainScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.coroutines.delay
import org.koin.core.Koin
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first


class SplashScreen(val koin : Koin) : Screen{

    @Composable
    override fun Content() {
        SplashMainScreen(koin)
    }
}


@Composable
fun SplashMainScreen(
    koin: Koin,
    splashScreenViewModel: SplashScreenViewModel = koin.get()
) {
    val navigator = LocalNavigator.currentOrThrow
    val usuarioState by splashScreenViewModel.uiState.collectAsState()
    val updateAvailable by splashScreenViewModel.updateAvailable.collectAsState()
    val downloadStatus by splashScreenViewModel.downloadStatus.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    // Coletar eventos e mostrar snackbar
    LaunchedEffect(Unit) {
        splashScreenViewModel.events.collect { event ->
            when (event) {
                is UiEvent.ShowMessage -> {
                    snackbarHostState.showSnackbar(
                        message = event.message,
                        actionLabel = event.actionLabel
                    )
                }

                else -> {}
            }
        }
    }


    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->

        // UI do splash (seu layout atual)
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(24.dp))
                BouncyImage(
                    painter = painterResource(Res.drawable.icone),
                    contentDescription = "correios",
                    amplitudeDp = 18,
                    cycleDurationMs = 900
                )
            }
        }
    }

    updateAvailable?.let {
        AlertDialog(
            onDismissRequest = {},
            title = { Text("Atualização disponível") },
            text = { Text("Existe uma nova versão do aplicativo disponível. Ela será baixada e instalada agora.") },
            confirmButton = {
                TextButton(onClick = { splashScreenViewModel.confirmarAtualizacao() }) {
                    Text("OK")
                }
            }
        )
    }

    downloadStatus?.let { status ->
        when (status) {
            is UpdateDownloadStatus.Progress -> {
                AlertDialog(
                    onDismissRequest = {},
                    title = { Text("Baixando atualização") },
                    text = {
                        Column {
                            Text("Baixando nova versão... ${status.percent}%")
                            Spacer(Modifier.height(8.dp))
                            LinearProgressIndicator(
                                progress = { status.percent / 100f },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    },
                    confirmButton = {}
                )
            }
            UpdateDownloadStatus.Installing -> {
                AlertDialog(
                    onDismissRequest = {},
                    title = { Text("Instalando") },
                    text = { Text("Download concluído. Iniciando a instalação da nova versão...") },
                    confirmButton = {}
                )
            }
            is UpdateDownloadStatus.Failed -> {
                AlertDialog(
                    onDismissRequest = { splashScreenViewModel.dismissDownloadStatus() },
                    title = { Text("Falha na atualização") },
                    text = { Text(status.message) },
                    confirmButton = {
                        TextButton(onClick = { splashScreenViewModel.dismissDownloadStatus() }) {
                            Text("OK")
                        }
                    }
                )
            }
        }
    }


    // Navegação após: (tempo mínimo de 5s) E (estado != Loading)
    LaunchedEffect(Unit) {
        // roda em paralelo
        val minDelay = async { delay(5_000) }
        val finalStateDeferred = async {
            splashScreenViewModel.uiState
                .filter { it !is UsuarioUiState.Loading }
                .first() // espera sair do Loading
        }

        val finalState = finalStateDeferred.await()
        minDelay.await() // garante os 5s mínimos

        // se o diálogo de atualização estiver aberto, espera o usuário confirmar antes de navegar
        splashScreenViewModel.updateAvailable.filter { it == null }.first()

        // se o download estiver em andamento, espera ele terminar (instalando ou nunca começou);
        // em caso de falha, só segue depois que o usuário fechar o diálogo de erro
        splashScreenViewModel.downloadStatus
            .filter { it == null || it is UpdateDownloadStatus.Installing }
            .first()

        when (finalState) {
            is UsuarioUiState.Error -> {
                navigator.replaceAll(LoginDescriptionScreen(koin))
            }
            is UsuarioUiState.Success -> {
                val destino = if (finalState.usuario?.login.isNullOrEmpty()) {
                    LoginDescriptionScreen(koin)
                } else {
                    MainScreen(koin)
                }
                navigator.replaceAll(destino)
            }
            UsuarioUiState.Loading -> Unit // não deve cair aqui
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
