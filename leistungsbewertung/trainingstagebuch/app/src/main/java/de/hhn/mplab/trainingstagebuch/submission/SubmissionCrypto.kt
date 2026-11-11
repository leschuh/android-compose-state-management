package de.hhn.mplab.trainingstagebuch.submission

import java.nio.ByteBuffer
import java.security.GeneralSecurityException
import java.util.Base64
import java.util.Locale
import javax.crypto.Cipher
import javax.crypto.Mac
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

/**
 * Entschlüsselt den Prüfauftrag und berechnet daraus die persönlichen Werte.
 *
 * Aufbau der Datei `assets/pruefauftrag.bin`:
 * `"TTPA"` | Version (1 Byte) | Iterationen (4 Byte, Big Endian) | Salz (16 Byte) | IV (12 Byte) | Chiffretext mit Tag.
 * Verschlüsselt ist mit AES-256-GCM, der Schlüssel entsteht per PBKDF2 (HMAC-SHA256) aus dem Freischaltcode.
 * Der Kopf bis einschließlich IV ist als zusätzliche authentifizierte Daten an den Chiffretext gebunden.
 *
 * Alle Funktionen sind reine JVM-Funktionen ohne Android-Bezug. [deriveKey] ist bewusst langsam
 * und darf nicht auf dem Main-Thread laufen.
 */
internal object SubmissionCrypto {

    private val MAGIC = "TTPA".toByteArray(Charsets.US_ASCII)
    private const val VERSION: Byte = 1
    private const val SALT_SIZE = 16
    private const val IV_SIZE = 12
    private const val HEADER_SIZE = 4 + 1 + 4 + SALT_SIZE + IV_SIZE
    private const val KEY_BITS = 256
    private const val TAG_BITS = 128
    private const val MAX_ITERATIONS = 5_000_000

    private const val PAYLOAD_HEADER = "TT-PRUEFAUFTRAG 1"
    private const val SECRET_PREFIX = "geheimnis:"
    private const val SEPARATOR = "\n---\n"

    /** Alphabet ohne leicht verwechselbare Zeichen (kein I, L, O, U). */
    private const val CODE_ALPHABET = "0123456789ABCDEFGHJKMNPQRSTVWXYZ"
    private const val CODE_LENGTH = 6

    private val PLACEHOLDER = Regex("""\{\{([a-z0-9_]+)(?::([^}]*))?\}\}""")
    private val RANGE = Regex("""(\d+)-(\d+)""")

    /** Großbuchstaben und Ziffern, alles andere (Leerzeichen, Bindestriche) fällt weg. */
    fun normalizeUnlockCode(input: String): String =
        input.uppercase(Locale.ROOT).filter { it in 'A'..'Z' || it in '0'..'9' }

    fun normalizeMatriculation(input: String): String = input.filter { it in '0'..'9' }

