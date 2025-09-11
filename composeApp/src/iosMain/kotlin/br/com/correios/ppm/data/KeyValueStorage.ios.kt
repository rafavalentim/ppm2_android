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
}
