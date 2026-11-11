package de.hhn.mplab.trainingstagebuch.submission

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

/**
 * Legt den Abgabe-Balken unter die eigentliche App.
 *
 * Der Balken bekommt eine eigene Zeile am unteren Rand, damit er nichts aus [content] verdeckt.
 * Die untere Systemleiste übernimmt der Balken, [content] bekommt diesen Abstand deshalb nicht noch einmal.
 */
@Composable
fun SubmissionFrame(content: @Composable () -> Unit) {
    val viewModel: SubmissionViewModel = viewModel()
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val barInsets = WindowInsets.systemBars
        .union(WindowInsets.displayCutout)
        .only(WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom)

    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .consumeWindowInsets(barInsets.only(WindowInsetsSides.Bottom)),
        ) {
            content()
        }
        SubmissionBar(
            receipt = state.receipt,
            insets = barInsets,
            onClick = viewModel::onBarClick,
        )
    }

    val receipt = state.receipt
    when {
        state.overlay == Overlay.UNLOCK -> UnlockDialog(
            initial = receipt,
            isUnlocking = state.isUnlocking,
            error = state.unlockError,
            onUnlock = viewModel::onUnlock,
            onDismiss = viewModel::onDismiss,
        )
        state.overlay == Overlay.ASSIGNMENT && receipt != null -> AssignmentDialog(
            receipt = receipt,
            text = state.assignmentText,
            error = state.assignmentError,
            onChangeData = viewModel::onChangeData,
            onDismiss = viewModel::onDismiss,
        )
    }
}

@Composable
private fun SubmissionBar(
    receipt: Receipt?,
    insets: WindowInsets,
    onClick: () -> Unit,
) {
    Surface(
        color = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClickLabel = if (receipt == null) "Abgabe freischalten" else "Prüfauftrag anzeigen",
                onClick = onClick,
            ),
    ) {
        Row(
            modifier = Modifier
                .windowInsetsPadding(insets)
                .heightIn(min = 24.dp)
                .padding(horizontal = 16.dp, vertical = 3.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (receipt == null) {
                Icon(Icons.Filled.Lock, contentDescription = null, modifier = Modifier.size(14.dp))
                Spacer(Modifier.width(6.dp))
                Text(
                    text = "Abgabe freischalten: hier tippen",
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            } else {
                Text(
                    text = receipt.lastName,
                    style = MaterialTheme.typography.labelLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false),
                )
                Text(
                    text = " · ",
                    style = MaterialTheme.typography.labelLarge,
                )
                Text(
                    text = receipt.code,
                    style = MaterialTheme.typography.labelLarge,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                )
                Spacer(Modifier.weight(1f))
                Text(
                    text = "Prüfauftrag",
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    modifier = Modifier.padding(start = 12.dp),
                )
            }
        }
    }
}

@Composable
private fun UnlockDialog(
    initial: Receipt?,
    isUnlocking: Boolean,
    error: String?,
    onUnlock: (String, String, String) -> Unit,
    onDismiss: () -> Unit,
) {
    var lastName by rememberSaveable { mutableStateOf(initial?.lastName.orEmpty()) }
    var matriculation by rememberSaveable { mutableStateOf(initial?.matriculation.orEmpty()) }
    var unlockCode by rememberSaveable { mutableStateOf("") }
    val canSubmit = !isUnlocking && lastName.isNotBlank() && matriculation.isNotBlank() && unlockCode.isNotBlank()

    AlertDialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(dismissOnBackPress = !isUnlocking, dismissOnClickOutside = !isUnlocking),
        title = { Text("Abgabe freischalten") },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = "Den Freischaltcode finden Sie in ILIAS. Danach zeigt der Balken unten Ihren Beleg-Code.",
                    style = MaterialTheme.typography.bodyMedium,
                )
                OutlinedTextField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    label = { Text("Nachname") },
                    singleLine = true,
                    enabled = !isUnlocking,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        imeAction = ImeAction.Next,
                    ),
                    modifier = Modifier.fillMaxWidth(),
                )
                OutlinedTextField(
                    value = matriculation,
                    onValueChange = { matriculation = it },
                    label = { Text("Matrikelnummer") },
                    singleLine = true,
                    enabled = !isUnlocking,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next,
                    ),
                    modifier = Modifier.fillMaxWidth(),
                )
                OutlinedTextField(
                    value = unlockCode,
                    onValueChange = { unlockCode = it },
                    label = { Text("Freischaltcode") },
                    singleLine = true,
                    enabled = !isUnlocking,
                    isError = error != null,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Characters,
                        autoCorrectEnabled = false,
                        keyboardType = KeyboardType.Ascii,
                        imeAction = ImeAction.Done,
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { if (canSubmit) onUnlock(lastName, matriculation, unlockCode) },
                    ),
                    modifier = Modifier.fillMaxWidth(),
                )
                if (isUnlocking) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                        Spacer(Modifier.width(8.dp))
                        Text("Wird geprüft …", style = MaterialTheme.typography.bodyMedium)
                    }
                }
                if (error != null) {
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onUnlock(lastName, matriculation, unlockCode) },
                enabled = canSubmit,
            ) {
                Text("Freischalten")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss, enabled = !isUnlocking) {
                Text("Abbrechen")
            }
        },
    )
}

@Composable
private fun AssignmentDialog(
    receipt: Receipt,
    text: String?,
    error: String?,
    onChangeData: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Prüfauftrag") },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = "${receipt.lastName} · ${receipt.matriculation} · ${receipt.code}",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                )
                when {
                    error != null -> Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    text == null -> CircularProgressIndicator(modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                    else -> AssignmentText(text)
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Schließen")
            }
        },
        dismissButton = {
            TextButton(onClick = onChangeData) {
                Text("Daten ändern")
            }
        },
    )
}

/** Absätze sind durch Leerzeilen getrennt, ein Absatz mit "## " am Anfang ist eine Überschrift. */
@Composable
private fun AssignmentText(text: String) {
    text.split(Regex("""\n\s*\n""")).forEach { block ->
        if (block.startsWith("## ")) {
            Text(
                text = block.removePrefix("## ").trim(),
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(top = 4.dp),
            )
        } else {
            Text(text = block.trim(), style = MaterialTheme.typography.bodyMedium)
        }
    }
}
