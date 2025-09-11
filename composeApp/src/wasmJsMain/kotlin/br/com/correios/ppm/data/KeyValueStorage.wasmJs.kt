package br.com.correios.ppm.data

// wasmJsMain
import kotlinx.browser.window

actual class KeyValueStorage {
    private val storage = window.localStorage

    actual fun putString(key: String, value: String) {
        storage.setItem(key, value)
    }

    actual fun getString(key: String, defaultValue: String?): String? {
        return storage.getItem(key) ?: defaultValue
    }
}
