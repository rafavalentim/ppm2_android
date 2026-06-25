package br.com.correios.ppm.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp

@Composable
fun CorreiosHorizontalDivider(topPadding:Int) {
    HorizontalDivider(
        modifier = Modifier
            .padding(top = topPadding.dp)
            .shadow(8.dp),
        thickness = 2.dp,
        color = MaterialTheme.colorScheme.secondary
    )
}