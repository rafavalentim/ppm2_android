package br.com.correios.ppm.data

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

actual class KeyValueStorage(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

    actual fun putString(key: String, value: String) {
        prefs.edit { putString(key, value) }
    }

    actual fun getString(key: String, defaultValue: String?): String? {
        return prefs.getString(key, defaultValue)
    }
}
