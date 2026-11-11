package de.hhn.mplab.trainingstagebuch.submission

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import java.io.IOException
import java.security.GeneralSecurityException
import javax.crypto.AEADBadTagException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/** Was der Balken nach dem Freischalten anzeigt. */
internal data class Receipt(val lastName: String, val matriculation: String, val code: String)

/** Welcher Dialog der Abgabe gerade offen ist. */
internal enum class Overlay { NONE, UNLOCK, ASSIGNMENT }

internal data class SubmissionUiState(
    val receipt: Receipt? = null,
    val overlay: Overlay = Overlay.NONE,
    val isUnlocking: Boolean = false,
    val unlockError: String? = null,
    val assignmentText: String? = null,
    val assignmentError: String? = null,
)

/**
 * Freischalten, Beleg-Code und Prüfauftrag.
 *
 * Unabhängig vom übrigen Zustand der App: Die Freischaltung liegt im [SubmissionStore],
 * der offene Dialog im [SavedStateHandle]. Schlüsselableitung und Entschlüsselung laufen
 * auf [Dispatchers.Default].
 */
internal class SubmissionViewModel(
    application: Application,
    private val savedStateHandle: SavedStateHandle,
) : AndroidViewModel(application) {

    private val store = SubmissionStore(application)

    private val _uiState = MutableStateFlow(initialState())
    val uiState: StateFlow<SubmissionUiState> = _uiState.asStateFlow()

    private var unlockJob: Job? = null
    private var assignmentJob: Job? = null

    init {
        if (_uiState.value.overlay == Overlay.ASSIGNMENT) loadAssignment()
    }

    /** Tipp auf den Balken: ohne Freischaltung zum Freischalten, sonst zum Prüfauftrag. */
    fun onBarClick() {
        if (_uiState.value.receipt == null) {
            showOverlay(Overlay.UNLOCK)
        } else {
            showOverlay(Overlay.ASSIGNMENT)
            loadAssignment()
        }
    }

    fun onChangeData() {
        _uiState.update { it.copy(unlockError = null) }
        showOverlay(Overlay.UNLOCK)
    }

    fun onDismiss() {
        if (_uiState.value.isUnlocking) return
        _uiState.update { it.copy(unlockError = null) }
        showOverlay(Overlay.NONE)
    }

    fun onUnlock(lastName: String, matriculation: String, unlockCode: String) {
        if (unlockJob?.isActive == true) return
        val name = lastName.trim()
        val number = matriculation.trim()
        val code = SubmissionCrypto.normalizeUnlockCode(unlockCode)
        val error = when {
            name.isEmpty() -> "Bitte den Nachnamen eingeben."
            number.length !in 5..9 || number.any { it !in '0'..'9' } -> "Die Matrikelnummer besteht aus 5 bis 9 Ziffern."
            code.isEmpty() -> "Bitte den Freischaltcode eingeben."
            else -> null
        }
        if (error != null) {
            _uiState.update { it.copy(unlockError = error) }
            return
        }

        _uiState.update { it.copy(isUnlocking = true, unlockError = null) }
        unlockJob = viewModelScope.launch {
            val result = withContext(Dispatchers.Default) { unlock(name, number, code) }
            when (result) {
                is UnlockResult.Success -> {
                    store.save(result.stored)
                    _uiState.update {
                        it.copy(
                            receipt = result.stored.toReceipt(),
                            isUnlocking = false,
                            assignmentText = result.text,
                            assignmentError = null,
                        )
                    }
                    showOverlay(Overlay.ASSIGNMENT)
                }
                is UnlockResult.Failure -> _uiState.update {
                    it.copy(isUnlocking = false, unlockError = result.message)
                }
            }
        }
    }

    private fun unlock(lastName: String, matriculation: String, unlockCode: String): UnlockResult =
        try {
            val blob = readAsset()
            val key = SubmissionCrypto.deriveKey(blob, unlockCode)
            val assignment = SubmissionCrypto.decrypt(blob, key)
            val stored = StoredSubmission(
                lastName = lastName,
                matriculation = matriculation,
                receiptCode = SubmissionCrypto.receiptCode(assignment, matriculation),
                key = key,
            )
            UnlockResult.Success(stored, SubmissionCrypto.render(assignment, lastName, matriculation))
        } catch (e: AEADBadTagException) {
            UnlockResult.Failure("Der Freischaltcode stimmt nicht.")
        } catch (e: GeneralSecurityException) {
            UnlockResult.Failure(READ_ERROR)
        } catch (e: IOException) {
            UnlockResult.Failure(READ_ERROR)
        } catch (e: IllegalArgumentException) {
            UnlockResult.Failure(READ_ERROR)
        }

    /** Entschlüsselt den Prüfauftrag mit dem gespeicherten Schlüssel, falls er noch nicht geladen ist. */
    private fun loadAssignment() {
        if (_uiState.value.assignmentText != null || assignmentJob?.isActive == true) return
        val stored = store.load()
        if (stored == null) {
            showOverlay(Overlay.NONE)
            return
        }
        _uiState.update { it.copy(assignmentError = null) }
        assignmentJob = viewModelScope.launch {
            val text = withContext(Dispatchers.Default) {
                try {
                    val assignment = SubmissionCrypto.decrypt(readAsset(), stored.key)
                    SubmissionCrypto.render(assignment, stored.lastName, stored.matriculation)
                } catch (e: GeneralSecurityException) {
                    null
                } catch (e: IOException) {
                    null
                } catch (e: IllegalArgumentException) {
                    null
                }
            }
            _uiState.update {
                if (text != null) {
                    it.copy(assignmentText = text, assignmentError = null)
                } else {
                    it.copy(assignmentError = "Der Prüfauftrag ließ sich nicht öffnen. Bitte über „Daten ändern“ neu freischalten.")
                }
            }
        }
    }

    private fun readAsset(): ByteArray =
        getApplication<Application>().assets.open(ASSET_NAME).use { it.readBytes() }

    private fun showOverlay(overlay: Overlay) {
        savedStateHandle[KEY_OVERLAY] = overlay.name
        _uiState.update { it.copy(overlay = overlay) }
    }

    private fun initialState(): SubmissionUiState {
        val receipt = store.load()?.toReceipt()
        val restored = savedStateHandle.get<String>(KEY_OVERLAY)
            ?.let { name -> Overlay.entries.firstOrNull { it.name == name } }
            ?: Overlay.NONE
        val overlay = if (restored == Overlay.ASSIGNMENT && receipt == null) Overlay.NONE else restored
        return SubmissionUiState(receipt = receipt, overlay = overlay)
    }

    private fun StoredSubmission.toReceipt() = Receipt(lastName, matriculation, receiptCode)

    private sealed interface UnlockResult {
        class Success(val stored: StoredSubmission, val text: String) : UnlockResult
        class Failure(val message: String) : UnlockResult
    }

    private companion object {
        const val ASSET_NAME = "pruefauftrag.bin"
        const val KEY_OVERLAY = "abgabe_dialog"
        const val READ_ERROR = "Der Prüfauftrag ließ sich nicht lesen. Bitte die App neu installieren."
    }
}
