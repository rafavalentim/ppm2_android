package br.com.correios.ppm.di

import br.com.correios.ppm.config.IosPermissions
import br.com.correios.ppm.config.Permissions

actual object PermissionsProvider {
    actual fun provide(androidActivityOrNull: Any?): Permissions = IosPermissions()
}