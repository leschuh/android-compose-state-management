# Leistungsbewertung: Trainingstagebuch

Mobility Project Lab · App-Labor AI3 · Lerneinheiten „State Management in Jetpack Compose“

| | |
|---|---|
| Bearbeitung | einzeln |
| Abgabe | ZIP-Datei über ILIAS bis **Montag, 23.11.2026, 0:00 Uhr** (Nacht von Sonntag auf Montag) |
| Bewertung | 100 Punkte, Überblick unten |

## Worum es geht

Sie bekommen die App **„Trainingstagebuch“**. Sie läuft und sieht fertig aus, im Code stecken aber mehrere Fehler im Umgang mit State, ganz ähnlich wie in den Lerneinheiten. Für die volle Punktzahl reichen **fünf Fehler**.

Pro Fehler machen Sie drei Dinge:

1. **Screenshot vorher:** den Fehler in der App zeigen.
2. **Beheben:** den Code ändern und direkt über die geänderte Zeile einen kurzen Kommentar schreiben.
3. **Screenshot nachher:** zeigen, dass der Fehler weg ist.

Zum Schluss zeichnen Sie per Hand ein Diagramm, wem welcher State gehört. Dazu kommt ein kurzer Prüfauftrag, der nur in der App steht.

## Die App

Ein Trainingstagebuch für das heutige Training im Gym.

- **Liste:** Übungen mit Sätzen × Wiederholungen · Gewicht. Abhaken (Checkbox), Löschen (Mülleimer), Antippen klappt Details auf.
- **Gesamtvolumen:** Kopfbereich zeigt das Volumen aller erledigten Übungen (Sätze × Wiederholungen × Gewicht) und den Fortschritt.
- **Filter:** „Nur offene“ sowie Filter nach Muskelgruppe (Mehrfachauswahl, keine Auswahl = alle).
- **Neue Übung:** Button „Übung“ öffnet einen Dialog.
- **Detail-/Bearbeitungsansicht:** über „Bearbeiten“ in einer aufgeklappten Karte. Werte ändern und speichern, mit „Vorherige“/„Nächste“ durch die Übungen blättern, Übung löschen.
- **Menü (⋮):** „Neue Trainingseinheit“ setzt alle Haken zurück.

Die App soll sich so verhalten, wie man es von einer gepflegten App aus dem Play Store erwartet: Eingaben gehen nicht verloren, Anzeigen stimmen mit den Daten überein, Aktionen wirken sofort.

## Vorbereitung

1. GitHub-Repo klonen oder als ZIP herunterladen (Einrichtung: `ANLEITUNG.md` im Repo). Ordner `leistungsbewertung/trainingstagebuch/` in Android Studio öffnen (*File → Open*), nicht das ganze Repo.
2. Gradle-Sync abwarten, App auf Emulator oder Gerät starten (minSdk 26).
3. **Automatisches Drehen aktivieren.** Im Emulator drehen Sie mit den Rotations-Buttons in der Seitenleiste oder mit Strg/Cmd + Pfeiltaste.
4. **App freischalten.** Tippen Sie unten in der App auf den Balken und geben Sie Nachname, Matrikelnummer und den Freischaltcode ein. Den Freischaltcode zeigen wir am Ende von Lerneinheit 2 auf der Folie „Rahmen und Abgabe“. Wer an dem Termin nicht da sein kann, meldet sich bei uns. Danach zeigt der Balken Ihren persönlichen **Beleg-Code** (`Nachname · TT-XXXXXX`). Er bleibt gespeichert, auch nach dem Drehen und nach einem Neustart der App.

Der Balken und das Package `submission` gehören nicht zur Aufgabe und enthalten keine Fehler. Ändern Sie dort bitte nichts.

## Aufgaben

### 1. Fehler finden

Probieren Sie die App in Ruhe aus, bevor Sie in den Code schauen. Gute Stellen zum Suchen:

- Drehen, in jeder Ansicht und mit offenem Dialog
- Hinzufügen, Abhaken und Löschen an verschiedenen Stellen der Liste
- Filter zusammen mit anderen Aktionen
- Wechsel zwischen Liste und Detailansicht, Blättern in der Detailansicht

