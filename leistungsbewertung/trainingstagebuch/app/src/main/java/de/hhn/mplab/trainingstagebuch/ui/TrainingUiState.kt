package de.hhn.mplab.trainingstagebuch.ui

import de.hhn.mplab.trainingstagebuch.model.Exercise

/**
 * Zustand der gesamten Oberfläche, wie ihn das [TrainingViewModel] bereitstellt.
 *
 * @property exercises alle Übungen des heutigen Trainings in der angelegten Reihenfolge
 * @property showOnlyOpen Filter "Nur offene"
 * @property selectedExerciseId Übung, die gerade in der Detailansicht geöffnet ist (`null` = Liste)
 * @property totalVolumeKg Gesamtvolumen aller erledigten Übungen
 */
data class TrainingUiState(
    val exercises: List<Exercise> = emptyList(),
    val showOnlyOpen: Boolean = false,
    val selectedExerciseId: Long? = null,
    val totalVolumeKg: Double = 0.0,
) {
    /** Übungen, die die Liste unter Berücksichtigung des Filters anzeigt. */
    val visibleExercises: List<Exercise>
        get() = if (showOnlyOpen) exercises.filterNot { it.done } else exercises

    /** Anzahl der bereits erledigten Übungen. */
    val doneCount: Int
        get() = exercises.count { it.done }

    /** Die in der Detailansicht geöffnete Übung, falls vorhanden. */
    val selectedExercise: Exercise?
        get() = exercises.find { it.id == selectedExerciseId }
}
