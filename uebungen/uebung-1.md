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

Ein Build dauert je nach Laptop eine halbe bis eine Minute. Deshalb erst lesen und alle Fehler beheben, dann **einmal** bauen.

### Schritt 1: Lesen und vorhersagen (ca. 4 Min)

Lest den Code, **ohne** die App zu bedienen. Schreibt auf:

1. Was passiert, wenn ihr auf „Artikel zur Liste hinzufügen“ tippt?
2. Was passiert mit Zähler, Notiz und Liste, wenn ihr das Gerät dreht?

### Schritt 2: In der App prüfen (ca. 3 Min)

Startet die App (sie ist schon gebaut, wenn ihr sie vorher einmal gestartet habt) und probiert beides aus. Wo lagt ihr richtig, wo nicht?

### Schritt 3: Alle drei Fehler beheben (ca. 7 Min)

- Die Liste aktualisiert sich nicht, wenn ein Artikel hinzugefügt oder abgehakt wird. Erklärt in einem Satz, warum, und ändert den Code so, dass jede Änderung sofort sichtbar ist.
- Zähler und Notiz gehen beim Drehen verloren. Sorgt dafür, dass beide das Drehen überstehen.
- Übersteht danach auch die Liste das Drehen? Falls nicht: beheben.

### Schritt 4: Einmal bauen und testen (ca. 3 Min)

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
- Baut es bei euch nicht? Setzt euch zu jemandem, bei dem es läuft, und macht Schritt 1 und 3 auf Papier.
