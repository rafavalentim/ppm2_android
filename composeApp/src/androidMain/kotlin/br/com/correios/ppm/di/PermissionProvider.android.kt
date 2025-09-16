package br.com.correios.ppm.di

import androidx.fragment.app.FragmentActivity
import br.com.correios.ppm.config.AndroidPermissions
import br.com.correios.ppm.config.Permissions

actual object PermissionsProvider {

    actual fun provide(androidActivityOrNull: Any?): Permissions {
        val act = androidActivityOrNull as? FragmentActivity
            ?: error("Android PermissionsProvider.provide requer FragmentActivity")
        return AndroidPermissions(act)
    }

}