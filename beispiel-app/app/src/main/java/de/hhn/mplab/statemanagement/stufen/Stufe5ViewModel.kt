/**
 * Stufe 5: Screen mit ViewModel.
 *
 * Zeigt: Der Screen hält keinen eigenen State mehr. Er sammelt `uiState` aus dem
 * `ShoppingListViewModel` und gibt ihn an `ShoppingListContent` aus Stufe 4 weiter.
 * Events gehen direkt ans ViewModel.
 * Löst: Single Source of Truth, State überlebt das Drehen ohne Parcelable-Zwang in der UI.
 * Problem: siehe `ShoppingListViewModel` (Process Death nur als Ausblick).
 */
package de.hhn.mplab.statemanagement.stufen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun Stufe5Screen(
    modifier: Modifier = Modifier,
    viewModel: ShoppingListViewModel = viewModel(),
) {
    // StateFlow als Compose-State beobachten. Neuer Wert im Flow -> Recomposition.
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    ShoppingListContent(
        items = state.visibleItems,
        input = state.input,
        onInputChange = { viewModel.onInputChange(it) },
        onAdd = { viewModel.onAdd() },
        onToggle = { viewModel.onToggle(it) },
        showOnlyOpen = state.showOnlyOpen,
        onFilterChange = { viewModel.onFilterChange(it) },
        modifier = modifier,
    )
}
