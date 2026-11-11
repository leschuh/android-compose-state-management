package de.hhn.mplab.statemanagement.stufen

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Unit-Tests für die Logik aus Stufe 5.
 * Kein Emulator, keine UI: Das ViewModel ist eine normale Kotlin-Klasse.
 */
class ShoppingListViewModelTest {

    private lateinit var viewModel: ShoppingListViewModel

    @Before
    fun setUp() {
        viewModel = ShoppingListViewModel()
    }

    @Test
    fun `onAdd fügt Eintrag hinzu und leert die Eingabe`() {
        viewModel.onInputChange("Milch")
        viewModel.onAdd()

        val state = viewModel.uiState.value
        assertEquals(1, state.items.size)
        assertEquals("Milch", state.items[0].name)
        assertFalse(state.items[0].done)
        assertEquals("", state.input)
    }

    @Test
    fun `onAdd vergibt eindeutige IDs`() {
        viewModel.onInputChange("Milch")
        viewModel.onAdd()
        viewModel.onInputChange("Brot")
        viewModel.onAdd()

        val ids = viewModel.uiState.value.items.map { it.id }
        assertEquals(2, ids.distinct().size)
    }

    @Test
    fun `leere Eingabe wird ignoriert`() {
        viewModel.onInputChange("   ")
        viewModel.onAdd()

        assertTrue(viewModel.uiState.value.items.isEmpty())
    }

    @Test
    fun `onToggle hakt genau einen Eintrag ab und wieder auf`() {
        viewModel.onInputChange("Milch")
        viewModel.onAdd()
        viewModel.onInputChange("Brot")
        viewModel.onAdd()
        val milchId = viewModel.uiState.value.items[0].id

        viewModel.onToggle(milchId)
        assertTrue(viewModel.uiState.value.items[0].done)
        assertFalse(viewModel.uiState.value.items[1].done)

        viewModel.onToggle(milchId)
        assertFalse(viewModel.uiState.value.items[0].done)
    }

    @Test
    fun `Filter zeigt nur offene Einträge`() {
        viewModel.onInputChange("Milch")
        viewModel.onAdd()
        viewModel.onInputChange("Brot")
        viewModel.onAdd()
        viewModel.onToggle(viewModel.uiState.value.items[0].id)

        viewModel.onFilterChange(true)
        val filtered = viewModel.uiState.value
        assertTrue(filtered.showOnlyOpen)
        assertEquals(listOf("Brot"), filtered.visibleItems.map { it.name })
        assertEquals(1, filtered.openCount)

        viewModel.onFilterChange(false)
        assertEquals(2, viewModel.uiState.value.visibleItems.size)
    }
}
