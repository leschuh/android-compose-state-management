/**
 * Stufe 4: State Hoisting.
 *
 * Zeigt: Die Oberfläche steckt in der zustandslosen Funktion `ShoppingListContent`. Sie
 * bekommt den Zustand als Parameter und meldet Ereignisse über Lambdas nach oben
 * (State fließt nach unten, Events fließen nach oben: Unidirectional Data Flow).
 * Der State liegt im zustandsbehafteten Wrapper `Stufe4Screen`.
 * Löst: `ShoppingListContent` ist wiederverwendbar, testbar und hat eine Preview.
 * Problem: Die Logik (Hinzufügen, Abhaken, Filtern) steckt weiterhin im Composable und
 * ist an die UI gebunden. Lösung in Stufe 5.
 */
package de.hhn.mplab.statemanagement.stufen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.hhn.mplab.statemanagement.model.Item
import de.hhn.mplab.statemanagement.ui.theme.StateManagementTheme

/** Zustandsbehafteter Wrapper: hält den State und reicht ihn an ShoppingListContent weiter. */
@Composable
fun Stufe4Screen(modifier: Modifier = Modifier) {
    var items by rememberSaveable { mutableStateOf(listOf<Item>()) }
    var input by rememberSaveable { mutableStateOf("") }
    var showOnlyOpen by rememberSaveable { mutableStateOf(false) }

    ShoppingListContent(
        items = if (showOnlyOpen) items.filterNot { it.done } else items,
        input = input,
        onInputChange = { input = it },
        onAdd = {
            if (input.isNotBlank()) {
                items = items + Item(id = items.size + 1L, name = input.trim())
                input = ""
            }
        },
        onToggle = { id ->
            items = items.map { if (it.id == id) it.copy(done = !it.done) else it }
        },
        showOnlyOpen = showOnlyOpen,
        onFilterChange = { showOnlyOpen = it },
        modifier = modifier,
    )
}

/**
 * Zustandslose Einkaufsliste: kein `remember`, kein ViewModel.
 * Alles, was angezeigt wird, kommt als Parameter herein. Alles, was passiert, geht als Event raus.
 */
@Composable
fun ShoppingListContent(
    items: List<Item>,
    input: String,
    onInputChange: (String) -> Unit,
    onAdd: () -> Unit,
    onToggle: (Long) -> Unit,
    showOnlyOpen: Boolean,
    onFilterChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = input,
                onValueChange = onInputChange,
                label = { Text("Neuer Eintrag") },
                singleLine = true,
                modifier = Modifier.weight(1f),
            )
            Spacer(Modifier.width(16.dp))
            Button(onClick = onAdd) {
                Text("Hinzufügen")
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = showOnlyOpen, onCheckedChange = onFilterChange)
            Text("Nur offene anzeigen")
        }
        Spacer(Modifier.height(16.dp))
        LazyColumn {
            items(items, key = { it.id }) { item ->
                ShoppingItemRow(item = item, onToggle = onToggle)
            }
        }
    }
}

/** Ein Eintrag als Karte. Auch zustandslos: `done` kommt aus dem Item, Abhaken geht als Event raus. */
@Composable
private fun ShoppingItemRow(item: Item, onToggle: (Long) -> Unit) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceContainer,
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 4.dp)) {
            Checkbox(checked = item.done, onCheckedChange = { onToggle(item.id) })
            Text(
                text = item.name,
                style = MaterialTheme.typography.bodyLarge,
                color = if (item.done) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface,
                textDecoration = if (item.done) TextDecoration.LineThrough else null,
            )
        }
    }
}

/** Preview: geht nur, weil ShoppingListContent zustandslos ist. Daten einfach hineingeben. */
@Preview(showBackground = true)
@Composable
private fun ShoppingListContentPreview() {
    StateManagementTheme {
        ShoppingListContent(
            items = listOf(
                Item(id = 1, name = "Milch"),
                Item(id = 2, name = "Brot", done = true),
                Item(id = 3, name = "Äpfel"),
            ),
            input = "Käse",
            onInputChange = {},
            onAdd = {},
            onToggle = {},
            showOnlyOpen = false,
            onFilterChange = {},
        )
    }
}
