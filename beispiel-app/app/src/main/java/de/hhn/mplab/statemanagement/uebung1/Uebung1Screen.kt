package de.hhn.mplab.statemanagement.uebung1

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import de.hhn.mplab.statemanagement.model.Item

@Composable
fun Uebung1Screen(modifier: Modifier = Modifier) {
    var boughtCount by remember { mutableStateOf(0) }
    var note by remember { mutableStateOf("") }
    val items = remember { mutableListOf<Item>() }

    val scrollState = rememberScrollState()

    Column(modifier = modifier.padding(16.dp).verticalScroll(scrollState)) {
        Text("Gekaufte Artikel: $boughtCount", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(16.dp))
        Button(onClick = { boughtCount++ }) {
            Text("Artikel gekauft")
        }
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = note,
            onValueChange = { note = it },
            label = { Text("Notiz zum Einkauf") },
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(16.dp))
        Button(onClick = {
            val itemNote = if (note.isNotBlank()) note else "keine Notiz"

            items.add(Item(id = items.size + 1L, name = "Artikel $boughtCount|||$itemNote"))

        }) {
            Text("Artikel zur Liste hinzufügen")
        }
        Spacer(Modifier.height(16.dp))
        items.forEachIndexed { index, item ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = item.done,
                    onCheckedChange = { items[index] = item.copy(done = !item.done) },
                )
                Column {
                    val parts = item.name.split("|||")
                    val title = parts.getOrElse(0) { item.name }
                    val subtitle = parts.getOrElse(1) { "" }

                    Text(title)

                    if (subtitle == "keine Notiz") {
                        Text(subtitle, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                    } else if (subtitle.isNotBlank()) {
                        Text(subtitle, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}
