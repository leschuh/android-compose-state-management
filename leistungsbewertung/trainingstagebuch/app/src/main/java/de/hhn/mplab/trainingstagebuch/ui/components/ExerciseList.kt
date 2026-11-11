package de.hhn.mplab.trainingstagebuch.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.hhn.mplab.trainingstagebuch.model.Exercise
import de.hhn.mplab.trainingstagebuch.model.SampleData
import de.hhn.mplab.trainingstagebuch.ui.formatKg
import de.hhn.mplab.trainingstagebuch.ui.theme.Dimens
import de.hhn.mplab.trainingstagebuch.ui.theme.TrainingstagebuchTheme

/** Scrollbare Liste der Übungen. */
@Composable
fun ExerciseList(
    exercises: List<Exercise>,
    onToggleDone: (Long) -> Unit,
    onDelete: (Long) -> Unit,
    onEdit: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(
            start = Dimens.ScreenPadding,
            end = Dimens.ScreenPadding,
            top = Dimens.ItemSpacing,
            bottom = Dimens.FabClearance,
        ),
        verticalArrangement = Arrangement.spacedBy(Dimens.ItemSpacing),
    ) {
        items(exercises) { exercise ->
            ExerciseCard(
                exercise = exercise,
                onToggleDone = { onToggleDone(exercise.id) },
                onDelete = { onDelete(exercise.id) },
                onEdit = { onEdit(exercise.id) },
            )
        }
    }
}

/**
 * Karte für eine Übung. Ein Tipp auf die Karte klappt Details auf;
 * Abhaken und Löschen sind direkt erreichbar.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseCard(
    exercise: Exercise,
    onToggleDone: () -> Unit,
    onDelete: () -> Unit,
    onEdit: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        onClick = { expanded = !expanded },
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(),
    ) {
        Column(modifier = Modifier.padding(vertical = Dimens.ItemSpacing)) {
            Row(
                modifier = Modifier.padding(start = Dimens.ItemSpacing, end = Dimens.ItemSpacing),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Checkbox(checked = exercise.done, onCheckedChange = { onToggleDone() })
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = exercise.name,
                        style = MaterialTheme.typography.titleMedium,
                        textDecoration = if (exercise.done) TextDecoration.LineThrough else TextDecoration.None,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = "${exercise.sets} × ${exercise.reps} · ${formatKg(exercise.weightKg)}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Icon(
                    imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                    contentDescription = if (expanded) "Details ausblenden" else "Details anzeigen",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                IconButton(onClick = onDelete) {
                    Icon(Icons.Filled.Delete, contentDescription = "Übung löschen")
                }
            }

            if (expanded) {
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = Dimens.ItemSpacing),
                    color = MaterialTheme.colorScheme.outlineVariant,
                )
                Column(
                    modifier = Modifier.padding(horizontal = Dimens.CardPadding),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    DetailRow(label = "Muskelgruppe", value = exercise.muscleGroup.label)
                    DetailRow(label = "Volumen", value = formatKg(exercise.volumeKg))
                    DetailRow(label = "Status", value = if (exercise.done) "Erledigt" else "Offen")
                }
                TextButton(
                    onClick = onEdit,
                    modifier = Modifier.padding(start = Dimens.ItemSpacing, top = 4.dp),
                ) {
                    Icon(Icons.Filled.Edit, contentDescription = null, modifier = Modifier.size(ButtonDefaults.IconSize))
                    Spacer(Modifier.width(ButtonDefaults.IconSpacing))
                    Text("Bearbeiten")
                }
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f),
        )
        Text(text = value, style = MaterialTheme.typography.bodyMedium)
    }
}

@Preview(showBackground = true)
@Composable
private fun ExerciseCardPreview() {
    TrainingstagebuchTheme {
        ExerciseCard(
            exercise = SampleData.exercises.first(),
            onToggleDone = {},
            onDelete = {},
            onEdit = {},
            modifier = Modifier.padding(Dimens.ScreenPadding),
        )
    }
}
