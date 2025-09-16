package br.com.correios.ppm.data

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import br.com.correios.ppm.config.AppContext

actual class KeyValueStorage actual constructor() {
    private val prefs: SharedPreferences by lazy {
        val ctx: Context = AppContext.appContext
        ctx.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    }

    actual fun putString(key: String, value: String) {
        prefs.edit { putString(key, value) }
    }

    actual fun getString(key: String, defaultValue: String?): String? {
        return prefs.getString(key, defaultValue)
    }

    actual fun putBoolean(key: String, value: Boolean) {
        prefs.edit{ putBoolean(key, value) }
    }

    actual fun getBoolean(key: String,value: Boolean?): Boolean? {
       return value?.let { prefs.getBoolean(key, it) }
    }
}
