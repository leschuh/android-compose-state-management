package de.hhn.mplab.trainingstagebuch.model

/**
 * Eine Übung im heutigen Training.
 *
 * @property sets Anzahl der Sätze
 * @property reps Wiederholungen pro Satz
 * @property weightKg Gewicht pro Wiederholung in Kilogramm
 * @property done ob die Übung heute bereits absolviert wurde
 */
data class Exercise(
    val id: Long,
    val name: String,
    val muscleGroup: MuscleGroup,
    val sets: Int,
    val reps: Int,
    val weightKg: Double,
    val done: Boolean = false,
) {
    /** Trainingsvolumen dieser Übung: Sätze × Wiederholungen × Gewicht. */
    val volumeKg: Double
        get() = sets * reps * weightKg
}
