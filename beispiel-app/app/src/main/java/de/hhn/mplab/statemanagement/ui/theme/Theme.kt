package de.hhn.mplab.statemanagement.ui.theme

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
 * Theme der Beispiel-App (Einkaufsliste): "Midnight Compose", Kotlin-Violett + Teal.
 * Sieht aus wie die Folien – was im Live-Coding läuft, passt zu den Slides.
 */
private val LightColors = lightColorScheme(
    primary = Color(0xFF5B34E0),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFE6DEFF),
    onPrimaryContainer = Color(0xFF1D0A5C),
    inversePrimary = Color(0xFFC8B6FF),
    secondary = Color(0xFF5E5A78),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFE3DFFA),
    onSecondaryContainer = Color(0xFF1A1730),
    tertiary = Color(0xFF0B7A6E),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFB8F0E6),
    onTertiaryContainer = Color(0xFF00201C),
    error = ErrorLight,
    onError = OnErrorLight,
    errorContainer = ErrorContainerLight,
    onErrorContainer = OnErrorContainerLight,
    background = Color(0xFFFBF8FF),
    onBackground = Color(0xFF1B1A24),
    surface = Color(0xFFFBF8FF),
    onSurface = Color(0xFF1B1A24),
    surfaceVariant = Color(0xFFE5E0F2),
    onSurfaceVariant = Color(0xFF48455A),
    surfaceTint = Color(0xFF5B34E0),
    inverseSurface = Color(0xFF302E3A),
    inverseOnSurface = Color(0xFFF3EFFB),
    outline = Color(0xFF78758A),
    outlineVariant = Color(0xFFC9C4D8),
    scrim = Color(0xFF000000),
    surfaceBright = Color(0xFFFBF8FF),
    surfaceDim = Color(0xFFDBD8E5),
    surfaceContainerLowest = Color(0xFFFFFFFF),
    surfaceContainerLow = Color(0xFFF5F2FD),
    surfaceContainer = Color(0xFFEFECF8),
    surfaceContainerHigh = Color(0xFFE9E6F3),
    surfaceContainerHighest = Color(0xFFE3E0ED),
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFC8B6FF),
    onPrimary = Color(0xFF2C1182),
    primaryContainer = Color(0xFF4A2BC2),
    onPrimaryContainer = Color(0xFFE6DEFF),
    inversePrimary = Color(0xFF5B34E0),
    secondary = Color(0xFFC7C2E4),
    onSecondary = Color(0xFF2F2B47),
    secondaryContainer = Color(0xFF45415F),
    onSecondaryContainer = Color(0xFFE3DFFA),
    tertiary = Color(0xFF7CDCCB),
    onTertiary = Color(0xFF003731),
    tertiaryContainer = Color(0xFF005047),
    onTertiaryContainer = Color(0xFFB8F0E6),
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
    surfaceTint = Color(0xFFC8B6FF),
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
 * Dynamic Color ist bewusst aus: Die App soll auf jedem Gerät und am Beamer
 * genauso aussehen wie auf den Folien.
 */
@Composable
fun StateManagementTheme(
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
