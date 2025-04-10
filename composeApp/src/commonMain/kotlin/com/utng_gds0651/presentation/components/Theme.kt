package com.utng_gds0651.presentation.components
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Colores inspirados en Rick and Morty
private val RickGreen = Color(0xFF97CE4C)
private val MortyYellow = Color(0xFFF0E14A)
private val PortalBlue = Color(0xFF00B5CC)
private val PortalGreenLight = Color(0xFFCAFF70)
private val PortalGreen = Color(0xFF44281D)
private val BackgroundLight = Color(0xFFF2F7FF)
private val BackgroundDark = Color(0xFF121212)
private val CardLight = Color(0xFFFFFFFF)
private val CardDark = Color(0xFF1E1E1E)

private val RickAndMortyLightColors = lightColorScheme(
    primary = RickGreen,
    onPrimary = Color.White,
    secondary = PortalBlue,
    onSecondary = Color.White,
    tertiary = MortyYellow,
    onTertiary = Color.Black,
    background = BackgroundLight,
    onBackground = Color.Black,
    surface = CardLight,
    onSurface = Color.Black
)

private val RickAndMortyDarkColors = darkColorScheme(
    primary = RickGreen,
    onPrimary = Color.Black,
    secondary = PortalBlue,
    onSecondary = Color.Black,
    tertiary = MortyYellow,
    onTertiary = Color.Black,
    background = BackgroundDark,
    onBackground = Color.White,
    surface = CardDark,
    onSurface = Color.White
)

private val AppTypography = Typography(
    headlineLarge = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        letterSpacing = 0.sp
    ),
    headlineMedium = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        letterSpacing = 0.15.sp
    ),
    headlineSmall = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        letterSpacing = 0.15.sp
    ),
    bodyLarge = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        letterSpacing = 0.15.sp
    ),
    bodyMedium = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        letterSpacing = 0.25.sp
    ),
    bodySmall = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        letterSpacing = 0.4.sp
    ),
    labelLarge = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        letterSpacing = 1.sp
    )
)

@Composable
fun RickAndMortyTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (useDarkTheme) RickAndMortyDarkColors else RickAndMortyLightColors

    MaterialTheme(
        colorScheme = colors,
        typography = AppTypography,
        content = content
    )
}