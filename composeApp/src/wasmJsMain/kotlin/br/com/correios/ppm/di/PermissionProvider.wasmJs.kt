package br.com.correios.ppm.di

import br.com.correios.ppm.config.Permissions
import br.com.correios.ppm.config.WasmJsPermissions

actual object PermissionsProvider {
    actual fun provide(androidActivityOrNull: Any?): Permissions {
        return WasmJsPermissions()
    }
}