package br.com.correios.ppm.ui.screens.unidade

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import br.com.correios.ppm.ui.components.CorreiosHorizontalDivider
import br.com.correios.ppm.ui.components.LoadingScreen
import br.com.correios.ppm.ui.components.LogoScreen
import br.com.correios.ppm.ui.components.UiEvent
import br.com.correios.ppm.ui.themes.AppTheme
import br.com.correios.ppm.unidade.application.Unidade
import br.com.correios.ppm.unidade.presentation.UnidadeUiState
import br.com.correios.ppm.unidade.presentation.UnidadeViewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.koin.core.Koin

class ConsultaUnidadeScreen(val koin: Koin) : Screen {

    @Composable
    override fun Content() {
        AppTheme {
            ConsultaUnidadeContent(koin)
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ConsultaUnidadeContent(
        koin: Koin,
        viewModel: UnidadeViewModel = koin.get()
    ) {
        val navigator = LocalNavigator.currentOrThrow
        val isLoading by viewModel.isLoading.collectAsState()
        val uiState by viewModel.uiState.collectAsState()
        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
        var expanded by remember { mutableStateOf(false) }
        val snackbarHostState = remember { SnackbarHostState() }

        LaunchedEffect(Unit) {
            viewModel.events.collect { event ->
                when (event) {
                    is UiEvent.ShowMessage -> snackbarHostState.showSnackbar(event.message)
                    else -> {}
                }
            }
        }

        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                CenterAlignedTopAppBar(
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        titleContentColor = MaterialTheme.colorScheme.primary,
                    ),
                    title = { LogoScreen(5) },
                    actions = {
                        IconButton(onClick = { expanded = true }) {
                            Icon(
                                imageVector = Icons.Filled.Menu,
                                contentDescription = "Menu"
                            )
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Sair") },
                                onClick = {
                                    expanded = false
                                    navigator.pop()
                                }
                            )
                        }
                    },
                    scrollBehavior = scrollBehavior,
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                CorreiosHorizontalDivider(0)

                UnidadeSearchBar(onSearch = { viewModel.buscarUnidade(it) })

                when (val state = uiState) {
                    is UnidadeUiState.Success -> UnidadeResultCard(state.unidade)
                    is UnidadeUiState.Error -> UnidadeErrorCard(state.message)
                    else -> {}
                }
            }

            if (isLoading) {
                LoadingScreen(isLoading)
            }
        }
    }

    @Composable
    fun UnidadeResultCard(unidade: Unidade) {
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Dados da Unidade",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(8.dp))
                UnidadeInfoRow("Código", unidade.codigoUnidade)
                UnidadeInfoRow("Nome", unidade.nome)
                UnidadeInfoRow("Sigla", unidade.sigla)
                UnidadeInfoRow("DR", unidade.dr)
                UnidadeInfoRow("Tipo", unidade.tipo)
            }
        }
    }

    @Composable
    fun UnidadeInfoRow(label: String, value: String?) {
        if (value.isNullOrBlank()) return
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        ) {
            Text(
                text = "$label:",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.weight(0.4f)
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.weight(0.6f)
            )
        }
    }

    @Composable
    fun UnidadeErrorCard(message: String) {
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Unidade não encontrada",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }

    @Composable
    fun UnidadeSearchBar(onSearch: (String) -> Unit) {
        var codigoMcu by remember { mutableStateOf("") }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Unidade de Negócio (MCU)",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(end = 8.dp)
            )

            OutlinedTextField(
                value = codigoMcu,
                onValueChange = { codigoMcu = it },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp),
                singleLine = true,
                placeholder = {
                    Text(
                        text = "Código",
                        style = MaterialTheme.typography.bodySmall
                    )
                },
                trailingIcon = {
                    IconButton(
                        onClick = { onSearch(codigoMcu) },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Pesquisar",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        }
    }
}