    /** Leitet den AES-Schlüssel aus dem Freischaltcode ab. Salz und Iterationen stehen im Kopf der Datei. */
    fun deriveKey(blob: ByteArray, unlockCode: String): ByteArray {
        val header = readHeader(blob)
        val password = normalizeUnlockCode(unlockCode).toCharArray()
        require(password.isNotEmpty()) { "Freischaltcode fehlt" }
        val spec = PBEKeySpec(password, header.salt, header.iterations, KEY_BITS)
        try {
            return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).encoded
        } finally {
            spec.clearPassword()
            password.fill('\u0000')
        }
    }

    /**
     * Entschlüsselt die Datei.
     *
     * @throws javax.crypto.AEADBadTagException wenn der Schlüssel nicht zum Freischaltcode passt
     */
    fun decrypt(blob: ByteArray, key: ByteArray): Assignment {
        readHeader(blob)
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(
            Cipher.DECRYPT_MODE,
            SecretKeySpec(key, "AES"),
            GCMParameterSpec(TAG_BITS, blob, HEADER_SIZE - IV_SIZE, IV_SIZE),
        )
        cipher.updateAAD(blob, 0, HEADER_SIZE)
        val plain = cipher.doFinal(blob, HEADER_SIZE, blob.size - HEADER_SIZE).toString(Charsets.UTF_8)
        return parsePayload(plain)
    }

    /** Persönlicher Beleg-Code, z. B. `TT-7K3Q9M`. */
    fun receiptCode(assignment: Assignment, matriculation: String): String {
        val digest = hmac(assignment.secret, "beleg:$matriculation")
        // Die ersten 30 Bit ergeben sechs Zeichen zu je 5 Bit.
        val bits = ByteBuffer.wrap(digest, 0, 4).int ushr 2
        val code = (CODE_LENGTH - 1 downTo 0).map { i -> CODE_ALPHABET[(bits ushr (5 * i)) and 0x1F] }
        return "TT-" + code.joinToString("")
    }

    /**
     * Setzt die persönlichen Werte in den Text ein.
     *
     * Platzhalter: `{{name:2-5}}` wählt eine Zahl aus dem Bereich, `{{name:A|B|C}}` eine der Möglichkeiten.
     * Die Wahl hängt nur vom Geheimnis, dem Namen des Platzhalters und der Matrikelnummer ab.
     * `{{nachname}}`, `{{matrikel}}` und `{{beleg}}` werden direkt ersetzt.
     */
    fun render(assignment: Assignment, lastName: String, matriculation: String): String =
        PLACEHOLDER.replace(assignment.template) { match ->
            val name = match.groupValues[1]
            val spec = match.groups[2]?.value
            when {
                spec != null -> choose(assignment.secret, name, spec, matriculation)
                name == "nachname" -> lastName
                name == "matrikel" -> matriculation
                name == "beleg" -> receiptCode(assignment, matriculation)
                else -> match.value
            }
        }

    private fun choose(secret: ByteArray, name: String, spec: String, matriculation: String): String {
        val digest = hmac(secret, "wert:$name:$matriculation")
        val value = ByteBuffer.wrap(digest, 0, 4).int.toLong() and 0xFFFF_FFFFL
        val range = RANGE.matchEntire(spec.trim())
        if (range != null) {
            val min = range.groupValues[1].toLong()
            val max = range.groupValues[2].toLong()
            return (min + value % (max - min + 1)).toString()
        }
        val options = spec.split('|').map { it.trim() }
        return options[(value % options.size).toInt()]
    }

    private fun hmac(secret: ByteArray, message: String): ByteArray {
        val mac = Mac.getInstance("HmacSHA256")
        mac.init(SecretKeySpec(secret, "HmacSHA256"))
        return mac.doFinal(message.toByteArray(Charsets.UTF_8))
    }

    private fun parsePayload(plain: String): Assignment {
        val text = plain.replace("\r\n", "\n")
        val split = text.indexOf(SEPARATOR)
        if (split < 0) throw GeneralSecurityException("Inhalt unvollständig")
        val head = text.substring(0, split).lines()
        if (head.firstOrNull() != PAYLOAD_HEADER) throw GeneralSecurityException("Unbekanntes Format")
        val secretLine = head.firstOrNull { it.startsWith(SECRET_PREFIX) }
            ?: throw GeneralSecurityException("Geheimnis fehlt")
        val secret = Base64.getDecoder().decode(secretLine.removePrefix(SECRET_PREFIX).trim())
        return Assignment(secret = secret, template = text.substring(split + SEPARATOR.length).trim())
    }

    private fun readHeader(blob: ByteArray): Header {
        if (blob.size <= HEADER_SIZE || !blob.copyOfRange(0, MAGIC.size).contentEquals(MAGIC)) {
            throw GeneralSecurityException("Keine Prüfauftragsdatei")
        }
        val buffer = ByteBuffer.wrap(blob, MAGIC.size, HEADER_SIZE - MAGIC.size)
        if (buffer.get() != VERSION) throw GeneralSecurityException("Unbekannte Version")
        val iterations = buffer.int
        if (iterations !in 1..MAX_ITERATIONS) throw GeneralSecurityException("Ungültiger Kopf")
        val salt = ByteArray(SALT_SIZE).also { buffer.get(it) }
        return Header(iterations, salt)
    }

    private class Header(val iterations: Int, val salt: ByteArray)
}

/** Entschlüsselter Inhalt: Geheimnis für den Beleg-Code und Text mit Platzhaltern. */
internal class Assignment(val secret: ByteArray, val template: String)
