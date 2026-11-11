package de.hhn.mplab.statemanagement.ui.theme

import androidx.compose.ui.graphics.Color

// Design-System "Midnight Compose" (siehe design/compose-theme.md).
// Gemeinsame Neutraltöne und semantische Farben beider Apps.

// ---------- Midnight (Dark-Mode-Flächen, = Folien-Tokens) ----------
val Midnight = Color(0xFF0E1122)           // Folien --bg
val MidnightLowest = Color(0xFF090B17)     // Folien --code-bg
val MidnightLow = Color(0xFF13172A)
val MidnightContainer = Color(0xFF171B31)  // Folien --soft
val MidnightHigh = Color(0xFF1F2440)       // Folien --surface-2
val MidnightHighest = Color(0xFF2A2F4C)
val MidnightBright = Color(0xFF353A58)
val MidnightOnSurface = Color(0xFFE6E4F2)
val MidnightOnVariant = Color(0xFFC6C3D8)
val MidnightOutline = Color(0xFF8F8DA3)
val MidnightOutlineVar = Color(0xFF3B4470) // Folien --line-strong

// ---------- Fehler ----------
val ErrorLight = Color(0xFFC21F3A)
val OnErrorLight = Color(0xFFFFFFFF)
val ErrorContainerLight = Color(0xFFFFDADC)
val OnErrorContainerLight = Color(0xFF40000D)
val ErrorDark = Color(0xFFFFB3B8)
val OnErrorDark = Color(0xFF680019)
val ErrorContainerDark = Color(0xFF93102B)
val OnErrorContainerDark = Color(0xFFFFDADC)

// ---------- Erfolg / "Fix" (nicht Teil von M3, siehe ExtendedColors) ----------
val SuccessLight = Color(0xFF0B7A43)
val SuccessDark = Color(0xFF3DDC84) // Android Green, = Folien --good
