package br.com.correios.ppm.data

import kotlinx.browser.window

actual class KeyValueStorage {
    private val storage = window.localStorage

    actual fun putString(key: String, value: String) {
        storage.setItem(key, value)
    }

    actual fun getString(key: String, defaultValue: String?): String? {
        return storage.getItem(key) ?: defaultValue
    }

    actual fun putBoolean(key: String, value: Boolean) {
        storage.setItem(key, value.toString())
    }

    actual fun getBoolean(key: String, value: Boolean?): Boolean? {
        return (storage.getItem(key) ?: value) as Boolean?
    }
}
