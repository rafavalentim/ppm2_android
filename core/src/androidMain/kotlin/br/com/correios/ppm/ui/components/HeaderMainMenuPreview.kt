package br.com.correios.ppm.ui.components

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview

@Preview(name = "Light Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewHeaderMainMenu() {
    HeaderMainMenu(
        nome = "Rafael Valentim Fonseca",
        matricula = "12345678",
        logoPainter = placeholderPainter()
    )
}

@Preview(name = "Light Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewHeaderDefaultMenu() {
    HeaderDefaultMenu(
        nome = "Rafael Valentim Fonseca",
        logoPainter = placeholderPainter()
    )
}

// Retorna um Painter vazio apenas para uso em previews
@Composable
private fun placeholderPainter(): Painter =
    painterResource(android.R.drawable.ic_menu_gallery)
