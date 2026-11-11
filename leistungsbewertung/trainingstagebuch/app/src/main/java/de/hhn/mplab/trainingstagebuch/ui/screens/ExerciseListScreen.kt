package de.hhn.mplab.trainingstagebuch.ui.screens

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import de.hhn.mplab.trainingstagebuch.model.MuscleGroup
import de.hhn.mplab.trainingstagebuch.ui.TrainingUiState
import de.hhn.mplab.trainingstagebuch.ui.components.AddExerciseDialog
import de.hhn.mplab.trainingstagebuch.ui.components.ExerciseList
import de.hhn.mplab.trainingstagebuch.ui.components.VolumeSummary
import de.hhn.mplab.trainingstagebuch.ui.theme.Dimens

/** Startansicht: Gesamtvolumen, Filter und die Liste der heutigen Übungen. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseListScreen(
    state: TrainingUiState,
    onToggleDone: (Long) -> Unit,
    onDelete: (Long) -> Unit,
    onEdit: (Long) -> Unit,
    onAdd: (String, MuscleGroup, Int, Int, Double) -> Unit,
    onShowOnlyOpenChange: (Boolean) -> Unit,
    onStartNewSession: () -> Unit,
) {
    var showAddDialog by rememberSaveable { mutableStateOf(false) }
    var menuExpanded by remember { mutableStateOf(false) }

    // Muskelgruppen-Filter; leer bedeutet "alle anzeigen"
    val selectedGroups = remember { mutableListOf<MuscleGroup>() }

    val shownExercises = state.visibleExercises.filter {
        selectedGroups.isEmpty() || it.muscleGroup in selectedGroups
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Trainingstagebuch") },
                actions = {
                    Box {
                        IconButton(onClick = { menuExpanded = true }) {
                            Icon(Icons.Filled.MoreVert, contentDescription = "Weitere Optionen")
                        }
                        DropdownMenu(
                            expanded = menuExpanded,
                            onDismissRequest = { menuExpanded = false },
                        ) {
                            DropdownMenuItem(
                                text = { Text("Neue Trainingseinheit") },
                                leadingIcon = { Icon(Icons.Filled.Refresh, contentDescription = null) },
                                onClick = {
                                    menuExpanded = false
                                    onStartNewSession()
                                },
                            )
                        }
                    }
                },
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showAddDialog = true },
                icon = { Icon(Icons.Filled.Add, contentDescription = null) },
                text = { Text("Übung") },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            VolumeSummary(
                totalVolumeKg = state.totalVolumeKg,
                doneCount = state.doneCount,
                totalCount = state.exercises.size,
                modifier = Modifier.padding(
                    start = Dimens.ScreenPadding,
                    end = Dimens.ScreenPadding,
                    top = Dimens.ItemSpacing,
                ),
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = Dimens.ScreenPadding, vertical = Dimens.ItemSpacing),
                horizontalArrangement = Arrangement.spacedBy(Dimens.ItemSpacing),
            ) {
                FilterChip(
                    selected = state.showOnlyOpen,
                    onClick = { onShowOnlyOpenChange(!state.showOnlyOpen) },
                    label = { Text("Nur offene") },
                )
                MuscleGroup.entries.forEach { group ->
                    FilterChip(
                        selected = group in selectedGroups,
                        onClick = {
                            if (group in selectedGroups) selectedGroups.remove(group) else selectedGroups.add(group)
                        },
                        label = { Text(group.label) },
                    )
                }
            }

            if (shownExercises.isEmpty()) {
                EmptyListHint(
                    filtered = state.exercises.isNotEmpty(),
                    modifier = Modifier.weight(1f),
                )
            } else {
                ExerciseList(
                    exercises = shownExercises,
                    onToggleDone = onToggleDone,
                    onDelete = onDelete,
                    onEdit = onEdit,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }

    if (showAddDialog) {
        AddExerciseDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { name, group, sets, reps, weight ->
                onAdd(name, group, sets, reps, weight)
                showAddDialog = false
            },
        )
    }
}

@Composable
private fun EmptyListHint(filtered: Boolean, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(Dimens.ScreenPadding),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = if (filtered) {
                "Keine Übungen für diesen Filter."
            } else {
                "Noch keine Übungen. Lege mit \"Übung\" die erste an."
            },
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}
