package de.hhn.mplab.trainingstagebuch.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextOverflow
import de.hhn.mplab.trainingstagebuch.model.Exercise
import de.hhn.mplab.trainingstagebuch.ui.components.NumberField
import de.hhn.mplab.trainingstagebuch.ui.formatKg
import de.hhn.mplab.trainingstagebuch.ui.parsePositiveInt
import de.hhn.mplab.trainingstagebuch.ui.parseWeight
import de.hhn.mplab.trainingstagebuch.ui.theme.Dimens
import de.hhn.mplab.trainingstagebuch.ui.weightToInput

/**
 * Detail- und Bearbeitungsansicht einer Übung.
 *
 * Änderungen werden erst mit "Speichern" übernommen. Mit "Vorherige"/"Nächste"
 * blättert man durch die Übungen des Trainings, ohne zur Liste zurückzukehren.
 *
 * @param position Position der Übung in der Liste (ab 1)
 * @param count Anzahl aller Übungen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseEditScreen(
    exercise: Exercise,
    position: Int,
    count: Int,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    onSave: (Exercise) -> Unit,
    onDelete: () -> Unit,
    onBack: () -> Unit,
) {
    BackHandler(onBack = onBack)

    // Bearbeitbare Werte; die Felder starten mit den gespeicherten Werten der Übung.
    var name by rememberSaveable { mutableStateOf(exercise.name) }
    var sets by rememberSaveable { mutableStateOf(exercise.sets.toString()) }
    var reps by rememberSaveable { mutableStateOf(exercise.reps.toString()) }
    var weight by rememberSaveable { mutableStateOf(weightToInput(exercise.weightKg)) }

    val parsedSets = parsePositiveInt(sets)
    val parsedReps = parsePositiveInt(reps)
    val parsedWeight = parseWeight(weight)
    val previewVolume = if (parsedSets != null && parsedReps != null && parsedWeight != null) {
        parsedSets * parsedReps * parsedWeight
    } else {
        null
    }
    val canSave = name.isNotBlank() && previewVolume != null

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = exercise.name,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                        Text(
                            text = "Übung $position von $count",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Zurück")
                    }
                },
                actions = {
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Filled.Delete, contentDescription = "Übung löschen")
                    }
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(Dimens.ScreenPadding),
            verticalArrangement = Arrangement.spacedBy(Dimens.ItemSpacing),
        ) {
            InfoRow(label = "Muskelgruppe", value = exercise.muscleGroup.label)
            InfoRow(label = "Status", value = if (exercise.done) "Erledigt" else "Offen")

            HorizontalDivider(modifier = Modifier.padding(vertical = Dimens.ItemSpacing))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Übung") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Next,
                ),
                modifier = Modifier.fillMaxWidth(),
            )
            Row(horizontalArrangement = Arrangement.spacedBy(Dimens.ItemSpacing)) {
                NumberField(
                    value = sets,
                    onValueChange = { sets = it },
                    label = "Sätze",
                    modifier = Modifier.weight(1f),
                )
                NumberField(
                    value = reps,
                    onValueChange = { reps = it },
                    label = "Wiederholungen",
                    modifier = Modifier.weight(1f),
                )
            }
            NumberField(
                value = weight,
                onValueChange = { weight = it },
                label = "Gewicht (kg)",
                decimal = true,
                modifier = Modifier.fillMaxWidth(),
            )

            InfoRow(
                label = "Volumen",
                value = previewVolume?.let { formatKg(it) } ?: "–",
            )

            Spacer(Modifier.size(Dimens.ItemSpacing))

            Button(
                onClick = {
                    if (canSave && parsedSets != null && parsedReps != null && parsedWeight != null) {
                        onSave(
                            exercise.copy(
                                name = name.trim(),
                                sets = parsedSets,
                                reps = parsedReps,
                                weightKg = parsedWeight,
                            )
                        )
                    }
                },
                enabled = canSave,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Speichern")
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Dimens.ItemSpacing),
            ) {
                OutlinedButton(
                    onClick = onPrevious,
                    enabled = position > 1,
                    modifier = Modifier.weight(1f),
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = null,
                        modifier = Modifier.size(ButtonDefaults.IconSize),
                    )
                    Spacer(Modifier.width(ButtonDefaults.IconSpacing))
                    Text("Vorherige")
                }
                OutlinedButton(
                    onClick = onNext,
                    enabled = position < count,
                    modifier = Modifier.weight(1f),
                ) {
                    Text("Nächste")
                    Spacer(Modifier.width(ButtonDefaults.IconSpacing))
                    Icon(
                        Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        modifier = Modifier.size(ButtonDefaults.IconSize),
                    )
                }
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f),
        )
        Text(text = value, style = MaterialTheme.typography.bodyLarge)
    }
}
