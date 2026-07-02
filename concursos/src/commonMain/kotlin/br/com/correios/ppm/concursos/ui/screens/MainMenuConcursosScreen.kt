package br.com.correios.ppm.concursos.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.correios.ppm.concursos.presentation.MainConcursosViewModel
import br.com.correios.ppm.concursos.ui.screens.menu.BottomNavigationBar
import br.com.correios.ppm.concursos.ui.screens.menu.NavigationItem
import br.com.correios.ppm.ui.components.CorreiosHorizontalDivider
import br.com.correios.ppm.ui.components.HeaderMainMenu
import br.com.correios.ppm.ui.components.IconTitleList
import br.com.correios.ppm.ui.components.LoadingScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.koin.core.Koin

class MainMenuConcursosScreen(val koin: Koin) : Screen {

    @Composable
    override fun Content() {
        MenuConcursosScreen(koin)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuConcursosScreen(
    koin: Koin,
    viewModel: MainConcursosViewModel = koin.get()
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val isLoading by viewModel.isLoading.collectAsState()
    val nome by viewModel.nomeUsuario.collectAsState()

    val truncada = viewModel.truncateStringWithEllipsis(16, nome)

    // Itens do bottom nav — adicione telas reais conforme forem criadas
    val navItems = listOf<NavigationItem>()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            HeaderMainMenu(
                nome = truncada,
                matricula = null,
                logoPainter = rememberVectorPainter(Icons.Default.Edit)
            )
        },
        bottomBar = {
            if (navItems.isNotEmpty()) {
                BottomNavigationBar(items = navItems)
            }
        }
    ) { paddingValues ->
        MenuContent(
            viewModel = viewModel,
            koin = koin,
            modifier = Modifier.padding(paddingValues)
        )
    }

    if (isLoading) {
        LoadingScreen(isLoading)
    }
}

@Composable
fun MenuContent(
    viewModel: MainConcursosViewModel,
    koin: Koin,
    modifier: Modifier = Modifier
) {
    val navigator = LocalNavigator.currentOrThrow
    val isConnected by viewModel.isConnected.collectAsState()

    val listaMenus = listOf(
        Pair(rememberVectorPainter(Icons.Default.Edit), "Cadastrar Dados do Concurso"),
        Pair(rememberVectorPainter(Icons.Default.Sync), "Atualizar Status de Objetos")
    )

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CorreiosHorizontalDivider(topPadding = 8)
        Box(contentAlignment = Alignment.TopCenter) {
            MainMenuText(
                name = "Escolha uma opção",
                isConnected = isConnected,
                topPadding = 16
            )
            IconTitleList(
                items = listaMenus,
                onItemClick = { item ->
                    when (item) {
                        "Cadastrar Dados do Concurso" -> {
                            // navigator.push(CadastroConcursoScreen(koin))
                        }
                        "Atualizar Status de Objetos" -> {
                            // navigator.push(AtualizaStatusScreen(koin))
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun MainMenuText(name: String, isConnected: Boolean, topPadding: Int) {
    val icon = if (isConnected) Icons.Default.Wifi else Icons.Default.WifiOff
    val tint = if (isConnected) MaterialTheme.colorScheme.secondary
               else MaterialTheme.colorScheme.error

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = topPadding.dp, start = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = if (isConnected) "Conectado" else "Desconectado",
            tint = tint,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = name,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(start = 8.dp, top = 2.dp)
        )
    }
}
