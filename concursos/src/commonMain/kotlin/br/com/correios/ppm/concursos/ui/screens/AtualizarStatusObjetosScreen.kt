package br.com.correios.ppm.concursos.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
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
import androidx.compose.ui.window.Dialog
import br.com.correios.ppm.concursos.presentation.AtualizaStatusObjetosViewModel
import br.com.correios.ppm.ui.components.LoadingScreen
import br.com.correios.ppm.ui.components.UiEvent
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.koin.core.Koin

class AtualizaStatusObjetosScreen(val koin: Koin) : Screen {

    @Composable
    override fun Content() {
        val viewModel: AtualizaStatusObjetosViewModel = koin.get()
        TelaAtualizaStatusObjetosScreen(viewModel)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaAtualizaStatusObjetosScreen(viewModel: AtualizaStatusObjetosViewModel) {
    val navigator = LocalNavigator.currentOrThrow
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val isLoading by viewModel.isLoading.collectAsState()
    val isConnected by viewModel.isConnected.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.carregarEdicaoCadastrada()
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
            TopAppBar(
                title = { Text("Atualizar Status de Objetos") },
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
                name = "Atualização",
                isConnected = isConnected,
                topPadding = 10
            )
            AtualizaStatusObjetosScreenContent(viewModel)
        }
    }

    if (isLoading) {
        LoadingScreen(isLoading)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AtualizaStatusObjetosScreenContent(viewModel: AtualizaStatusObjetosViewModel) {
    val tipoSelecionado by viewModel.tipoSelecionado.collectAsState()
    val numeroEdicao by viewModel.numeroEdicao.collectAsState()
    val nomeEdicao by viewModel.nomeEdicao.collectAsState()
    val codigoObjeto by viewModel.codigoObjeto.collectAsState()
    val dataSelecionada by viewModel.dataSelecionada.collectAsState()
    val horaSelecionada by viewModel.horaSelecionada.collectAsState()

    val erroCodigoObjeto by viewModel.erroCodigoObjeto.collectAsState()
    val erroNumeroEdicao by viewModel.erroNumeroEdicao.collectAsState()
    val erroNomeEdicao by viewModel.erroNomeEdicao.collectAsState()

    val showScanner by viewModel.showBarcodeScanner.collectAsState()

    val datePickerState = rememberDatePickerState()
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var expandedTipoOperacao by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {

        Row(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = numeroEdicao,
                onValueChange = {},
                readOnly = true,
                isError = erroNumeroEdicao,
                label = { Text("Número da Edição") },
                modifier = Modifier.weight(1f).padding(end = 8.dp)
            )
            OutlinedTextField(
                value = nomeEdicao,
                onValueChange = {},
                readOnly = true,
                isError = erroNomeEdicao,
                label = { Text("Nome da Edição") },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        ExposedDropdownMenuBox(
            expanded = expandedTipoOperacao,
            onExpandedChange = { expandedTipoOperacao = !expandedTipoOperacao }
        ) {
            OutlinedTextField(
                value = tipoSelecionado,
                onValueChange = {},
                readOnly = true,
                label = { Text("Tipo de Operação") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandedTipoOperacao) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expandedTipoOperacao,
                onDismissRequest = { expandedTipoOperacao = false }
            ) {
                viewModel.tipoOperacaoOptions.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            viewModel.onTipoSelecionadoChange(option)
                            expandedTipoOperacao = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = dataSelecionada,
                onValueChange = {},
                readOnly = true,
                label = { Text("Data") },
                modifier = Modifier
                    .weight(1f)
                    .clickable { showDatePicker = true }
            )

            IconButton(
                onClick = { showDatePicker = true },
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Icon(Icons.Default.DateRange, contentDescription = "Selecionar data")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = horaSelecionada,
                onValueChange = {},
                readOnly = true,
                label = { Text("Hora") },
                modifier = Modifier
                    .weight(1f)
                    .clickable { showTimePicker = true }
            )

            IconButton(
                onClick = { showTimePicker = true },
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Icon(Icons.Default.Schedule, contentDescription = "Selecionar hora")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            CampoCodigoObjeto(
                codigoObjeto = codigoObjeto,
                onCodigoObjetoChange = { viewModel.onCodigoObjetoChange(it) },
                erro = erroCodigoObjeto,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            )

            IconButton(
                onClick = { viewModel.onShowBarcodeChanged(true) },
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Icon(Icons.Default.CameraAlt, contentDescription = "Ler código de barras")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { viewModel.atualizarStatusObjetos() },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Atualizar")
        }
    }

    if (showScanner) {
        Dialog(onDismissRequest = { viewModel.onShowBarcodeChanged(false) }) {
            Box(modifier = Modifier.size(300.dp)) {
                BarcodeScannerScreen(
                    onBarcodeScanned = {
                        viewModel.onCodigoObjetoChange(it)
                        viewModel.onShowBarcodeChanged(false)
                    }
                )
            }
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        viewModel.onDataSelecionadaChange(it)
                    }
                    showDatePicker = false
                }) { Text("Confirmar") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancelar") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (showTimePicker) {
        val timePickerState = rememberTimePickerState(is24Hour = true)
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.onHoraSelecionadaChange(timePickerState.hour, timePickerState.minute)
                    showTimePicker = false
                }) { Text("Confirmar") }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) { Text("Cancelar") }
            },
            text = { TimePicker(state = timePickerState) }
        )
    }
}

@Composable
fun CampoCodigoObjeto(
    codigoObjeto: String,
    onCodigoObjetoChange: (String) -> Unit,
    erro: Boolean,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value = codigoObjeto,
            onValueChange = onCodigoObjetoChange,
            label = { Text("Código do Objeto") },
            isError = erro,
            modifier = Modifier.fillMaxWidth()
        )
        if (erro) {
            Text(
                text = "Código obrigatório",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}
