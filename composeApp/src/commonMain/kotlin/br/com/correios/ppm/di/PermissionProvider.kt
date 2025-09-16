package br.com.correios.ppm.di

import br.com.correios.ppm.config.Permissions

/**
 * Factory multiplataforma. Em Android você deve passar uma FragmentActivity.
 * Nos demais alvos, o parâmetro é ignorado.
 */
expect object PermissionsProvider {
    fun provide(androidActivityOrNull: Any? = null): Permissions
}