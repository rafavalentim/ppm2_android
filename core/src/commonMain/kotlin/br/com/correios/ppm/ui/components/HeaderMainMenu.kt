package br.com.correios.ppm.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContactMail
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HeaderDefaultMenu(nome: String?, logoPainter: Painter) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 35.dp),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            LogoMenu(logoPainter)
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = "Usuário: $nome",
                modifier = Modifier.padding(top = 6.dp),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 14.sp
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            CorreiosHorizontalDivider(2)
        }
    }
}

@Composable
fun HeaderMainMenu(
    nome: String?,
    matricula: String?,
    logoPainter: Painter
) {
    Column(
        modifier = Modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            LogoMenu(logoPainter)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconCard(icon = Icons.Default.ContactMail)
            UserColumn(nome, matricula)
        }
    }
}

@Composable
fun LogoMenu(painter: Painter) {
    Image(
        painter = painter,
        contentDescription = "Logo",
        modifier = Modifier
            .height(30.dp)
            .width(140.dp)
    )
}

@Composable
fun IconCard(
    icon: ImageVector,
    iconColor: Color = MaterialTheme.colorScheme.primary
) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = Modifier
            .border(width = 2.dp, color = Color.Transparent)
            .size(width = 60.dp, height = 60.dp)
            .padding(5.dp)
            .clickable { },
        shape = RoundedCornerShape(8.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = "Icon",
                tint = iconColor,
                modifier = Modifier
                    .size(48.dp)
                    .padding()
            )
        }
    }
}

@Composable
fun UserColumn(nome: String?, matricula: String?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "$nome",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = "Matrícula: $matricula",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}
