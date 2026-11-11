/**
 * Stufe 1: Lokaler State mit `remember`.
 *
 * Zeigt: Liste und Eingabe liegen in `mutableStateOf`. Jede Zuweisung eines neuen Werts
 * löst eine Recomposition aus, die Oberfläche zeigt den neuen Zustand (UI = f(State)).
 * Löst: Die UI reagiert auf Eingaben.
 * Problem: `remember` übersteht keine Konfigurationsänderung. Beim Drehen wird die Activity
 * neu erzeugt, Liste und Eingabe sind weg.
 */
package de.hhn.mplab.statemanagement.stufen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.hhn.mplab.statemanagement.model.Item

@Composable
fun Stufe1Screen(modifier: Modifier = Modifier) {
    // State 1: die Liste. Unveränderliche Liste, eine Änderung weist eine NEUE Liste zu.
    var items by remember { mutableStateOf(listOf<Item>()) }
    // State 2: der Text im Eingabefeld.
    var input by remember { mutableStateOf("") }

    Column(modifier = modifier.padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                label = { Text("Neuer Eintrag") },
                singleLine = true,
                modifier = Modifier.weight(1f),
            )
            Spacer(Modifier.width(16.dp))
            Button(onClick = {
                if (input.isNotBlank()) {
                    // Neue Liste zuweisen -> State ändert sich -> Recomposition.
                    items = items + Item(id = items.size + 1L, name = input.trim())
                    input = ""
                }
            }) {
                Text("Hinzufügen")
            }
        }
        Spacer(Modifier.height(16.dp))
        items.forEach { item ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = item.done,
                    onCheckedChange = {
                        // Abhaken: neue Liste, in der genau dieser Eintrag ersetzt ist.
                        items = items.map { if (it.id == item.id) it.copy(done = !it.done) else it }
                    },
                )
                Text(item.name)
            }
        }
    }
}
