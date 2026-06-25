package br.com.correios.ppm.ui.screens.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import br.com.correios.ppm.main.presentation.MainViewModel
import br.com.correios.ppm.ui.components.CardMenu
import org.jetbrains.compose.resources.painterResource
import ppm_kmp.composeapp.generated.resources.Res
import ppm_kmp.composeapp.generated.resources.empilhadeira_cor
import ppm_kmp.composeapp.generated.resources.ic_documento_cor
import br.com.correios.ppm.ui.components.CorreiosHorizontalDivider
import br.com.correios.ppm.ui.components.LoadingScreen
import br.com.correios.ppm.ui.components.LogoScreen
import br.com.correios.ppm.ui.themes.AppTheme
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
        mainViewModel: MainViewModel = koin.get()
    ) {

        val isLoading = mainViewModel.isLoading.collectAsState()

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


        // Overlay de loading
        if (isLoading.value) {
            LoadingScreen(isLoading.value)
        }

    }



    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun CenterAlignedTopAppBarExample(viewModel: MainViewModel) {
        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
        var expanded by remember { mutableStateOf(false) }

        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),

            topBar = {
                CenterAlignedTopAppBar(
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        titleContentColor = MaterialTheme.colorScheme.primary,
                    ),
                    title = {
                        LogoScreen(5)
                    },
                    navigationIcon = {
                        // IconButton(onClick = { /* do something */ }) {
//                            Icon(
//                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
//                                contentDescription = "Localized description"
//                            )
                        // }
                    },
                    actions = {
                        IconButton(onClick = { expanded = true }) {
                            Icon(
                                imageVector = Icons.Filled.Menu,
                                contentDescription = "Localized description"
                            )
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Cadastrar MCU") },
                                onClick = {
                                   // viewModel.onNavigateToMcu()
                                    expanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Sair") },
                                onClick = {
                                    //viewModel.onLogoff()
                                    expanded = false
                                }
                            )
                        }
                    },
                    scrollBehavior = scrollBehavior,
                )
            },
        ) {padding ->
            AppTheme {
                MenuContent(viewModel)
            }
        }
    }


    @Composable
    fun MenuContent(viewModel: MainViewModel) {
        //Inicializando os itens do Menu
        val listaMenuObject = listOf(
            MenuObject("Armazém", Res.drawable.empilhadeira_cor),
            MenuObject("Concursos", Res.drawable.ic_documento_cor)
        )
        CorreiosHorizontalDivider(110)
        MainMenuText()
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .padding(top = 115.dp, start = 10.dp, end = 10.dp)
                .width(300.dp)
        ) {
            items(listaMenuObject) { item ->
                CardMenu(
                    title = item.title,
                    icon = painterResource(item.icon),
                    onClick = {}
                )
            }
        }
        // Overlay de loading
        if (viewModel.isLoading.value) {
            LoadingScreen(viewModel.isLoading.value)
        }
    }

    @Composable
    fun MainMenuText(name: String = "Menu Principal") {
        Text(
            text = name,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp)
        )
    }

}