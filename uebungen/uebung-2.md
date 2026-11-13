# Übung 2: Löschen und „x offen“ im ViewModel

**Lerneinheit 2 · Präsenz · 20 Minuten · allein oder zu zweit**

## Lernziele

- Lernziel 3: State Hoisting und Unidirectional Data Flow anwenden. Ein zustandsloses Composable um ein Event erweitern.
- Lernziel 4: Das ViewModel mit `StateFlow` als Single Source of Truth nutzen. Abgeleitete Werte statt doppeltem State.

## Ausgangslage

Ausgangspunkt ist **Stufe 5** der Beispiel-App (auf dem Startbildschirm: **Übung 2**). Ihr arbeitet direkt in diesen Dateien unter `app/src/main/java/de/hhn/mplab/statemanagement/stufen/`:

- `ShoppingListViewModel.kt`: ViewModel und `ShoppingListUiState`
- `Stufe4Hoisting.kt`: zustandsloses `ShoppingListContent` (plus `Stufe4Screen` und Preview)
- `Stufe5ViewModel.kt`: `Stufe5Screen`, verbindet ViewModel und `ShoppingListContent`

## Aufgaben

### Aufgabe 1: `onDelete(id)` im ViewModel (ca. 4 Min)

Ergänzt im `ShoppingListViewModel` eine Funktion `onDelete(id: Long)`, die den Eintrag mit dieser ID entfernt. Änderungen am Zustand nur über `_uiState.update { it.copy(...) }`.

### Aufgabe 2: Löschen-Button in `ShoppingListContent` (ca. 7 Min, inklusive Bauen)

Jede Zeile bekommt einen Button „Löschen“.

- `ShoppingListContent` **bleibt zustandslos**: kein `remember`, kein ViewModel darin. Das Löschen wird als Event nach oben gemeldet.
- Verbindet das Event in `Stufe5Screen` mit dem ViewModel.
- Der Compiler zeigt euch, welche Aufrufer ihr noch anpassen müsst (`Stufe4Screen`, Preview). In `Stufe4Screen` reicht `onDelete = {}`, getestet wird unter „Übung 2“.
- Jetzt einmal bauen und unter „Übung 2“ testen: drehen, Filter an und aus, einen erledigten und einen offenen Eintrag löschen. Ein Build dauert eine halbe bis eine Minute, darum erst nach dieser Aufgabe.

### Aufgabe 3: Anzeige „x offen“ (ca. 6 Min)

Über der Liste soll stehen, wie viele Einträge noch offen sind, z. B. „2 offen“.

- Nutzt `openCount` aus `ShoppingListUiState` und reicht ihn als neuen Parameter an `ShoppingListContent` weiter. In `Stufe4Screen` und in der Preview braucht ihr dann ebenfalls einen Wert, der Compiler zeigt euch die Stellen.
- **Kein** zusätzliches State-Feld, kein eigener Zähler, der beim Hinzufügen/Abhaken/Löschen mitgezählt wird.

### Aufgabe 4 (Bonus): Unit-Test für `onDelete`

Ergänzt in `app/src/test/.../stufen/ShoppingListViewModelTest.kt` einen Test, der prüft, dass `onDelete` genau den richtigen Eintrag entfernt. Ausführen: grüner Pfeil neben der Testklasse.

Danach lösen wir gemeinsam auf (ca. 3 Min).

## Hinweise

- State fließt nach unten (Parameter), Events fließen nach oben (Lambdas).
- `filterNot { it.id == id }` liefert eine neue Liste ohne den Eintrag.
- Baut es bei euch nicht? Setzt euch zu jemandem, bei dem es läuft.
- Zurücksetzen auf den Ausgangsstand: `git checkout -- beispiel-app/app/src` (sofern ihr mit Git arbeitet).
