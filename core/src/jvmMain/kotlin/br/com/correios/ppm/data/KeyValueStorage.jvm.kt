package br.com.correios.ppm.data

import java.util.prefs.Preferences

actual class KeyValueStorage {
    private val prefs: Preferences = Preferences.userRoot().node("MyApp")

    actual fun putString(key: String, value: String) {
        prefs.put(key, value)
        prefs.flush()
    }

    actual fun getString(key: String, defaultValue: String?): String? {
        return prefs.get(key, defaultValue)
    }

    actual fun putBoolean(key: String, value: Boolean) {
        prefs.putBoolean(key, value)
        prefs.flush()
    }

    actual fun getBoolean(key: String, value: Boolean?): Boolean? {
        return if (prefs.get(key, null) != null) prefs.getBoolean(key, false) else value
    }
}
