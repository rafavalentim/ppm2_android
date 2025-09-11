package br.com.correios.ppm.data

expect class KeyValueStorage {

    fun putString(key: String, value: String)
    fun getString(key: String, defaultValue: String? = null): String?

}


//✅ Assim, no código compartilhado (commonMain) você chama apenas:
//
//val storage = KeyValueStorage()
//storage.putString("token", "abc123")
//val token = storage.getString("token")