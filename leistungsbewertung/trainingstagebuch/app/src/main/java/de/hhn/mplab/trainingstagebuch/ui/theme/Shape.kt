package de.hhn.mplab.trainingstagebuch.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/** Gleiche Radien wie die Folien (Karten 14, Code 16, Pills voll rund). */
val AppShapes = Shapes(
    extraSmall = RoundedCornerShape(6.dp),  // Chips, Badges
    small = RoundedCornerShape(8.dp),       // TextField, kleine Buttons
    medium = RoundedCornerShape(14.dp),     // Cards
    large = RoundedCornerShape(16.dp),      // Bottom Sheets, Dialog-Inhalte
    extraLarge = RoundedCornerShape(28.dp), // Dialoge, FAB extended
)
