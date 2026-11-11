package de.hhn.mplab.trainingstagebuch.submission

import android.content.Context
import java.util.Base64

/** Angaben, die nach dem Freischalten dauerhaft gespeichert bleiben. */
internal class StoredSubmission(
    val lastName: String,
    val matriculation: String,
    val receiptCode: String,
    val key: ByteArray,
)

/**
 * Speichert die Freischaltung in den `SharedPreferences`.
 *
 * Bewusst außerhalb von Compose und ViewModel: Der Balken soll Drehen, Ansichtswechsel
 * und Neustarts der App unabhängig vom übrigen Zustand der App überstehen.
 */
internal class SubmissionStore(context: Context) {

    private val prefs = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun load(): StoredSubmission? {
        val lastName = prefs.getString(KEY_LAST_NAME, null) ?: return null
        val matriculation = prefs.getString(KEY_MATRICULATION, null) ?: return null
        val receiptCode = prefs.getString(KEY_RECEIPT_CODE, null) ?: return null
        val encodedKey = prefs.getString(KEY_KEY, null) ?: return null
        val key = runCatching { Base64.getDecoder().decode(encodedKey) }.getOrNull() ?: return null
        return StoredSubmission(
            lastName = lastName,
            matriculation = matriculation,
            receiptCode = receiptCode,
            key = key,
        )
    }

    fun save(submission: StoredSubmission) {
        prefs.edit()
            .putString(KEY_LAST_NAME, submission.lastName)
            .putString(KEY_MATRICULATION, submission.matriculation)
            .putString(KEY_RECEIPT_CODE, submission.receiptCode)
            .putString(KEY_KEY, Base64.getEncoder().encodeToString(submission.key))
            .apply()
    }

    private companion object {
        const val PREFS_NAME = "abgabe"
        const val KEY_LAST_NAME = "nachname"
        const val KEY_MATRICULATION = "matrikelnummer"
        const val KEY_RECEIPT_CODE = "beleg_code"
        const val KEY_KEY = "schluessel"
    }
}
