package de.hhn.mplab.statemanagement

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import de.hhn.mplab.statemanagement.stufen.Stufe1Screen
import de.hhn.mplab.statemanagement.stufen.Stufe2Screen
import de.hhn.mplab.statemanagement.stufen.Stufe3Screen
import de.hhn.mplab.statemanagement.stufen.Stufe4Screen
import de.hhn.mplab.statemanagement.stufen.Stufe5Screen
import de.hhn.mplab.statemanagement.ui.theme.StateManagementTheme
import de.hhn.mplab.statemanagement.uebung1.Uebung1Screen

/**
 * Einstieg der Beispiel-App.
 *
 * Alle Stufen liegen in einer App, damit man in der Vorlesung zwischen ihnen wechseln kann.
 * Navigation ohne Library: Der aktuelle Screen ist selbst nur ein State (ein String).
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StateManagementTheme {
                App()
            }
        }
    }
}

/** Ein Eintrag auf dem Startbildschirm. */
private data class Destination(val route: String, val title: String, val description: String)

private val destinations = listOf(
    Destination("stufe1", "Stufe 1", "Lokaler State mit remember. Funktioniert, bis man das Gerät dreht."),
    Destination("stufe2", "Stufe 2", "Zwei typische Bugs: nicht beobachtbare Liste und Rotation."),
    Destination("stufe3", "Stufe 3", "rememberSaveable: Liste, Eingabe und Filter überstehen das Drehen."),
    Destination("stufe4", "Stufe 4", "State Hoisting: zustandsloses ShoppingListContent mit Preview."),
    Destination("stufe5", "Stufe 5", "ViewModel und StateFlow als Single Source of Truth."),
    Destination("uebung1", "Übung 1 (Start)", "Startcode: Zähler, Notiz und Liste mit drei State-Fehlern."),
    Destination("uebung2", "Übung 2", "Ausgangspunkt Stufe 5: Löschen und Anzeige der offenen Einträge."),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    // Aktueller Screen, null = Startbildschirm.
    // rememberSaveable, damit man nach dem Drehen auf demselben Screen bleibt.
    var currentRoute by rememberSaveable { mutableStateOf<String?>(null) }

    // Zurück-Taste bzw. -Geste führt zum Startbildschirm statt die App zu schließen.
    BackHandler(enabled = currentRoute != null) { currentRoute = null }

    val title = destinations.find { it.route == currentRoute }?.title ?: "State Management"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    if (currentRoute != null) {
                        TextButton(onClick = { currentRoute = null }) { Text("Zurück") }
                    }
                },
            )
        },
    ) { innerPadding ->
        val modifier = Modifier.padding(innerPadding)
        when (currentRoute) {
            "stufe1" -> Stufe1Screen(modifier)
            "stufe2" -> Stufe2Screen(modifier)
            "stufe3" -> Stufe3Screen(modifier)
            "stufe4" -> Stufe4Screen(modifier)
            "stufe5" -> Stufe5Screen(modifier)
            "uebung1" -> Uebung1Screen(modifier)
            // Übung 2 arbeitet direkt am Code von Stufe 5 (siehe uebungen/uebung-2.md).
            "uebung2" -> Stufe5Screen(modifier)
            else -> StartScreen(onOpen = { currentRoute = it }, modifier = modifier)
        }
    }
}

@Composable
private fun StartScreen(onOpen: (String) -> Unit, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        item {
            Text(
                text = "UI = f(state)",
                style = MaterialTheme.typography.labelLarge.copy(fontFamily = FontFamily.Monospace),
                color = MaterialTheme.colorScheme.primary,
            )
            Text(
                text = "Beispiel-App zur Lerneinheit. Jede Stufe löst ein Problem der vorherigen.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp, bottom = 8.dp),
            )
        }
        itemsIndexed(destinations) { index, destination ->
            DestinationCard(number = index + 1, destination = destination, onClick = { onOpen(destination.route) })
        }
    }
}

/** Eintrag auf dem Startbildschirm: Nummer als Badge, Titel und kurze Erklärung. */
@Composable
private fun DestinationCard(number: Int, destination: Destination, onClick: () -> Unit) {
    val isExercise = destination.route.startsWith("uebung")
    Surface(
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceContainer,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(16.dp)) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = if (isExercise) MaterialTheme.colorScheme.tertiaryContainer else MaterialTheme.colorScheme.primaryContainer,
                        shape = CircleShape,
                    ),
            ) {
                Text(
                    text = if (isExercise) "Ü" else number.toString(),
                    style = MaterialTheme.typography.titleMedium.copy(fontFamily = FontFamily.Monospace),
                    color = if (isExercise) MaterialTheme.colorScheme.onTertiaryContainer else MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }
            Spacer(Modifier.width(16.dp))
            Column {
                Text(destination.title, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = destination.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}
