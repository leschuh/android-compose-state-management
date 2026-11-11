/**
 * Stufe 3: `rememberSaveable`.
 *
 * Zeigt: Liste, Eingabe und Filter liegen in `rememberSaveable`. Der Wert wird vor dem
 * Neuaufbau der Activity in einem Bundle gesichert und danach wiederhergestellt.
 * Neu: Filter „Nur offene anzeigen“.
 * Löst: Der Zustand übersteht das Drehen.
 * Problem: Nur Bundle-fähige Typen lassen sich sichern. Ohne `@Parcelize` an `Item` stürzt
 * die App ab, sobald sie in den Hintergrund geht (beim Drehen fällt es noch nicht auf). Außerdem stecken State und Oberfläche noch in einer Funktion:
 * nicht wiederverwendbar, schwer testbar, keine sinnvolle Preview. Lösung in Stufe 4.
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.hhn.mplab.statemanagement.model.Item

@Composable
fun Stufe3Screen(modifier: Modifier = Modifier) {
    // rememberSaveable statt remember: übersteht das Drehen.
    // Funktioniert nur, weil Item @Parcelize ist (sonst Absturz, sobald die App in den Hintergrund geht).
    var items by rememberSaveable { mutableStateOf(listOf<Item>()) }
    var input by rememberSaveable { mutableStateOf("") }
    var showOnlyOpen by rememberSaveable { mutableStateOf(false) }

    // Abgeleiteter Wert: kein eigener State, wird bei jeder Recomposition neu berechnet.
    val visibleItems = if (showOnlyOpen) items.filterNot { it.done } else items

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
                    items = items + Item(id = items.size + 1L, name = input.trim())
                    input = ""
                }
            }) {
                Text("Hinzufügen")
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = showOnlyOpen, onCheckedChange = { showOnlyOpen = it })
            Text("Nur offene anzeigen")
        }
        Spacer(Modifier.height(16.dp))
        visibleItems.forEach { item ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = item.done,
                    onCheckedChange = {
                        items = items.map { if (it.id == item.id) it.copy(done = !it.done) else it }
                    },
                )
                Text(item.name)
            }
        }
    }
}
