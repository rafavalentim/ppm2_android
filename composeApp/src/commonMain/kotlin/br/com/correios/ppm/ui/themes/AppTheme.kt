package br.com.correios.ppm.ui.themes

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val lightColors  = lightColorScheme(

        primary = primary_30,
        onPrimary = primary_100,
        primaryContainer = neutral_70,
        onPrimaryContainer = primary_70,
        secondary = secondary_50,
        onSecondary = primary_20,
        onTertiary = primary_20,
        background = neutral_90,
        onBackground = neutral_20,
        surface = neutral_100,
        onSurface = neutral_20,
        outline = neutral_40,
        outlineVariant = neutral_variant_80,
        surfaceVariant = neutral_variant_90,
        onSurfaceVariant = neutral_variant_20,
        error = error_40,
        onError = error_100

    )
    val darkColors  = darkColorScheme(
        primary = primary_70,
        onPrimary = primary_20,
        primaryContainer = neutral_50,
        onPrimaryContainer = primary_20,
        secondary = secondary_50,
        onSecondary = primary_20,
        onTertiary = primary_20,
        background = neutral_10,
        onBackground = neutral_90,
        surface = neutral_20,
        onSurface = neutral_90,
        outline = neutral_40,
        outlineVariant = neutral_variant_50,
        surfaceVariant = neutral_variant_10,
        onSurfaceVariant = neutral_variant_20,
        error = error_40,
        onError = error_100

    )
    val colors = if (darkTheme) darkColors else lightColors

    MaterialTheme(
        colorScheme = colors,
        typography = typography,
        shapes = shapes,
        content = content
    )
}