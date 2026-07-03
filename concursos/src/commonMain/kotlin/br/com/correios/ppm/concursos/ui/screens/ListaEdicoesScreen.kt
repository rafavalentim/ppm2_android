package br.com.correios.ppm.concursos.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.correios.ppm.concursos.application.Edicao
import br.com.correios.ppm.concursos.presentation.CadastroEdicaoUiState
import br.com.correios.ppm.concursos.presentation.CadastroEdicaoViewModel

@Composable
fun ListaEdicoesScreen(
    viewModel: CadastroEdicaoViewModel,
    onEdicaoSelecionada: (Edicao) -> Unit
) {
    val isLoading by viewModel.isLoading.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val edicoes = (uiState as? CadastroEdicaoUiState.Success)?.edicoes.orEmpty()

    LaunchedEffect(Unit) {
        viewModel.carregarEdicoes()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Lista de Edições",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        when {
            isLoading -> Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }

            uiState is CadastroEdicaoUiState.Error -> Text(
                text = (uiState as CadastroEdicaoUiState.Error).mensagem ?: "Erro ao buscar edições",
                color = MaterialTheme.colorScheme.error
            )

            else -> LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(edicoes) { edicao ->
                    ElevatedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable { onEdicaoSelecionada(edicao) },
                        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
                    ) {
                        EdicaoItem(edicao)
                    }
                }
            }
        }
    }
}

@Composable
fun EdicaoItem(edicao: Edicao) {
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text(text = "Código: ${edicao.codigo}", style = MaterialTheme.typography.bodyMedium)
        Text(text = "Descrição: ${edicao.descricao}", style = MaterialTheme.typography.bodyMedium)
        Text(text = "Edição Atual: ${edicao.edicaoAtual}", style = MaterialTheme.typography.bodyMedium)
    }
}
