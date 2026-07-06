package br.com.correios.ppm.data

import platform.Foundation.NSUserDefaults

actual class KeyValueStorage {
    private val defaults = NSUserDefaults.standardUserDefaults()

    actual fun putString(key: String, value: String) {
        defaults.setObject(value, forKey = key)
    }

    actual fun getString(key: String, defaultValue: String?): String? {
        return defaults.stringForKey(key) ?: defaultValue
    }

    actual fun putBoolean(key: String, value: Boolean) {
        defaults.setObject(value, forKey = key)
    }

    actual fun getBoolean(key: String, value: Boolean?): Boolean? {
        return if (defaults.objectForKey(key) != null) defaults.boolForKey(key) else value
    }
}
