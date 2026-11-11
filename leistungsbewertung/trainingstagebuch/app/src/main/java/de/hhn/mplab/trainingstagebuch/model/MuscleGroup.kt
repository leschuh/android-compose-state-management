package de.hhn.mplab.trainingstagebuch.model

/** Grobe Einteilung der Übungen, wird für den Filter verwendet. */
enum class MuscleGroup(val label: String) {
    BRUST("Brust"),
    RUECKEN("Rücken"),
    BEINE("Beine"),
    SCHULTERN("Schultern"),
    ARME("Arme"),
}
