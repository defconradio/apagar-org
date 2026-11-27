package org.apagar.shared.presentation.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val ReceiptColorPalette = lightColorScheme(
    primary = InkBlack,
    onPrimary = HeroCream,
    secondary = HeroDeepBlue,
    onSecondary = HeroCream,
    background = PaperBackground,
    onBackground = InkBlack,
    surface = ReceiptSurface,
    onSurface = InkBlack,
    surfaceVariant = PaperVariant,
    onSurfaceVariant = InkMuted,
    outline = PaperBorder,
    error = StampRed,
    onError = Color.White
)

@Composable
fun AppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = ReceiptColorPalette,
        content = {
            SystemBarColoring()
            content()
        }
    )
}

@Composable
expect fun SystemBarColoring()
