package br.com.correios.ppm.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


    @Composable
    fun LoadingScreen(isLoading: Boolean) {

        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Tela de carregamento transparente sobreposta
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f)), // Transparência do fundo
                    contentAlignment = Alignment.Center
                ) {
                    Column {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.size(64.dp)
                        )
                        Text(
                            text = "Carregando...",
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            }
        }
    }