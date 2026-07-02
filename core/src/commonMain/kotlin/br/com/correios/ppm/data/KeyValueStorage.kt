package br.com.correios.ppm.data

expect class KeyValueStorage() {

    fun putString(key: String, value: String)
    fun getString(key: String, defaultValue: String? = null): String?

    fun putBoolean(key: String, value: Boolean)

    fun getBoolean(key: String, value: Boolean?): Boolean?
}
