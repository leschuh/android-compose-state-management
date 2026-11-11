package de.hhn.mplab.trainingstagebuch.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import de.hhn.mplab.trainingstagebuch.model.MuscleGroup
import de.hhn.mplab.trainingstagebuch.ui.parsePositiveInt
import de.hhn.mplab.trainingstagebuch.ui.parseWeight
import de.hhn.mplab.trainingstagebuch.ui.theme.Dimens

/**
 * Dialog zum Anlegen einer neuen Übung.
 *
 * @param onConfirm wird mit den eingegebenen Werten aufgerufen (Name, Muskelgruppe, Sätze, Wiederholungen, Gewicht)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExerciseDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, MuscleGroup, Int, Int, Double) -> Unit,
) {
    var name by remember { mutableStateOf("") }
    var muscleGroup by remember { mutableStateOf(MuscleGroup.BRUST) }
    var sets by remember { mutableStateOf("3") }
    var reps by remember { mutableStateOf("10") }
    var weight by remember { mutableStateOf("") }

    // "Hinzufügen" ist erst möglich, wenn alle Felder sinnvoll ausgefüllt sind.
    val isValid by remember {
        derivedStateOf {
            name.isNotBlank() &&
                parsePositiveInt(sets) != null &&
                parsePositiveInt(reps) != null &&
                parseWeight(weight) != null
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Neue Übung") },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(Dimens.ItemSpacing),
            ) {
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
                Text(
                    text = "Muskelgruppe",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(Dimens.ItemSpacing),
                ) {
                    MuscleGroup.entries.forEach { group ->
                        FilterChip(
                            selected = group == muscleGroup,
                            onClick = { muscleGroup = group },
                            label = { Text(group.label) },
                        )
                    }
                }
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
                        label = "Wdh.",
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
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val parsedSets = parsePositiveInt(sets)
                    val parsedReps = parsePositiveInt(reps)
                    val parsedWeight = parseWeight(weight)
                    if (parsedSets != null && parsedReps != null && parsedWeight != null) {
                        onConfirm(name, muscleGroup, parsedSets, parsedReps, parsedWeight)
                    }
                },
                enabled = isValid,
            ) {
                Text("Hinzufügen")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Abbrechen")
            }
        },
    )
}

/** Einzeiliges Eingabefeld mit Zahlentastatur. */
@Composable
fun NumberField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    decimal: Boolean = false,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = if (decimal) KeyboardType.Decimal else KeyboardType.Number,
            imeAction = ImeAction.Next,
        ),
        modifier = modifier,
    )
}
