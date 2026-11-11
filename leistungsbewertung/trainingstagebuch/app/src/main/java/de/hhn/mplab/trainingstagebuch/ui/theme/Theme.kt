package de.hhn.mplab.trainingstagebuch.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * Theme des Trainingstagebuchs: "Midnight Compose", Petrol + Orange.
 * Gleiche Flächen, Formen und Typo wie die Beispiel-App, nur andere Markenfarben.
 */
private val LightColors = lightColorScheme(
    primary = Color(0xFF00677A),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFB0ECFF),
    onPrimaryContainer = Color(0xFF001F26),
    inversePrimary = Color(0xFF85D2E8),
    secondary = Color(0xFF4B6269),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFCEE7EF),
    onSecondaryContainer = Color(0xFF061F25),
    tertiary = Color(0xFFA24A00), // Orange: Rekorde, aktive Serie, Timer
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFFFDBC8),
    onTertiaryContainer = Color(0xFF341100),
    error = ErrorLight,
    onError = OnErrorLight,
    errorContainer = ErrorContainerLight,
    onErrorContainer = OnErrorContainerLight,
    background = Color(0xFFF6FAFC),
    onBackground = Color(0xFF171C1F),
    surface = Color(0xFFF6FAFC),
    onSurface = Color(0xFF171C1F),
    surfaceVariant = Color(0xFFDBE4E8),
    onSurfaceVariant = Color(0xFF3F484C),
    surfaceTint = Color(0xFF00677A),
    inverseSurface = Color(0xFF2C3134),
    inverseOnSurface = Color(0xFFEDF1F4),
    outline = Color(0xFF6F797C),
    outlineVariant = Color(0xFFBFC8CC),
    scrim = Color(0xFF000000),
    surfaceBright = Color(0xFFF6FAFC),
    surfaceDim = Color(0xFFD6DBDE),
    surfaceContainerLowest = Color(0xFFFFFFFF),
    surfaceContainerLow = Color(0xFFF0F4F7),
    surfaceContainer = Color(0xFFEAEEF1),
    surfaceContainerHigh = Color(0xFFE4E9EB),
    surfaceContainerHighest = Color(0xFFDFE3E6),
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF85D2E8),
    onPrimary = Color(0xFF003641),
    primaryContainer = Color(0xFF004E5C),
    onPrimaryContainer = Color(0xFFB0ECFF),
    inversePrimary = Color(0xFF00677A),
    secondary = Color(0xFFB2CBD3),
    onSecondary = Color(0xFF1D343A),
    secondaryContainer = Color(0xFF344A51),
    onSecondaryContainer = Color(0xFFCEE7EF),
    tertiary = Color(0xFFFFB68C),
    onTertiary = Color(0xFF562000),
    tertiaryContainer = Color(0xFF7B3600),
    onTertiaryContainer = Color(0xFFFFDBC8),
    error = ErrorDark,
    onError = OnErrorDark,
    errorContainer = ErrorContainerDark,
    onErrorContainer = OnErrorContainerDark,
    background = Midnight,
    onBackground = MidnightOnSurface,
    surface = Midnight,
    onSurface = MidnightOnSurface,
    surfaceVariant = MidnightHighest,
    onSurfaceVariant = MidnightOnVariant,
    surfaceTint = Color(0xFF85D2E8),
    inverseSurface = MidnightOnSurface,
    inverseOnSurface = Color(0xFF2B2E40),
    outline = MidnightOutline,
    outlineVariant = MidnightOutlineVar,
    scrim = Color(0xFF000000),
    surfaceBright = MidnightBright,
    surfaceDim = Midnight,
    surfaceContainerLowest = MidnightLowest,
    surfaceContainerLow = MidnightLow,
    surfaceContainer = MidnightContainer,
    surfaceContainerHigh = MidnightHigh,
    surfaceContainerHighest = MidnightHighest,
)

/** Rolle, die M3 nicht kennt: "erledigt" / "Fix funktioniert". */
@Immutable
data class ExtendedColors(val success: Color, val onSuccess: Color)

val LocalExtendedColors = staticCompositionLocalOf {
    ExtendedColors(success = SuccessLight, onSuccess = Color.White)
}

/**
 * App-Theme. Dynamische Farben (Material You) sind bewusst nicht aktiviert,
 * damit die App auf allen Geräten gleich aussieht.
 */
@Composable
fun TrainingstagebuchTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colors = if (darkTheme) DarkColors else LightColors
    val extended = if (darkTheme) {
        ExtendedColors(success = SuccessDark, onSuccess = Midnight)
    } else {
        ExtendedColors(success = SuccessLight, onSuccess = Color.White)
    }
    CompositionLocalProvider(LocalExtendedColors provides extended) {
        MaterialTheme(
            colorScheme = colors,
            typography = AppTypography,
            shapes = AppShapes,
            content = content,
        )
    }
}

/** Nutzung: `MaterialTheme.extended.success` */
val MaterialTheme.extended: ExtendedColors
    @Composable get() = LocalExtendedColors.current
