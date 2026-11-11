# Anleitung: Android Studio einrichten

Plant dafür etwa eine Stunde ein, je nach Internetverbindung. Insgesamt werden mehrere Gigabyte heruntergeladen. Bitte macht das zu Hause und **vor dem 17.11.**

## Was ihr braucht

| | Mindestens |
|---|---|
| Arbeitsspeicher | 16 GB für Android Studio mit Emulator, 8 GB nur für Android Studio |
| Freier Speicher | 16 GB für Android Studio mit Emulator, besser 32 GB auf einer SSD |
| Prozessor | Windows/Linux: Intel Core 8. Generation oder AMD Ryzen, Virtualisierung im BIOS aktiv. Mac: Apple M1 oder Intel Core 6. Generation |
| Android Studio | **Quail 4 (2026.1.4) oder neuer**, aktuell ist Rabbit 2 |

Quelle: [Systemanforderungen von Android Studio](https://developer.android.com/studio/install). Reicht euer Rechner nicht für den Emulator, nehmt ein eigenes Android-Handy (siehe Schritt 3) oder sprecht uns an.

## 1. Android Studio installieren

1. Von [developer.android.com/studio](https://developer.android.com/studio) die aktuelle Version herunterladen.
2. Installieren und starten, im Einrichtungsassistenten die Standardeinstellungen übernehmen und die Lizenzen akzeptieren.

Ein passendes Java bringt Android Studio mit, ihr müsst nichts extra installieren.

Android Studio ist schon installiert? Prüft unter *Help > About* (Mac: *Android Studio > About*) die Version. Ältere Versionen als Quail 4 können die Projekte nicht öffnen, also bitte aktualisieren.

## 2. Android SDK ergänzen

Öffnet *Tools > SDK Manager* (auf dem Startbildschirm: *More Actions > SDK Manager*).

- Reiter **SDK Platforms**: **API 37** und **API 36** anhaken.
- Reiter **SDK Tools**: **Android SDK Build-Tools**, **Android Emulator** und **Android SDK Platform-Tools** anhaken.
- Mit *OK* installieren.

## 3. Emulator anlegen

1. *Tools > Device Manager* öffnen (auf dem Startbildschirm: *More Actions > Virtual Device Manager*), dann auf **+** und *Create Virtual Device*.
2. Ein Telefon wählen, zum Beispiel *Medium Phone* oder ein Pixel.
3. System-Image **API 36** mit **Google APIs** auswählen und herunterladen. Auf Windows und Intel-Macs x86_64, auf Macs mit Apple-Chip arm64.
4. Fertigstellen und den Emulator starten (Play-Symbol).
5. Im Emulator von oben wischen und in den Schnelleinstellungen **Auto-rotate** einschalten. Das braucht ihr in jeder Übung.

**Windows: Der Emulator startet nicht oder ist sehr langsam?** Sucht im Startmenü nach *Windows-Features aktivieren oder deaktivieren*, setzt den Haken bei **Windows-Hypervisor-Plattform** (*Windows Hypervisor Platform*), bestätigt und startet neu. Virtualisierung muss außerdem im BIOS aktiv sein (Intel VT-x bzw. AMD SVM). Details: [Hardware-Beschleunigung](https://developer.android.com/studio/run/emulator-acceleration).

**Alternative: eigenes Android-Handy** (ab Android 8.0). Unter *Einstellungen > Über das Telefon* siebenmal auf die *Build-Nummer* tippen, dann unter *Entwickleroptionen* **USB-Debugging** einschalten und das Handy per Kabel anschließen. Das Handy erscheint in Android Studio oben in der Geräteauswahl.

## 4. Projekt holen

Legt das Projekt in einen **kurzen Pfad ohne Leerzeichen und Umlaute**, unter Windows zum Beispiel `C:\dev\`. Nicht in OneDrive oder einen anderen synchronisierten Ordner legen, das führt zu Build-Fehlern.

**Mit Git (empfohlen).** [Git installieren](https://git-scm.com/downloads), dann:

```bash
cd C:\dev
git clone https://github.com/leschuh/android-compose-state-management.git
```

**Ohne Git.** Oben auf *Code* und dann *Download ZIP* klicken, entpacken.

## 5. App öffnen und starten

1. In Android Studio *File > Open* wählen und den Ordner **`beispiel-app/`** öffnen, **nicht** das ganze Repo. Dasselbe gilt später für `leistungsbewertung/trainingstagebuch/`.
2. Warten, bis der **Gradle-Sync** fertig ist. Beim ersten Mal kann das 10 Minuten und länger dauern. Unten rechts seht ihr den Fortschritt.
3. Oben den Emulator oder das Handy auswählen und auf **Run** (grünes Dreieck) klicken.

Fertig ist alles, wenn ihr in der App den Startbildschirm mit den Stufen seht und das Gerät drehen könnt.

Im Emulator dreht ihr mit `Strg + ←/→` (Windows/Linux), `Cmd + ←/→` (Mac) oder den Dreh-Knöpfen in der Leiste neben dem Emulator. Das Kamerasymbol in derselben Leiste macht einen Screenshot, ihr findet ihn standardmäßig auf dem Desktop.

**Trainingstagebuch: Balken unten in der App.** Im Trainingstagebuch seht ihr ganz unten einen schmalen Balken. Tippt darauf und gebt Nachname, Matrikelnummer und den Freischaltcode ein, den wir nach Lerneinheit 2 in ILIAS stellen. Danach zeigt der Balken euren persönlichen Beleg-Code, der auf jedem Screenshot der Leistungsbewertung zu sehen sein muss, und ein Tipp darauf öffnet den Prüfauftrag. Die Angaben bleiben gespeichert, ihr müsst sie nur einmal eingeben.

## 6. Git-Grundlagen

Für die Übungen reicht das hier. In der Leistungsbewertung liefert Git euch die Diffs fast von selbst.

```bash
git status                # was hat sich geändert?
git diff                  # Änderungen Zeile für Zeile anzeigen
git add -A                # alle Änderungen vormerken
git commit -m "Fix 1"     # Stand speichern
git show                  # letzten Commit als Diff anzeigen
git restore .             # alle ungespeicherten Änderungen verwerfen
git pull                  # Korrekturen von uns holen
```

Am besten committet ihr in der Leistungsbewertung nach jedem Fix einzeln, dann habt ihr pro Fehler einen eigenen Diff.

## 7. Compose-Grundlagen

Die Lerneinheiten arbeiten mit Jetpack Compose, das ihr aus dem 1. und 2. Semester kennt. Zur Auffrischung reichen diese wenigen Begriffe:

```kotlin
@Composable
fun Begruessung(name: String) {
    Column {
        Text("Hallo $name")
        Button(onClick = { /* Aktion */ }) {
            Text("Klick mich")
        }
    }
}
```

- `@Composable` markiert eine Funktion, die Oberfläche beschreibt.
- `Column` ordnet untereinander an, `Row` nebeneinander.
- `Text`, `Button` und `Checkbox` sind fertige Bausteine.
- `Modifier` steuert Größe, Abstände und Ähnliches.
- Die App startet über `setContent { … }` in der `MainActivity`.

Wer mehr sehen möchte: das [offizielle Compose-Tutorial](https://developer.android.com/develop/ui/compose/tutorial). Was **State** ist und wie er funktioniert, lernt ihr bei uns, das müsst ihr nicht vorbereiten.

## Häufige Probleme

| Meldung oder Symptom | Lösung |
|---|---|
| Hinweis auf eine **inkompatible Version des Android Gradle Plugin** | Android Studio aktualisieren, mindestens Quail 4 (2026.1.4) |
| **Failed to find target** oder Hinweis auf fehlendes SDK für `android-37` | Im SDK Manager API 37 installieren (Schritt 2), dann *File > Sync Project with Gradle Files* |
| **SDK location not found** | Projekt über Android Studio öffnen, das legt `local.properties` selbst an |
| Es gibt kein Modul `app`, der Run-Knopf ist grau | Ihr habt das ganze Repo geöffnet. *File > Open* und `beispiel-app/` wählen |
| **Filename too long** oder seltsame Pfadfehler | Projekt nach `C:\dev\` verschieben, weg von OneDrive, Leerzeichen und Umlauten |
| Sync bricht mit Netzwerkfehler ab | Noch einmal synchronisieren, WLAN wechseln oder VPN ausschalten |
| Emulator startet nicht | Hinweise in Schritt 3, sonst das eigene Handy nehmen |
| Nach `git pull` gibt es Konflikte | Eigene Änderungen vorher committen. Im Zweifel den Ordner neu klonen und die Dateien übertragen |