Haben Sie einen Fehler gefunden, machen Sie **gleich einen Screenshot**, bevor Sie etwas ändern. Nehmen Sie den ganzen Bildschirm auf, damit der Balken mit Ihrem Beleg-Code zu sehen ist (Bildschirmtastatur vorher schließen, falls sie ihn verdeckt).

### 2. Fehler beheben und kommentieren

- Ändern Sie nur so viel wie nötig und nur Code, der zu einem gefundenen Fehler gehört. Keine Umbauten der ganzen App.
- Schreiben Sie **direkt über jede geänderte Zeile** einen Kommentar mit einem kurzen Satz: was Sie geändert haben und warum. Gern mit einem Begriff aus den Lerneinheiten, zum Beispiel:

```kotlin
// Fehler 1: rememberSaveable statt remember, damit der Text das Drehen übersteht.
var name by rememberSaveable { mutableStateOf("") }
```

- Machen Sie danach denselben Ablauf noch einmal und einen **Screenshot nachher**, auf dem der Fehler weg ist.

### 3. Diagramm „Wem gehört welcher State?“ (per Hand)

Zeichnen Sie **per Hand** (Papier oder Tablet) zwei Skizzen:

- **Vorher:** Welche State-Werte gibt es und wo liegen sie (ViewModel, welches Composable, `remember` oder `rememberSaveable`)? Wie fließen Daten nach unten und Events nach oben? Markieren Sie, wo die Fehler entstehen.
- **Nachher:** derselbe Ausschnitt nach Ihren Fixes.

Gemeint ist eine einfache Skizze, kein UML. Für die Einkaufsliste aus den Lerneinheiten (Stufe 5) sähe das stark verkürzt so aus:

```
ShoppingListViewModel       State: items, input, showOnlyOpen
        |  uiState (nach unten)
        v
Stufe5Screen                kein eigener State
        |  visibleItems, input, showOnlyOpen (nach unten)
        v
ShoppingListContent         zustandslos
        :  onAdd, onToggle, onFilterChange (Events nach oben, zurück ans ViewModel)
```

Fotografieren oder scannen Sie beide Skizzen und legen Sie sie als PNG, JPG oder PDF in den Ordner `diagramme/`.

## Prüfauftrag aus der App

Ein kleiner Teil der Aufgabe steht nur in der App. Nach dem Freischalten zeigt sie ihn, wenn Sie auf den Balken tippen. Es sind kurze Abläufe, die Sie in Ihrer korrigierten App durchspielen. Tragen Sie die Werte in `abgabe.md` ein und legen Sie die Screenshots dazu bei.

## Nutzung von KI

KI-Werkzeuge (ChatGPT, Copilot, Gemini in Android Studio, Claude …) dürfen Sie verwenden. Geben Sie in `abgabe.md` kurz an, welches Werkzeug Sie wofür benutzt haben.

## Abgabe

Eine ZIP-Datei `Vorname_Nachname_Matrikelnummer.zip` mit:

```
Vorname_Nachname_Matrikelnummer.zip
├── abgabe.md                         ausgefüllte Vorlage (abgabe-vorlage.md)
├── eigenstaendigkeitserklaerung.pdf  ausgefüllt und unterschrieben (Scan oder Foto geht auch)
├── screenshots/                      z. B. fehler1_vorher.png, fehler1_nachher.png, pruefauftrag1.png
├── diagramme/                        vorher.jpg, nachher.jpg
└── trainingstagebuch/                das korrigierte Projekt mit Ihren Kommentaren
```

- Die unterschriebene Eigenständigkeitserklärung gehört dazu. Die Vorlage liegt neben dieser Aufgabe.
- Das Projekt sollte sich in Android Studio öffnen, bauen und starten lassen.
- Bitte **ohne** die Ordner `build/`, `app/build/`, `.gradle/`, `.idea/` und ohne `local.properties` packen.
- Laden Sie Ihre Lösung bitte nicht in ein öffentliches Repo hoch.

## Bewertung (Überblick)

| Teil | Punkte |
|---|---|
| Fehler: Screenshots vorher und nachher, Fix, Kommentar (5 Fehler à 12 P) | 60 |
| Diagramm vorher/nachher, per Hand | 20 |
| App läuft, Prüfauftrag, Fixes klein und sauber, Abgabe vollständig, KI-Angabe | 20 |
| **Summe** | **100** |

Bei Fragen melden Sie sich gern bei uns. Viel Erfolg!
