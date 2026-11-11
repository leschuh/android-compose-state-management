# Beispiel-App „Einkaufsliste“

Android-Studio-Projekt zu den Lerneinheiten „State Management in Jetpack Compose“. Alle Ausbaustufen und der Startcode der Übungen liegen in **einer** App. Der Startbildschirm listet die Screens, die Zurück-Taste führt immer dorthin.

## Öffnen

In Android Studio *File > Open* wählen und **diesen Ordner** öffnen, nicht das ganze Repo. Die Einrichtung steht in der [Anleitung](../ANLEITUNG.md).

## Aufbau

```
app/src/main/java/de/hhn/mplab/statemanagement/
  MainActivity.kt      Startbildschirm und Wechsel zwischen den Screens
  model/Item.kt        ein Eintrag der Liste
  stufen/              die Ausbaustufen der Lerneinheiten
  uebung1/             Startcode von Übung 1
app/src/test/          Unit-Tests
```

## Übungen

- Übung 1: auf dem Startbildschirm „Übung 1 (Start)“, Aufgabenblatt [`uebung-1.md`](../uebungen/uebung-1.md)
- Übung 2: auf dem Startbildschirm „Übung 2“, Aufgabenblatt [`uebung-2.md`](../uebungen/uebung-2.md)

Zurücksetzen auf den Ausgangsstand, wenn ihr mit Git arbeitet: `git restore .`

## Unit-Tests

`app/src/test/.../stufen/ShoppingListViewModelTest.kt` öffnen und auf den grünen Pfeil neben der Klasse klicken.
