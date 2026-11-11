/**
 * Stufe 5: ViewModel mit `StateFlow`.
 *
 * Zeigt: Der gesamte Zustand des Screens steckt in einem unveränderlichen
 * `ShoppingListUiState`. Das ViewModel ist die einzige Stelle, die ihn ändert
 * (Single Source of Truth). Die UI ruft nur Events auf.
 * Löst: Das ViewModel übersteht das Drehen ohne Bundle, die Logik ist ohne UI testbar
 * (siehe `ShoppingListViewModelTest`).
 * Problem: Process Death (System beendet die App im Hintergrund) übersteht es nicht.
 * Dafür gibt es `SavedStateHandle`, nur als Ausblick.
 */
package de.hhn.mplab.statemanagement.stufen

import androidx.lifecycle.ViewModel
import de.hhn.mplab.statemanagement.model.Item
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/** Alles, was der Screen anzeigt, in einem Objekt. */
data class ShoppingListUiState(
    val items: List<Item> = emptyList(),
    val input: String = "",
    val showOnlyOpen: Boolean = false,
) {
    // Abgeleitete Werte: werden aus items berechnet, nicht separat gespeichert.
    val visibleItems: List<Item> get() = if (showOnlyOpen) items.filterNot { it.done } else items
    val openCount: Int get() = items.count { !it.done }
}

class ShoppingListViewModel : ViewModel() {

    // Privat und veränderbar: nur das ViewModel darf schreiben.
    private val _uiState = MutableStateFlow(ShoppingListUiState())

    // Öffentlich und nur lesbar: die UI beobachtet diesen Flow.
    val uiState: StateFlow<ShoppingListUiState> = _uiState.asStateFlow()

    // Zähler für eindeutige IDs.
    private var nextId = 1L

    fun onInputChange(text: String) {
        _uiState.update { it.copy(input = text) }
    }

    fun onAdd() {
        val name = _uiState.value.input.trim()
        if (name.isEmpty()) return // Leere Eingabe ignorieren
        val newItem = Item(id = nextId++, name = name)
        _uiState.update { it.copy(items = it.items + newItem, input = "") }
    }

    fun onToggle(id: Long) {
        _uiState.update { state ->
            state.copy(items = state.items.map { if (it.id == id) it.copy(done = !it.done) else it })
        }
    }

    fun onFilterChange(showOnlyOpen: Boolean) {
        _uiState.update { it.copy(showOnlyOpen = showOnlyOpen) }
    }
}
