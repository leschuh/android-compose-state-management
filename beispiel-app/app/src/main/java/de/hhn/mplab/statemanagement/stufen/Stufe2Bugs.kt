/**
 * Stufe 2: Zwei typische State-Bugs (absichtlich eingebaut).
 *
 * Zeigt:
 * (a) Die Liste ist eine `mutableListOf`. Compose beobachtet sie nicht: `add()` und das
 *     Ersetzen beim Abhaken lösen keine Recomposition aus, die UI bleibt stehen.
 *     Erst wenn sich irgendein beobachteter State ändert (z. B. ein Zeichen tippen),
 *     erscheinen die Änderungen plötzlich.
 * (b) Alles liegt in `remember`. Beim Drehen ist der gesamte Zustand weg.
 * Löst: nichts, diese Stufe ist eine Bug-Demo („Aktualisiert sich die Liste?“,
 * „Was passiert beim Drehen?“).
 * Problem: Der Zustand ist nicht beobachtbar und nicht gesichert. Lösung in Stufe 3.
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
fun Stufe2Screen(modifier: Modifier = Modifier) {
    // BUG (a): mutableListOf ist eine normale Kotlin-Liste, kein Compose-State.
    // add() verändert die Liste, aber Compose bekommt davon nichts mit -> keine Recomposition.
    // Richtig: var items by remember { mutableStateOf(listOf<Item>()) } (Stufe 1)
    // oder remember { mutableStateListOf<Item>() }.
    val items = remember { mutableListOf<Item>() }

    // BUG (b): remember überlebt keine Konfigurationsänderung. Drehen -> Eingabe und Liste weg.
    // Richtig: rememberSaveable (Stufe 3).
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
                    // BUG (a): Die Liste wächst, aber die UI zeigt den neuen Eintrag nicht an.
                    items.add(Item(id = items.size + 1L, name = input.trim()))
                    // Hinweis: Die Eingabe wird hier absichtlich NICHT geleert. `input = ""` würde
                    // eine Recomposition auslösen und dabei die Liste neu zeichnen. Der Bug wäre
                    // dann verdeckt. Genau das macht solche Fehler im echten Code tückisch.
                }
            }) {
                Text("Hinzufügen")
            }
        }
        Spacer(Modifier.height(16.dp))
        items.forEachIndexed { index, item ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = item.done,
                    onCheckedChange = {
                        // BUG (a): Ersetzen in der mutableListOf ist ebenfalls unsichtbar,
                        // die Checkbox bleibt optisch unverändert.
                        items[index] = item.copy(done = !item.done)
                    },
                )
                Text(item.name)
            }
        }
    }
}
