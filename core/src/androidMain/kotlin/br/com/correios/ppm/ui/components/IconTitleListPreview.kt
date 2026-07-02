package br.com.correios.ppm.ui.components

import android.content.res.Configuration
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.graphics.vector.rememberVectorPainter

@Preview(name = "Light Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun IconTitleListPreview() {
    MaterialTheme {
        IconTitleList(
            items = listOf(
                Pair(rememberVectorPainter(Icons.Default.CameraAlt), "Câmera"),
                Pair(rememberVectorPainter(Icons.Default.Photo), "Galeria"),
                Pair(rememberVectorPainter(Icons.Default.Settings), "Configurações")
            ),
            onItemClick = {}
        )
    }
}
