package br.com.correios.ppm.config

class JvmPermissions : Permissions {
    override suspend fun ensure(vararg permissions: AppPermission): Map<AppPermission, PermissionStatus> {
        val out = mutableMapOf<AppPermission, PermissionStatus>()
        permissions.forEach { p ->
            out[p] = when (p) {
                // Não existe prompt no desktop: marque o que faria sentido como Granted
                AppPermission.Internet, AppPermission.NetworkState,
                AppPermission.Vibrate, AppPermission.WifiState,
                AppPermission.ChangeWifiState -> PermissionStatus.Granted

                // Sem equivalente no desktop “puro”
                AppPermission.Camera,
                AppPermission.FineLocation, AppPermission.CoarseLocation,
                AppPermission.ReadPhoneState,
                AppPermission.ReadMediaImages, AppPermission.ReadMediaVideo, AppPermission.ReadMediaAudio,
                AppPermission.ManageExternalStorage, AppPermission.RequestInstallPackages ->
                    PermissionStatus.Error("Permissão não aplicável em JVM desktop")
            }
        }
        return out
    }
}