package br.com.correios.ppm.concursos.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import br.com.correios.ppm.concursos.application.Edicao
import br.com.correios.ppm.concursos.presentation.CadastroEdicaoViewModel
import br.com.correios.ppm.ui.components.LoadingScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.koin.core.Koin

class CadastroEdicaoScreen(val koin: Koin) : Screen {

    @Composable
    override fun Content() {
        val viewModel: CadastroEdicaoViewModel = koin.get()
        TelaCadastroEdicaoScreen(viewModel)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaCadastroEdicaoScreen(viewModel: CadastroEdicaoViewModel) {
    val navigator = LocalNavigator.currentOrThrow
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val isLoading by viewModel.isLoading.collectAsState()
    val isConnected by viewModel.isConnected.collectAsState()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = { Text("Cadastro de Edição") },
                navigationIcon = {
                    IconButton(onClick = { navigator.pop() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            MainMenuText(
                name = "Cadastro",
                isConnected = isConnected,
                topPadding = 10
            )
            CadastroScreen(viewModel)
        }
    }

    if (isLoading) {
        LoadingScreen(isLoading)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CadastroScreen(viewModel: CadastroEdicaoViewModel) {
    val numeroEdicao by viewModel.numeroEdicao.collectAsState()
    val nomeEdicao by viewModel.nomeEdicao.collectAsState()

    val erroNumeroEdicao by viewModel.erroNumeroEdicao.collectAsState()
    val erroNomeEdicao by viewModel.erroNomeEdicao.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var edicaoSelecionada by remember { mutableStateOf<Edicao?>(null) }

    LaunchedEffect(edicaoSelecionada) {
        edicaoSelecionada?.let {
            viewModel.onNumeroEdicaoChanged(it.codigo.orEmpty())
            viewModel.onNomeEdicaoChanged(it.descricao.orEmpty())
        }
    }

    Column(
        modifier = Modifier.padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            CampoNumeroEdicao(
                numeroEdicao = numeroEdicao,
                onNumeroEdicaoChange = { viewModel.onNumeroEdicaoChanged(it) },
                erro = erroNumeroEdicao,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            )

            IconButton(
                onClick = { showDialog = true },
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Ícone de pesquisa",
                    modifier = Modifier.size(36.dp)
                )
            }
        }

        if (showDialog) {
            Dialog(onDismissRequest = { showDialog = false }) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    tonalElevation = 8.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 500.dp)
                ) {
                    ListaEdicoesScreen(
                        viewModel,
                        onEdicaoSelecionada = { edicao ->
                            edicaoSelecionada = edicao
                            showDialog = false
                        }
                    )
                }
            }
        }

        CampoNomeEdicao(
            nomeEdicao = nomeEdicao,
            onNomeEdicaoChange = { viewModel.onNomeEdicaoChanged(it) },
            erro = erroNomeEdicao
        )

        Button(
            onClick = {
                viewModel.onValidarNumeroEdicao()
                viewModel.onValidarNomeEdicao()
                if (!erroNumeroEdicao && !erroNomeEdicao) {
                    viewModel.cadastrar()
                }
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Cadastrar")
            }
        }
    }
}

@Composable
fun CampoNumeroEdicao(
    numeroEdicao: String,
    onNumeroEdicaoChange: (String) -> Unit,
    erro: Boolean,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value = numeroEdicao,
            onValueChange = onNumeroEdicaoChange,
            label = { Text("Número da Edição") },
            isError = erro,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        if (erro) {
            Text(
                text = "Número obrigatório",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}

@Composable
fun CampoNomeEdicao(
    nomeEdicao: String,
    onNomeEdicaoChange: (String) -> Unit,
    erro: Boolean
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = nomeEdicao,
            onValueChange = onNomeEdicaoChange,
            label = { Text("Nome da Edição") },
            isError = erro,
            modifier = Modifier.fillMaxWidth()
        )
        if (erro) {
            Text(
                text = "Nome obrigatório",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}
