package br.com.correios.ppm.concursos.ui.screens

import androidx.compose.runtime.Composable

@Composable
expect fun BarcodeScannerScreen(onBarcodeScanned: (String) -> Unit)
