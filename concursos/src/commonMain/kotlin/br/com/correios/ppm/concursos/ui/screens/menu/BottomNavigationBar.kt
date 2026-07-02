package br.com.correios.ppm.concursos.ui.screens.menu

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow

@Composable
fun BottomNavigationBar(items: List<NavigationItem>) {
    val navigator = LocalNavigator.currentOrThrow
    val currentScreen = navigator.lastItem

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surfaceBright,
        tonalElevation = 4.dp
    ) {
        items.forEach { item ->
            val isSelected = currentScreen::class == item.screen::class

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    if (!isSelected) {
                        navigator.replace(item.screen)
                    }
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        tint = MaterialTheme.colorScheme.primary,
                        contentDescription = item.label
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                alwaysShowLabel = true
            )
        }
    }
}

data class NavigationItem(
    val label: String,
    val icon: ImageVector,
    val screen: Screen
)
