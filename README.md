<p align="center"><img src=".github/banner.svg" alt="State Management in Jetpack Compose" width="100%"></p>

# State Management in Jetpack Compose

Material zu den zwei Lerneinheiten im App-Labor AI3, Hochschule Heilbronn, WS 2026/27.
Dienstag, 17.11.2026, 14:00–17:15 Uhr, Raum A411.

Team: Douglas Flubacher, Yakob Lahdo, Lennard Schuh

## Bitte vor dem Termin erledigen

Am 17.11. programmiert ihr selbst. Erledigt deshalb diese drei Schritte **zu Hause**, denn beim ersten Start wird viel heruntergeladen:

1. Android Studio und Emulator einrichten: [ANLEITUNG.md](ANLEITUNG.md)
2. Dieses Repo klonen oder als ZIP herunterladen
3. `beispiel-app/` öffnen und einmal starten

Klappt etwas nicht, schaut in die [häufigen Probleme](ANLEITUNG.md#häufige-probleme). Hilft das nicht, meldet euch vor dem Termin bei uns.

## Schnellstart

Für alle, die Android Studio schon haben. Die ausführliche Variante steht in der [Anleitung](ANLEITUNG.md).

**Das braucht ihr**

| | Version |
|---|---|
| Android Studio | Quail 4 (2026.1.4) oder neuer |
| SDK Platform (SDK Manager) | **API 37** (zum Bauen) und **API 36** |
| Emulator | System-Image **API 36**, Google APIs. Alternativ ein Handy ab Android 8.0 (API 26) |
| Java, Gradle | nichts installieren, Android Studio und der Gradle-Wrapper bringen alles mit |

**1. Klonen**, in einen kurzen Pfad ohne Leerzeichen, Umlaute und OneDrive. Unter Windows in der PowerShell:

```powershell
mkdir C:\dev
cd C:\dev
git clone https://github.com/leschuh/android-compose-state-management.git
```

Auf dem Mac und unter Linux im Terminal `mkdir ~/dev && cd ~/dev`, danach derselbe `git clone`.

**2. Öffnen:** In Android Studio *File > Open* wählen und den Ordner **`android-compose-state-management/beispiel-app`** öffnen, **nicht** das ganze Repo.

**3. Gradle-Sync abwarten:** Das dauert beim ersten Mal bis zu 10 Minuten, den Fortschritt seht ihr unten rechts.

**4. Starten:** Oben den Emulator wählen und auf **Run ▶** klicken. Ihr seht den Startbildschirm mit den Stufen.

Wenn ihr die Kommandozeile lieber mögt, könnt ihr optional so prüfen, ob alles baut:

```bash
cd android-compose-state-management/beispiel-app
./gradlew assembleDebug        # bauen; unter Windows: .\gradlew.bat assembleDebug
./gradlew testDebugUnitTest    # Unit-Tests; unter Windows: .\gradlew.bat testDebugUnitTest
```

**Änderungen verwerfen:** `git restore .` setzt alles auf den Ausgangsstand zurück.

Für die Leistungsbewertung öffnet ihr später genauso `leistungsbewertung/trainingstagebuch`.

## Was liegt wo?

| Pfad | Inhalt | Wann |
|---|---|---|
| [`ANLEITUNG.md`](ANLEITUNG.md) | Android Studio, Emulator, Git, Compose-Grundlagen | vor dem Termin |
| [`beispiel-app/`](beispiel-app/) | App „Einkaufsliste“ mit allen Stufen und Startcode | beide Lerneinheiten |
| [`uebungen/uebung-1.md`](uebungen/uebung-1.md) | Übung 1 | Lerneinheit 1 |
| [`uebungen/uebung-2.md`](uebungen/uebung-2.md) | Übung 2 | Lerneinheit 2 |
| [`leistungsbewertung/aufgabe.md`](leistungsbewertung/aufgabe.md) | Aufgabenstellung der Leistungsbewertung | nach Lerneinheit 2 |
| [`leistungsbewertung/abgabe-vorlage.md`](leistungsbewertung/abgabe-vorlage.md) | Vorlage für eure Abgabe | Leistungsbewertung |
| [`leistungsbewertung/eigenstaendigkeitserklaerung.pdf`](leistungsbewertung/eigenstaendigkeitserklaerung.pdf) | Erklärung zum Ausfüllen und Unterschreiben | Leistungsbewertung |
| [`leistungsbewertung/trainingstagebuch/`](leistungsbewertung/trainingstagebuch/) | App „Trainingstagebuch“ | Leistungsbewertung |

Die Folien findet ihr in ILIAS.

## Leistungsbewertung in Kürze

- Einzelarbeit, ca. 4 Stunden, 100 Punkte. Alle Details stehen in [`aufgabe.md`](leistungsbewertung/aufgabe.md).
- Die Frist steht in ILIAS.
- Abgabe als ZIP-Datei **in ILIAS**, nicht hier. Dazu gehört die unterschriebene Eigenständigkeitserklärung.
- KI-Werkzeuge sind erlaubt, müssen aber angegeben werden.
- Bitte ladet eure Lösung nicht in ein öffentliches Repo hoch.

## Änderungen holen

Korrigieren wir etwas, holt ihr es so:

```bash
git pull
```

Wer schon eigene Änderungen hat, committet sie vorher (siehe [Git-Grundlagen](ANLEITUNG.md#6-git-grundlagen)).
