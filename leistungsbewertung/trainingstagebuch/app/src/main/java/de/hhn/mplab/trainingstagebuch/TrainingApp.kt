package de.hhn.mplab.trainingstagebuch

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import de.hhn.mplab.trainingstagebuch.ui.TrainingViewModel
import de.hhn.mplab.trainingstagebuch.ui.screens.ExerciseEditScreen
import de.hhn.mplab.trainingstagebuch.ui.screens.ExerciseListScreen

/**
 * Einstiegspunkt der Oberfläche.
 *
 * Es gibt zwei Ansichten: die Übungsliste und die Detail-/Bearbeitungsansicht.
 * Welche angezeigt wird, ergibt sich aus der Auswahl im [TrainingViewModel].
 */
@Composable
fun TrainingApp() {
    val viewModel = remember { TrainingViewModel() }
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    val selected = state.selectedExercise
    if (selected != null) {
        val position = state.exercises.indexOfFirst { it.id == selected.id } + 1
        ExerciseEditScreen(
            exercise = selected,
            position = position,
            count = state.exercises.size,
            onPrevious = viewModel::onSelectPrevious,
            onNext = viewModel::onSelectNext,
            onSave = viewModel::onSaveExercise,
            onDelete = { viewModel.onDeleteExercise(selected.id) },
            onBack = viewModel::onCloseDetail,
        )
    } else {
        ExerciseListScreen(
            state = state,
            onToggleDone = viewModel::onToggleDone,
            onDelete = viewModel::onDeleteExercise,
            onEdit = viewModel::onSelectExercise,
            onAdd = viewModel::onAddExercise,
            onShowOnlyOpenChange = viewModel::onShowOnlyOpenChange,
            onStartNewSession = viewModel::onStartNewSession,
        )
    }
}
