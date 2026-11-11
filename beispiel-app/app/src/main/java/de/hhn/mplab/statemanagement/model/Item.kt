package de.hhn.mplab.statemanagement.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Fachmodell der Einkaufsliste: ein Eintrag.
 *
 * Unveränderlich (nur `val`): Änderungen erzeugen mit `copy(...)` ein neues Objekt.
 * `@Parcelize` macht den Eintrag Parcelable, damit `rememberSaveable` ihn im Bundle
 * speichern kann (ab Stufe 3).
 */
@Parcelize
data class Item(val id: Long, val name: String, val done: Boolean = false) : Parcelable
