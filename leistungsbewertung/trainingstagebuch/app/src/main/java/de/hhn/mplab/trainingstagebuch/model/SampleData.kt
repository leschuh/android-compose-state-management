package de.hhn.mplab.trainingstagebuch.model

/** Startdaten, damit die App beim ersten Öffnen nicht leer ist. */
object SampleData {
    val exercises: List<Exercise> = listOf(
        Exercise(id = 1, name = "Bankdrücken", muscleGroup = MuscleGroup.BRUST, sets = 4, reps = 8, weightKg = 70.0),
        Exercise(id = 2, name = "Kniebeuge", muscleGroup = MuscleGroup.BEINE, sets = 4, reps = 6, weightKg = 100.0),
        Exercise(id = 3, name = "Kreuzheben", muscleGroup = MuscleGroup.RUECKEN, sets = 3, reps = 5, weightKg = 120.0),
        Exercise(id = 4, name = "Schulterdrücken", muscleGroup = MuscleGroup.SCHULTERN, sets = 3, reps = 10, weightKg = 35.0),
        Exercise(id = 5, name = "Klimmzüge", muscleGroup = MuscleGroup.RUECKEN, sets = 3, reps = 8, weightKg = 10.0),
        Exercise(id = 6, name = "Bizepscurls", muscleGroup = MuscleGroup.ARME, sets = 3, reps = 12, weightKg = 14.0),
        Exercise(id = 7, name = "Beinpresse", muscleGroup = MuscleGroup.BEINE, sets = 3, reps = 12, weightKg = 160.0),
    )
}
