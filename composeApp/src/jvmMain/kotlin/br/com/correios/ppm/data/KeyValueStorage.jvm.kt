package br.com.correios.ppm.data

// jvmMain
import java.util.prefs.Preferences

actual class KeyValueStorage {
    private val prefs: Preferences = Preferences.userRoot().node("MyApp")

    actual fun putString(key: String, value: String) {
        prefs.put(key, value)
        prefs.flush() // garante persistência imediata
    }

    actual fun getString(key: String, defaultValue: String?): String? {
        return prefs.get(key, defaultValue)
    }
}
