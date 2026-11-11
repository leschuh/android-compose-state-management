package de.hhn.mplab.trainingstagebuch.ui

import java.text.NumberFormat
import java.util.Locale

private val kgFormat: NumberFormat = NumberFormat.getNumberInstance(Locale.GERMANY).apply {
    maximumFractionDigits = 1
}

/** Formatiert ein Gewicht für die Anzeige, z. B. `2.240 kg` oder `12,5 kg`. */
fun formatKg(value: Double): String = "${kgFormat.format(value)} kg"

/** Wandelt ein Gewicht in Text für ein Eingabefeld um (ohne Einheit, Komma als Dezimaltrenner). */
fun weightToInput(value: Double): String =
    if (value % 1.0 == 0.0) value.toLong().toString() else value.toString().replace('.', ',')

/** Liest ein eingegebenes Gewicht; akzeptiert Komma und Punkt. Liefert `null` bei ungültiger Eingabe. */
fun parseWeight(text: String): Double? =
    text.trim().replace(',', '.').toDoubleOrNull()?.takeIf { it >= 0.0 }

/** Liest eine positive ganze Zahl (Sätze, Wiederholungen). Liefert `null` bei ungültiger Eingabe. */
fun parsePositiveInt(text: String): Int? =
    text.trim().toIntOrNull()?.takeIf { it > 0 }
