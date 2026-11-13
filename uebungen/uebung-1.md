# Übung 1: Drei Fehler in einem kleinen Screen

**Lerneinheit 1 · Präsenz · 20 Minuten · allein oder zu zweit**

## Lernziele

- Lernziel 1: erklären, wann eine Änderung eine Recomposition auslöst (beobachtbarer vs. nicht beobachtbarer State).
- Lernziel 2: `remember`, `rememberSaveable` und `mutableStateOf` gezielt einsetzen und begründen, welcher State den Neuaufbau der Activity übersteht.

## Ausgangslage

Öffnet die Beispiel-App in Android Studio und startet sie im Emulator. Auf dem Startbildschirm: **Übung 1 (Start)**.

Der Screen hat einen Zähler „Gekaufte Artikel“ mit Button, ein Notizfeld und eine Artikelliste mit Checkboxen. Er kompiliert und startet, enthält aber **genau drei State-Fehler**. Alle drei Arten habt ihr in der Lerneinheit gesehen.

Der Code liegt in `app/src/main/java/de/hhn/mplab/statemanagement/uebung1/Uebung1Screen.kt`.

## Ablauf

Ein Build dauert je nach Laptop eine halbe bis eine Minute. Deshalb erst alle Fehler beheben, dann **einmal** bauen.

### Schritt 1: Fehler suchen (ca. 3 Min)

Öffnet `Uebung1Screen.kt` und vergleicht den Code mit Stufe 1 (`Stufe1LokalerState.kt`) und Stufe 3 (`Stufe3Saveable.kt`) der Beispiel-App.

### Schritt 2: Alle drei Fehler beheben (ca. 10 Min)

- Die Liste soll sich sofort aktualisieren, wenn ein Artikel hinzugefügt oder abgehakt wird.
- Zähler und Notiz sollen das Drehen überstehen.
- Auch die Liste soll das Drehen überstehen.

### Schritt 3: Einmal bauen und testen (ca. 4 Min)

Startet die App neu und testet: Artikel hinzufügen, einen abhaken, drehen, Home drücken und die App wieder öffnen. Alles muss erhalten bleiben.

Danach lösen wir gemeinsam auf (ca. 3 Min).

### Bonus: Filter „Nur offene anzeigen“

Ergänzt eine Checkbox „Nur offene anzeigen“. Ist sie aktiv, werden abgehakte Artikel ausgeblendet. Auch der Filter soll das Drehen überstehen.

## Hinweise

- Drehen im Emulator: Auto-Rotate in den Schnelleinstellungen aktivieren, dann `Strg + ←/→` (Windows/Linux) bzw. `Cmd + ←/→` (macOS) oder die Dreh-Buttons in der Emulator-Leiste.
- Für die Liste gibt es zwei Wege: eine unveränderliche Liste in `mutableStateOf` (neue Liste zuweisen statt `add()`) oder `mutableStateListOf`. Beide lassen sich mit `rememberSaveable` sichern.
- Spickzettel: Stufe 1 und Stufe 3 der Beispiel-App.
- `Item` ist schon `@Parcelize`.
- Ein abgeleiteter Wert (z. B. die gefilterte Liste) braucht keinen eigenen State.
- Baut es bei euch nicht? Setzt euch zu jemandem, bei dem es läuft.
