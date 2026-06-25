package br.com.correios.ppm.ui.components

import android.content.res.Configuration
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.tooling.preview.Preview
import br.com.correios.ppm.ui.themes.AppTheme

@Preview(name = "Light Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewElevatedCardMenu() {
    AppTheme {
        CardMenu(
            title = "Armazém",
            icon = rememberVectorPainter(Icons.Default.Home)
        )
    }
}