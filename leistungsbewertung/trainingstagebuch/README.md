# Trainingstagebuch

Kleine Jetpack-Compose-App zum Erfassen des heutigen Trainings: Übungen mit Sätzen, Wiederholungen und Gewicht anlegen, abhaken, bearbeiten und löschen. Der Kopfbereich zeigt das Gesamtvolumen der erledigten Übungen.

## Starten

1. Ordner in Android Studio öffnen (*File → Open*).
2. Gradle-Sync abwarten.
3. Run-Konfiguration `app` auf einem Emulator oder Gerät starten (Android 8.0 / API 26 oder neuer).

## Aufbau

```
app/src/main/java/de/hhn/mplab/trainingstagebuch/
├── MainActivity.kt
├── TrainingApp.kt                 Einstieg, Wechsel zwischen Liste und Detailansicht
├── model/                         Exercise, MuscleGroup, Startdaten
└── ui/
    ├── TrainingViewModel.kt       App-State und Aktionen
    ├── TrainingUiState.kt
    ├── Format.kt                  Formatierung und Eingabeprüfung
    ├── components/                Liste, Karte, Dialog, Kopfbereich
    ├── screens/                   Listenansicht, Detail-/Bearbeitungsansicht
    └── theme/
```

Technik: Kotlin, Jetpack Compose (Material 3), ViewModel + StateFlow. Keine Navigation-Library, der Wechsel zwischen den Ansichten läuft über State.
