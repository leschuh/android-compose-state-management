package de.hhn.mplab.trainingstagebuch.ui

import androidx.lifecycle.ViewModel
import de.hhn.mplab.trainingstagebuch.model.Exercise
import de.hhn.mplab.trainingstagebuch.model.MuscleGroup
import de.hhn.mplab.trainingstagebuch.model.SampleData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Hält die Übungen des heutigen Trainings und verarbeitet alle Benutzeraktionen.
 *
 * Die Oberfläche liest ausschließlich [uiState] und meldet Aktionen über die `on…`-Funktionen.
 */
class TrainingViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        TrainingUiState(
            exercises = SampleData.exercises,
            totalVolumeKg = volumeOf(SampleData.exercises),
        )
    )
    val uiState: StateFlow<TrainingUiState> = _uiState.asStateFlow()

    private var nextId: Long = SampleData.exercises.maxOf { it.id } + 1

    fun onAddExercise(name: String, muscleGroup: MuscleGroup, sets: Int, reps: Int, weightKg: Double) {
        val exercise = Exercise(
            id = nextId++,
            name = name.trim(),
            muscleGroup = muscleGroup,
            sets = sets,
            reps = reps,
            weightKg = weightKg,
        )
        _uiState.update { it.copy(exercises = it.exercises + exercise) }
    }

    fun onToggleDone(id: Long) {
        _uiState.update { state ->
            val updated = state.exercises.map { if (it.id == id) it.copy(done = !it.done) else it }
            state.copy(exercises = updated, totalVolumeKg = volumeOf(updated))
        }
    }

    fun onDeleteExercise(id: Long) {
        _uiState.update { state ->
            state.copy(
                exercises = state.exercises.filterNot { it.id == id },
                // Wird die geöffnete Übung gelöscht, geht es zurück zur Liste.
                selectedExerciseId = state.selectedExerciseId.takeIf { it != id },
            )
        }
    }

    /** Übernimmt die Änderungen aus der Bearbeitungsansicht und kehrt zur Liste zurück. */
    fun onSaveExercise(exercise: Exercise) {
        _uiState.update { state ->
            val updated = state.exercises.map { if (it.id == exercise.id) exercise else it }
            state.copy(exercises = updated, totalVolumeKg = volumeOf(updated), selectedExerciseId = null)
        }
    }

    /** Setzt alle Haken zurück, die Übungen selbst bleiben erhalten. */
    fun onStartNewSession() {
        _uiState.update { state ->
            val updated = state.exercises.map { it.copy(done = false) }
            state.copy(exercises = updated, totalVolumeKg = volumeOf(updated))
        }
    }

    fun onShowOnlyOpenChange(showOnlyOpen: Boolean) {
        _uiState.update { it.copy(showOnlyOpen = showOnlyOpen) }
    }

    fun onSelectExercise(id: Long) {
        _uiState.update { it.copy(selectedExerciseId = id) }
    }

    fun onSelectPrevious() {
        moveSelection(-1)
    }

    fun onSelectNext() {
        moveSelection(+1)
    }

    fun onCloseDetail() {
        _uiState.update { it.copy(selectedExerciseId = null) }
    }

    private fun moveSelection(offset: Int) {
        _uiState.update { state ->
            val index = state.exercises.indexOfFirst { it.id == state.selectedExerciseId }
            val target = state.exercises.getOrNull(index + offset) ?: return@update state
            state.copy(selectedExerciseId = target.id)
        }
    }

    private fun volumeOf(exercises: List<Exercise>): Double =
        exercises.filter { it.done }.sumOf { it.volumeKg }
}
