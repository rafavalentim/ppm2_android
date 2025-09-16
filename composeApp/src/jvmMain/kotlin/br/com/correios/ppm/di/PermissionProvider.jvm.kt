package br.com.correios.ppm.di

import br.com.correios.ppm.config.JvmPermissions
import br.com.correios.ppm.config.Permissions

actual object PermissionsProvider {
    actual fun provide(androidActivityOrNull: Any?): Permissions = JvmPermissions()
}