package br.com.correios.ppm.ui.screens.main

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.correios.ppm.main.presentation.MainViewModel
import br.com.correios.ppm.ui.themes.AppTheme

@Preview(name = "Light Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewTopBar() {
    AppTheme {
        // Substitua por uma instância fake do MainViewModel quando ele existir
        // MainScreen(...).CenterAlignedTopAppBarExample(viewModel<MainViewModel>())
    }
}
